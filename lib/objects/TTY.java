import org.mozilla.javascript.*;
/*
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
*/
import java.util.Map;

public class TTY extends ScriptableObject {
//public class SlowBuffer {
    private static final long serialVersionUID = 34297528888642L;
    private int fd;

    public void jsConstructor(int fd, boolean val) {
        this.fd = fd;
    }

    @Override
    public String getClassName() { return "TTY"; }

    public void jsFunction_readStop() { }

    public void jsFunction_readStart() { }

    public void jsFunction_setRawMode(boolean val) {
        System.out.println("set raw mode: " + val);
    }

    public Scriptable jsFunction_writeUtf8String(String data) {
        // send to correct fd!
        Scriptable ret = Context.enter().newObject(this);
        if (this.fd == 1) {
            System.out.print(data);
            return ret;
        } else if (this.fd == 2) {
            System.err.print(data);
            return ret;
        } else {
            System.err.println("SEND TO SOME OTHER FD: " + data);
            return null;
        }
    }

    public Scriptable jsFunction_getWindowSize() {
        Map<String, String> env = System.getenv();
        String lines = System.getenv("LINES");
        String cols = System.getenv("COLUMNS");

        if (lines != null && cols != null) {
            //System.out.println("L: " + lines + " cols: " + cols);
            Object[] res = new Object[] { cols, lines };
            return Context.enter().newArray(this, res);
        } else {
            return null;
        }
    }
}
