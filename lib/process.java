import org.mozilla.javascript.*;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

public class process extends ScriptableObject {
    private static final long serialVersionUID = 498270592527335642L;
    private static Scriptable moduleLoadList;
    private static Map<String, Scriptable> bindingCache;

    public process() {}

    public void jsConstructor(Scriptable args) {
        bindingCache = new HashMap<String, Scriptable>();
        try {
            moduleLoadList = Context.enter().newArray(this, 0);
            this.put("moduleLoadList", this, moduleLoadList);

            Scriptable stdout = Context.enter().newObject(this);
            this.put("stdout", this, stdout);

            Scriptable stderr = Context.enter().newObject(this);
            this.put("stderr", this, stderr);

            String[] writeFunc = { "write" };
            ((ScriptableObject)stdout).defineFunctionProperties(writeFunc, this.getClass(), EMPTY);
            ((ScriptableObject)stderr).defineFunctionProperties(writeFunc, this.getClass(), EMPTY);

            String[] stdinFunc = { "resume", "setEncoding" };
            Scriptable stdin = Context.enter().newObject(this);
            this.put("stdin", this, stdin);

            String [] processFuncs = { "binding", "cwd", "_needTickCallback" };
            this.defineFunctionProperties(processFuncs, this.getClass(), EMPTY);

            // Someday suck this from real ENV
            Scriptable env = Context.enter().newObject(this);
            this.put("env", this, env);

            this.put("platform", this, "linux");

            this.put("argv", this, args);
        } catch(Exception e) {
            System.err.println("Can't find class: " + e.toString());
        }
    }

    public static void _needTickCallback(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        // call process._tickCallback
        try { Thread.sleep(10); } catch(Exception e) {}
        ScriptableObject.callMethod(thisObj, "_tickCallback", null);
    }

    public static String cwd(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        try {
            return new File( "." ).getCanonicalPath();
        } catch(Exception e) {
            return null;
        }
    }

    public static void write(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        System.out.println(args[0]);
    }

    @Override
    public String getClassName() { return "process"; }

    public static Scriptable binding(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        String module = args[0].toString();
        Scriptable mod = bindingCache.get(module);
        if (mod != null) {
            return mod;
        }

        ScriptableObject.callMethod(moduleLoadList, "push", new Object[] { "Binding " + module });
        mod = loadThatShit(cx, thisObj, module);
        bindingCache.put(module, mod);
        return mod;

    }

    @SuppressWarnings("unchecked")
    private static Scriptable loadThatShit(Context cx, Scriptable sc, String module) {
        try {
            //Scriptable scope = ScriptableObject.getTopLevelScope(sc);
            Class clz = Class.forName(module);
            Method getObj = clz.getMethod("getObject", 
                new Class[] { cx.getClass(), Class.forName("org.mozilla.javascript.Scriptable") }
            );
            Scriptable newObj = (Scriptable)getObj.invoke(null, new Object[] { cx, sc });
            return newObj;
            
            //ScriptableObject.defineClass(scope, (Class)Class.forName(module));
            //Scriptable newObj = cx.newObject(scope, module);
            //return newObj;
        } catch (Exception e) {
            System.err.println("Error loading module: " + module);
            System.err.println(e.toString());
            e.printStackTrace();

            return null;
        }
    }
}
