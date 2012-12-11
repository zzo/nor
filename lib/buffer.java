import org.mozilla.javascript.*;

public class buffer {
    private static final long serialVersionUID = 298976666527337642L;

    public static Scriptable getObject(Context cx, Scriptable scope) throws Exception {
        ScriptableObject newObj = (ScriptableObject)cx.newObject(scope);
        ScriptableObject.defineClass(newObj, Buff.class);
        ScriptableObject.defineClass(newObj, SlowBuffer.class);
        return (Scriptable)newObj;
    }
}
