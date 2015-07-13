/* global cordova */
(function () {
  var crop = module.exports = function cropImage (success, fail, image, options) {
    options = options || {}
    return cordova.exec(success, fail, 'CropPlugin', 'cropImage', [image, options])
  }

  module.exports.promise = function cropAsync (image, options) {
    return new Promise(function (resolve, reject) {
      crop(resolve, reject, image, options)
    })
  }
}())
