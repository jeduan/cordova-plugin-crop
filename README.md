# cordova-plugin-crop-on-steroids

> Crop an image in a Cordova app


## Install

```
$ cordova plugin add --save cordova-plugin-crop-on-steroids
```


## Usage

```js
plugins.crop(function success () {

}, function fail () {

}, '/path/to/image', options)
```

or, if you are running on an environment that supports Promises
(Crosswalk, Android >= KitKat, iOS >= 8)

```js
plugins.crop.promise('/path/to/image', options)
.then(function success (newPath) {

})
.catch(function fail (err) {

})
```

## API

 * quality: Number
The resulting JPEG quality. default: 100

### ANDROID ONLY
 * in order to target a certain aspect ratio, the options value must be an array of the two dimensions: `[width, height]` 


### Libraries used

 * iOS: [PEPhotoCropEditor](https://github.com/kishikawakatsumi/PEPhotoCropEditor)
 * Android: [android-crop](https://github.com/jdamcd/android-crop)

## License

MIT © [Jeduan Cornejo](https://github.com/jeduan)