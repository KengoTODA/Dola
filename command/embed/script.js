exports.replace = function (file, js, callback) {
    var XmlStream = require('xml-stream'),
        fs = require('fs'),
        stream = new XmlStream(fs.createReadStream(file));
    stream.on('updateElement: script', function (script) {
        var src = script.$.src;
        if (src === 'main.js') {
            script.$.src = null;
            script.$text = js;
        } else {
            return '';
        }
    });
    stream.on('data', callback);
};
