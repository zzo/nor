import org.mozilla.javascript.*;
/*
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
*/
public class TTY extends ScriptableObject {
//public class SlowBuffer {
    private static final long serialVersionUID = 34297528888642L;

    public void jsConstructor(int fd, boolean val) {
        System.out.println("new TTY");
    }

    @Override
    public String getClassName() { return "TTY"; }
}
