import org.mozilla.javascript.*;
/*
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
*/
public class NodeScript extends ScriptableObject {
    private static final long serialVersionUID = 34233338888642L;

    public void jsConstructor() {
        //this.defineFunctionProperties(new String[] { "runInThisContext"} , this.getClass(), EMPTY);
    }

    @Override
    public String getClassName() { return "NodeScript"; }

    public static ScriptableObject jsStaticFunction_runInThisContext(
            Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        Object o = null;
        try {
            o = cx.evaluateString(thisObj, args[0].toString(), args[1].toString(), 0, null);
        } catch(Exception e) {
            System.err.println("Died evaluating " + args[1]);
            System.err.println(e.toString());
        }

        return (ScriptableObject)o;
    }


}
