# cordova-plugin-crop

> Crop an image in a Cordova app


## Install

```
$ npm install --save envvars
```


## Usage

```bash
$ cat .env
export FOO=foo
export FOO_BAR=bar
export BAR_FOO="baz"
```

```js
var envVars = require('envvars');

fs.readFile('.env', 'utf8', function (content) {
  var result = envVars(content)
  // => {foo: 'foo', fooBar: 'bar', barFoo: 'baz'}
})
```


## API

### envVars(input)

#### input

*Required*  
Type: `string`

The string with the exported variables

## License

MIT © [Jeduan Cornejo](https://github.com/jeduan)