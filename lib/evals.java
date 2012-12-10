import org.mozilla.javascript.*;

public class evals {
    private static final long serialVersionUID = 298970592527337642L;

    public static Scriptable getObject(Context cx, Scriptable scope) throws Exception {
        ScriptableObject newObj = (ScriptableObject)cx.newObject(scope);
        ScriptableObject.defineClass(newObj, NodeScript.class);
        return (Scriptable)newObj;
    }
}
