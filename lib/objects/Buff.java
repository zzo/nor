import org.mozilla.javascript.*;
/*
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
*/
public class Buff extends ScriptableObject {
    private static final long serialVersionUID = 34297528888642L;
    public StringBuffer buffer;

    public static Object jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr) {
        System.out.println("IN BUFF CONS");
        //this.buffer = new StringBuffer();
        //((ScriptableObject)thisObj).associateValue("buffer", new StringBuffer());
        return null;
    }

    /*
    public void jsConstructor(int length) {
        System.out.println("Buffer cons : " + length);
        this.buffer = new StringBuffer(length);
        this.associateValue("buffer", buffer);
    }
    */

    public String jsFunction_getBuffer() {
        System.err.println("GET BUFFER in Buffer");
        return this.buffer.toString();
    }

    public StringBuffer jsGet_buffer() {
        return this.buffer;
    }

    @Override
    public String getClassName() { return "Buffer"; }
}
