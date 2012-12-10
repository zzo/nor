import org.mozilla.javascript.*;

public class fs {
    private static final long serialVersionUID = 199970599527937692L;

    public static Scriptable getObject(Context cx, Scriptable scope) throws Exception {
        ScriptableObject newObj = (ScriptableObject)cx.newObject(scope);
        ScriptableObject.defineClass(newObj, Stats.class);

        String[] globalFuncs = new String[] { "write" };
        newObj.defineFunctionProperties(globalFuncs, fs.class, ScriptableObject.EMPTY); 

        return (Scriptable)newObj;
    }

    public static int write(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        /*
        args[0] = fd;
        args[1] = buffer;
        args[2] = offset;
        args[3] = length;
        args[4] = position;
        args[5] = wrapper
        */

            System.out.println("FS WRITE: " + args[0]);
            System.out.println("FS WRITE: " + args[1]);
            System.out.println("FS WRITE: " + args[2]);
            System.out.println("FS WRITE: " + args[3]);
            System.out.println("FS WRITE: " + args[4]);

            return args[1].toString().length();
    }
}
