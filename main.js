#!/usr/bin/env node

var argv, command, parse;
command = require('./command/' + process.argv[2]);
argv = command.parse();
command.run(argv);

