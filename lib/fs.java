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

    // bytesWritten = write(fd, data, position, enc, callback)
    // // Wrapper for write(2).
    // //
    // // 0 fd        integer. file descriptor
    // // 1 buffer    the data to write
    // // 2 offset    where in the buffer to start from
    // // 3 length    how much to write
    // // 4 position  if integer, position to write at in the file.
    // //             if null, write from the current position
    public static int write(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        int fd = ((Double)args[0]).intValue();
        int offset = ((Double)args[2]).intValue();
        int length = ((Integer)args[3]).intValue();
        int position = 0;
        Scriptable sc = null;

        //System.out.println(thisObj);
        //System.out.println("BUFF: " + ((ScriptableObject)args[1]).getProperty((Scriptable)args[1], "buffer"));
        //System.out.println("BUFF: " + ScriptableObject.callMethod((ScriptableObject)args[1], "getBuffer", null));
        if (!args[4].equals(Context.getUndefinedValue())) {
            position = ((Double)args[4]).intValue();
        }

        if (args.length > 5 && !args[5].equals(Context.getUndefinedValue())) {
            sc = (Scriptable)args[5];
        }

        /*
        java.util.Set<java.lang.Object> vals = ((NativeObject)args[1]).keySet();
        java.util.Iterator<Object> it = vals.iterator();
        Object[] kk = ((ScriptableObject)args[1]).getAllIds();
        for (int i = 0; i < kk.length; i++) {
            System.out.println("K: " + kk[i]);
        }
        while (it.hasNext()) {
            Object o = it.next();
            System.out.println(o + " = " + ((NativeObject)args[1]).get(o.toString(), (Scriptable)args[1]));
        }
        System.out.println(((NativeObject)args[1]).getClassName());
        if (args.length > 4) {
            // wrapper
        }
        */
        SlowBuffer parent = (SlowBuffer)((NativeObject)args[1]).get("parent", (Scriptable)args[1]);
        StringBuffer buff = (StringBuffer)((ScriptableObject)parent).getAssociatedValue("buffer");
        String data = buff.substring(offset, offset + length);
        System.out.print(data); // print to FD!!!
                if (sc != null) { /* make callback */ }
            return data.length();
    }
}
