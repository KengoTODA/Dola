#!/usr/bin/env node

var replace = require('../command/embed/script').replace,
    n = 0;


exports['test `replace` deletes <script> element exclude <script src="main.js">'] = function (beforeExit, assert) {

    replace('test/embed_script.html', 'console.log("hello, world!");', function (after) {
        assert.equal(
            '<script>console.log("hello, world!");</script>',
            after
        );
        ++n;
    });
    beforeExit(function () {
        assert.equal(1, n);
    });
};
