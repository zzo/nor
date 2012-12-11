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
       if (System.console() != null) {
           return true;
       } else {
           return false;
       }
    }

    public static String guessHandleType(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        int fd = ((Double)args[0]).intValue();
        if (args[0].toString().equals("0")) {
            System.out.println("CHECK STDIN");
        }

        if (args[0].toString().equals("1")) {
            System.out.println("CHECK STDOUT");
        }

        if (args[0].toString().equals("0")) {
            System.out.println("CHECK STDERR");
        }
        if (isaTTY(fd)) {
            return "TTY";
        }

        return "FILE";
    }

    public static boolean isTTY(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        int fd = ((Double)args[0]).intValue();
        return isaTTY(fd);
    }
}
