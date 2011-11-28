#!/usr/bin/env node

var embed = require('../lib/embed/assets'),
    path = require('path');
exports['embed gif'] = function (beforeExit, assert) {
    var embedded = embed('img = game.assets["dot.gif"];', path.join(process.cwd(), 'test', 'embed_assets.js'));
    assert.equal('img = game.assets["data:image/gif;base64,R0lGODdhAQABAIAAAP///////ywAAAAAAQABAAACAkQBADs="];', embedded);
};
