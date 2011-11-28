#!/usr/bin/env node
module.exports = function (body, filePath) {
    var dirPath = require('path').dirname(filePath),
        fs = require('fs'),
        mime = require('mime'),
        burrito = require('burrito');
    return burrito(body, function (node) {
        var p = node.parent(), n, fileName, fileData, mimeType;
        if (!p || node.name !== 'string') {
            return;
        }
        n = p.value[0];
        if (p.name === 'sub' && n[0] === 'dot' && n[2] === 'assets') {
            fileName = node.value[0];
            fileData = fs.readFileSync(dirPath + '/' + fileName).toString('base64');
            mimeType = mime.lookup(fileName);
            node.wrap('"data:' + mimeType + ';base64,' + fileData + '"');
        }
    });
};
