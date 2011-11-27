#!/usr/bin/env node

function loadPackageJson() {
    if (require('path').existsSync('package.json')) {
        return JSON.parse(require('fs').readFileSync('package.json', 'utf-8'));
    } else {
        return {};
    }
}

function browserify(script) {
    var b = require('browserify')();
    b.append(script);
    return b.bundle();
}

function compressJavaScript(js) {
    var jsp = require('uglify-js').parser,
        pro = require('uglify-js').uglify,
        ast = jsp.parse(js);

    ast = pro.ast_mangle(ast);
    ast = pro.ast_squeeze(ast);
    return pro.gen_code(ast);
}

function scriptMark(fileName) {
    return '&&' + fileName + '&&';
}

function packageAllTo(outputFilePath) {
    var fs = require('fs'),
        path = require('path'),
        Apricot = require('apricot').Apricot,
        _ = require('underscore'),
        scripts = [],
        useModules = !_.isEmpty(loadPackageJson().dependencies);

    Apricot.open('index.html', function (err, doc) {
        if (err) {
            throw err;
        }
        var html = doc.find('script[src]').each(function (item) {
            scripts.push(item.src);
            item.parentNode.insertBefore(doc.document.createTextNode(scriptMark(item.src)), item.nextSibling);
            item.removeAttribute('src');
        }).toHTML;
        _.each(scripts, function (src) {
            var script = fs.readFileSync(src, 'utf-8').toString();
            if (useModules) {
                script = browserify(script);
            }
            // TODO embed asset
            script = compressJavaScript(script);
            html = html.replace(
                '</script>' + scriptMark(src),
                script + '</script>');
        });
        fs.writeFileSync(outputFilePath, html);
    });
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
    packageAllTo(
        require('path').join('result', argv.output) // TODO user may want to change its name
    );
};

