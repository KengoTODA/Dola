exports.replace = function (file, js, callback) {
    var XmlStream = require('xml-stream'),
        fs = require('fs'),
        text, result = [],
        stream = new XmlStream(fs.createReadStream(file));
    stream.on('startElement: script', function (script) {
        var src = script.$.src;
        if (src === 'main.js') {
            delete script.$.src;
            text = js;
        } else {
            text = undefined;
        }
    });
    stream.on('updateElement: script', function (script) {
        if (text) {
            script.$text = text;
        }
    });
    stream.on('data', function (data) {
        result.push(data.replace(/&quot;/g, '"'));
    });
    stream.on('end', function () {
        callback(result.join(''));
    });
};
