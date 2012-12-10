import org.mozilla.javascript.*;

public class fs {
    private static final long serialVersionUID = 199970599527937692L;

    public static Scriptable getObject(Context cx, Scriptable scope) throws Exception {
        ScriptableObject newObj = (ScriptableObject)cx.newObject(scope);
        ScriptableObject.defineClass(newObj, Stats.class);
        return (Scriptable)newObj;
    }
}
