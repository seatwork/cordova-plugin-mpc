# cordova-plugin-mpc

This Cordova plugin provides JavaScript API, that allows you to communicate with server through TCP protocol. Currently we support Android platform only.

## Installation

```
cordova plugin add https://github.com/seatwork/cordova-plugin-socket
```

## Usage

Create socket instance and connect to server: 
```
const socket = new Socket()
socket.open('ip', port, () => {
    console.log('Socket is opened.')
  }, (res) => {
    console.error('Socket open failed: ' + res)
  }
)
```

Send message to socket server:
```
socket.write('message' () => {
    console.log('Send message success')
  }, (res) => {
    console.error('Send message failed: ' + res)
  }
)
```

Close socket connection:
```
socket.close(() => {
    console.log('Close success')
  }, (res) => {
    console.error('Close failed: ' + res)
  }
)
```

Set data consumer, close and error events:
```
socket.onData = function(res) {
  console.log(res)
}
socket.onClose = function(res) {
  console.log(res)
}
socket.onError = function(res) {
  console.error(res)
}
```

## Devlopment

```
npm install -g plugman
plugman create --name cordova-plugin-socket --plugin_id net.cloudseat.cordova --plugin_version 0.0.1
cd cordova-plugin-socket

plugman platform add --platform_name android
plugman createpackagejson .
```
