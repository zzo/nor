import org.mozilla.javascript.*;
/*
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
*/
public class Buff extends ScriptableObject {
    private static final long serialVersionUID = 34297528888642L;

    /*
    public void jsConstructor(int length) {
        System.out.println("JSCONS BUFF");
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
    */

    @Override
    public String getClassName() { return "Buffer"; }
}
