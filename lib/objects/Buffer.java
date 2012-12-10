import org.mozilla.javascript.*;
/*
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
*/
public class Buffer extends ScriptableObject {
    private static final long serialVersionUID = 34297528888642L;
    private StringBuffer buffer;

    public void jsConstructor(int length) {
        this.buffer = new StringBuffer(length);
    }

    public static int jsStaticFunction_byteLength(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        String buf = args[0].toString();
        String enc = args[1].toString();
        System.out.println("length of -" + buf + "- is " + buf.length());
        return buf.length();
    }

    @Override
    public String getClassName() { return "Buffer"; }
}
