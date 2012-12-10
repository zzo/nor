import org.mozilla.javascript.*;
/*
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
*/
public class SlowBuffer extends Buff {
    private static final long serialVersionUID = 34297528888642L;
    private StringBuffer buffer;

    public void jsConstructor(int length) {
        this.buffer = new StringBuffer(length);
    }

    // be smarter
    public static int jsStaticFunction_byteLength(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        String buf = args[0].toString();
        String enc = args[1].toString();
        return buf.length();
    }

    // Need to figure out what to do here
    public static Scriptable jsStaticFunction_makeFastBuffer(
            Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        return (Scriptable)args[1];  // 'this'
    }

    public static int jsFunction_utf8Write(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        int offset = ((Double)args[1]).intValue();
        int maxlen = 0;

        if (args.length > 1) {
            maxlen = ((Double)args[2]).intValue();
        }

        System.out.print(args[0].toString());
        return args[0].toString().length();
    }

    @Override
    public String getClassName() { return "SlowBuffer"; }
}
