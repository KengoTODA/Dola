#!/usr/bin/env node

var tasks = 0, doneTasks = 0;
exports.parse = function () {
    return require('optimist')
        .usage('Usage: dola init {options...}')
        .option('title', {
            alias: 't',
            desc: 'Title of your new project',
            demand: true
        })
        .option('owner', {
            alias: 'o',
            desc: 'Your name',
            'default': ''
        })
        .option('desciption', {
            alias: 'd',
            desc: 'Description of your project',
            'default': ''
        })
        .option('license', {
            alias: 'l',
            desc: 'License of your new project (APLV2, BSD2, GPL3, LGPL3, MIT)',
            'default': 'MIT'
        })
        .boolean('verbose', {
            alias: 'v',
            desc: 'Verbose option'
        })
        .argv;
};

function finish() {
    process.exit(0);
}

function addTask() {
    tasks++;
    return function () {
        doneTasks++;
        if (doneTasks === tasks) {
            finish();
        }
    };
}

var fs = require('fs'), LICENSE = require('../license'),
    path = require('path');

function createDirectory(argv) {
    fs.mkdirSync(argv.title, '0755');
    fs.mkdirSync(path.join(argv.title, 'minified'), '0755');
}

function createLicense(argv) {
    var text = LICENSE[argv.license];
    if (!text) {
        throw 'Unknown license type: ' + license;
    }
    text = text.replace(/<YEAR>/g, new Date().getYear() + 1900)
        .replace(/<OWNER>/g, argv.owner);
    fs.writeFile(path.join(process.cwd(), argv.title, 'LICENSE'), text, addTask());
}

function createPackageJson(title, desc, licenseType) {
    var packageData = {
        name: title,
        version: '0.0.1',
        description: desc
    };
    fs.writeFile(
        path.join(process.cwd(), title, 'package.json'),
        JSON.stringify(packageData, null, 2),
        addTask());
}

function createReadme(title, desc, licenseType) {
    var text = '';

    fs.writeFile(path.join(process.cwd(), title, 'README.md'), text, addTask());
}

function createSource(title, desc) {
    var esc = require('underscore').escape, html = require('underscore.string').sprintf("<!DOCTYPE html>\n<html>\n<head>\n  <meta charset=\"utf-8\">\n  <title>%s</title>\n  <meta name=\"description\" content=\"%s\"></meta>\n  <link rel=\"stylesheet\" type=\"text/css\" href=\"main.css\" />\n</head>\n<body>\n  <script src=\"main.js\"></script>\n</body>\n</html>", esc(title), esc(desc));
    fs.writeFile(path.join(process.cwd(), title, 'index.html'), html, addTask());
    fs.writeFile(path.join(process.cwd(), title, 'main.js'), '#!/usr/bin/env node\n', addTask());
    fs.writeFile(path.join(process.cwd(), title, 'test.js'), '#!/usr/bin/env node\n', addTask());
    fs.writeFile(path.join(process.cwd(), title, 'main.css'), '@charset "utf-8";\n', addTask());
}

exports.run = function (argv) {
    argv.license = argv.license.toUpperCase();
    createDirectory(argv);
    createLicense(argv);
    createPackageJson(argv.title, argv.description, argv.license);
    createReadme(argv.title, argv.description, argv.license);
    createSource(argv.title, argv.description);
};


