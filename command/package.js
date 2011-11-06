#!/usr/bin/env node

function joinJavaScripts() {
    var browserify = require('browserify'),
        embedAssets = require('./embed/assets'),
        b = browserify();
    b.register('.coffee', function (body, file) {
        return embedAssets(require('coffee-script').compile(body), file);
    });
    b.register('.js', function (body, file) {
        return embedAssets(body, file);
    });
    b.append('main.js'); // TODO user may want to change its name
    return b.bundle();
}

function compressJavaScripts(js) {
    var jsp = require('uglify-js').parser,
        pro = require('uglify-js').uglify,
        ast = jsp.parse(js);

    ast = pro.ast_mangle(ast);
    ast = pro.ast_squeeze(ast);
    return pro.gen_code(ast);
}

function packageAll(filePath, js) {
    var fs = require('fs'),
        XmlStream = require('xml-stream'),
        stream = fs.createReadStream('index.html'), // TODO user may want to change its name
        xml = new XmlStream(stream);

    
}
 
exports.parse = function () {
    return require('optimist')
        .usage('Usage: dola package {options...}')
        .option('output', {
            alias: 'o',
            "default": 'index.html'
        })
        .argv;
};

exports.run = function (argv) {
    var js = joinJavaScripts();
    js = compressJavaScripts(js);
    packageAll(
        require('path').join('result', argv.output), // TODO user may want to change its name
        js
    );
};

