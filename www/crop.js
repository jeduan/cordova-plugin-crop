/* global cordova */
(function () {
  var crop = exports.crop = function cropImage (success, fail, image, options) {
    options = options || {}
    return cordova.exec(success, fail, 'CropPlugin', 'cropImage', [image, options])
  }

  exports.cropAsync = function cropAsync (image, options) {
    return new Promise(function (resolve, reject) {
      crop(resolve, reject, image, options)
    })
  }
}())
