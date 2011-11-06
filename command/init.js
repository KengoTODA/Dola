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

var fs = require('fs'), LICENSE = require('./license');

function createLicense(type) {
    var text = LICENSE[type]; 
    if (!text) {
        throw 'Unknown license type: ' + type;
    }
    fs.writeFile(process.cwd() + '/LICENSE', text, addTask());
}

function createPackageJson(title, desc, licenseType) {
    var packageData = {
        name: title,
        version: '0.0.1',
        description: desc
    };
    fs.writeFile(
        process.cwd() + '/package.json',
        JSON.stringify(packageData, null, 2),
        addTask());
}

function createReadme(title, desc, licenseType) {
    var text = '';

    fs.writeFile('README.md', text, addTask());
}

function createSource(title, desc) {
    var esc = require('esc'), html = require('util').format("<!DOCTYPE html>\n<html>\n<head>\n  <meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8\" />\n  <title>%s</title>\n  <meta name=\"description\" content=\"%s\"\n></meta\n  <link rel=\"stylesheet\" type=\"text/css\" href=\"main.css\" />\n</head>\n<body>\n  <script src=\"main.js\"></script>\n</body>", esc(title), esc(desc));
    fs.writeFile('index.html', html, addTask());
    fs.writeFile('main.js', '#!/usr/bin/env node\n', addTask());
    fs.writeFile('test.js', '#!/usr/bin/env node\n', addTask());
    fs.writeFile('main.css', '@charset "utf-8";\n', addTask());
}

exports.run = function (argv) {
    argv.license = argv.license.toUpperCase();
    createLicense(argv.license);
    createPackageJson(argv.title, argv.description, argv.license);
    createReadme(argv.title, argv.description, argv.license);
    createSource(argv.title, argv.description);
};


