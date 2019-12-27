# cordova-plugin-mpc

This Cordova plugin provides JavaScript API, that allows you to communicate with MPD (https://www.musicpd.org) server. Currently we support Android platform only.

## Installation

```
cordova plugin add https://github.com/seatwork/cordova-plugin-mpc
```

## Simple Usage

Connect MPD server with window.mpc: 
```
mpc.connect('10.0.0.2', 6600, res => {
  console.log('connect success')
}, err => {
  console.error('connect failed: ', err)
})
```

Send command message to MPD server:
```
mpc.command(message, res => {
  console.log('receive message: ', res)
})
```
