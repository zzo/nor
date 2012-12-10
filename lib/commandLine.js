/**
 * @fileoverview command line parser
 */
exports = {
  sep: '--'
  , parse: function(args) {
    var obj = {}, arg, smArg, val, n;
    while(arg = args.shift()) {
      if (arg === exports.sep) { break; }
      if (arg.match(exports.sep)) {
        smArg = arg.replace(exports.sep, '');
        if (args[0] && !args[0].match(exports.sep)) {
          val = args.shift();
          obj[smArg] = val;
        } else {
          obj[smArg] = true;
        }
      }
    }

    return { parameters: obj, leftover: args };
  }
};
