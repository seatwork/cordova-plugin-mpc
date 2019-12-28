/**
 * MPD (Music Player Daemon) Client Plugin for Cordova.
 * Copyright (c) 2019, CLOUDSEAT Inc.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses>.
 *
 * @author AiChen
 * @copyright (c) 2019, CLOUDSEAT Inc.
 * @license https://www.gnu.org/licenses
 * @link https://www.cloudseat.net
 */

package net.cloudseat.cordova;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONException;

/**
 * Plugin Main Class
 */
public class MpcPlugin extends CordovaPlugin {

    // MPD message FLAGS
    private static final String MPD_VERSION = "OK MPD ";
    private static final String MPD_OK = "OK";
    private static final String MPD_ERROR = "ACK";

    // TCP socket
    private Socket socket;

    /**
     * Plugin main method
     */
    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext)
        throws JSONException {

        switch (action) {
            case "connect": connect(args, callbackContext); break;
            case "command": command(args, callbackContext); break;
            default: callbackContext.error("Undefined method:" + action);
            return false;
        }
        return true;
    }

    /**
     * Connect MPD server with host and port.
     */
    private void connect(CordovaArgs args, CallbackContext callbackContext)
        throws JSONException  {
        String host = args.getString(0);
        int port = args.getInt(1);

        try {
            if (!isSocketAlive()) {
                socket = new Socket(host, port);
            }
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    /**
     * Write command to MPD server.
     * @link https://www.musicpd.org
     */
    private void command(CordovaArgs args, CallbackContext callbackContext)
        throws JSONException  {
        String command = args.getString(0) + "\n";

        try {
            socket.getOutputStream().write(command.getBytes("UTF-8"));
            callbackContext.success(receiveMessage());
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    /**
     * Receive message from MPD server.
     * Truncate input stream with MPD FLAGS
     */
    private String receiveMessage() throws Exception {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;

        while (!isMessageEnd(line = reader.readLine())) {
            if (line.startsWith(MPD_ERROR)) {
                throw new Exception(line);
            }
            if (!line.startsWith(MPD_VERSION)) {
                // line = new String(line.getBytes("utf-8"), "utf-8");
                buffer.append(line + "\n");
            }
        }
        return buffer.toString();
    }

    /**
     * Assert message end by read line.
     */
    private boolean isMessageEnd(String line) {
        return line == null || MPD_OK.equals(line);
    }

    /**
     * Check the real state of socket by urgent package
     */
    private boolean isSocketAlive() {
        if (socket == null || !socket.isConnected() || !socket.isBound()) {
            return false;
        }
        try {
            socket.sendUrgentData(0xFF);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
