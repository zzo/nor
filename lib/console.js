var console = {
    _dump: function(what, where) {
        where.println(what);
    }
    , _mkbuff: function(args) {
        var buffer = '';
        args.forEach(function(obj) {
            buffer += DI(obj);
        });

        return buffer;
    }
    , log: function() {
        var args = Array.prototype.slice.call(arguments, 0);
        console._dump(console._mkbuff(args), stdout);
    }
    , error: function() {
        var args = Array.prototype.slice.call(arguments, 0);
        console._dump(console._mkbuff(args), stderr);
    }
};

function DI(obj, indent) {
    var result = "", property, value, od;
    if (indent == null) indent = "";

    if (typeof obj == "object") {
        for (property in obj) {
            value = obj[property];
            if (typeof value === 'string') {
                value = "'" + value + "'";
            } else if (typeof value === 'object') {
                if (value instanceof Array) {
                    value = "[\n" + DI(value, indent + "  ") + "\n" + indent + "]";
                } else {
                    od = DI(value, indent + "  ");
                    value = "\n" + indent + "{\n" + od + "\n" + indent + "}";
               }
          }
            result += indent + "'" + property + "' : " + value + ",\n";
        } // end for
          return result.replace(/,\n$/, "");
    } else {
        return obj;
    }
}
