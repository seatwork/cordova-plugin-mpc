var exec = require('cordova/exec')
module.exports = {

  connect(host, port, success, error) {
    exec(success, error, 'MpcPlugin', 'connect', [ host, port ])
  },

  command(arg, success, error) {
    exec(success, error, 'MpcPlugin', 'command', [ arg ])
  }

}
