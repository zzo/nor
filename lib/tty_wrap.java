import org.mozilla.javascript.*;

public class tty_wrap {
    private static final long serialVersionUID = 298970899333337642L;

    public static Scriptable getObject(Context cx, Scriptable scope) throws Exception {
        ScriptableObject newObj = (ScriptableObject)cx.newObject(scope);
        ScriptableObject.defineClass(newObj, TTY.class);

        String[] isTTY  = new String[] { "isTTY", "guessHandleType" };
        newObj.defineFunctionProperties(isTTY, tty_wrap.class, ScriptableObject.EMPTY); 

        return (Scriptable)newObj;
    }

    public static boolean isaTTY(int fd) {
        String script = "if [ -t " + fd + " ] ; then exit 1; fi";
        try {
            Process proc = Runtime.getRuntime().exec(script);
            if (proc.exitValue() > 0) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            return false; // throw runtime excep?
        }

    }

    public static String guessHandleType(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        int fd = ((Double)args[0]).intValue();
        System.out.println("GUESS HANDLE TYEPF of " + fd);

        if (isaTTY(fd)) {
        System.out.println("it's a TTY " + fd);
            return "TTY";
        }

        System.out.println("it's a FILE " + fd);
        return "FILE";
    }

    public static boolean isTTY(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        int fd = ((Double)args[0]).intValue();
        return isaTTY(fd);
    }
}
