import org.mozilla.javascript.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;

public class fs {
    private static final long serialVersionUID = 199970599527937692L;
    private static ScriptableObject me;
    private static Context context;
    private static int fdCount = 3;
    private static Map<Integer, FileInputStream> map;
    private static Map<Integer, String> mapName;

    public static Scriptable getObject(Context cx, Scriptable scope) throws Exception {
        ScriptableObject newObj = (ScriptableObject)cx.newObject(scope);
        ScriptableObject.defineClass(newObj, Stats.class);

        String[] globalFuncs = new String[] { "write", "stat", "lstat", "fstat", "open", "close", "read" };
        newObj.defineFunctionProperties(globalFuncs, fs.class, ScriptableObject.EMPTY); 

        newObj.associateValue("map", new HashMap<Integer, File>());
        map = new HashMap<Integer, FileInputStream>();
        mapName = new HashMap<Integer, String>();

        return (Scriptable)newObj;
    }

    // return binding.open(pathModule._makeLong(path), stringToFlags(flags), mode);
    public static int open(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        /*
        System.out.println("OPEN PATH: " + args[0]);
        System.out.println("FLAGS: " + args[1]);
        System.out.println("MODE: " + args[2]);
        */

        fdCount++;
        try {
            map.put(new Integer(fdCount), new FileInputStream(args[0].toString()));
            mapName.put(new Integer(fdCount), args[0].toString());
        } catch (Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }

        return fdCount;
    }

    public static int close(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        FileInputStream fis = map.remove(args[0]);
        mapName.remove(args[0]);

        if (fis != null) {
            try {
                fis.close();
            } catch(Exception e) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
        }

        return 0;
    }

    /*
     * bytesRead = fs.read(fd, buffer, offset, length, position)
     *
     * 0 fd        integer. file descriptor
     * 1 buffer    instance of Buffer
     * 2 offset    integer. offset to start reading into inside buffer
     * 3 length    integer. length to read
     * 4 position  file position - null for current position
     */
    public static int read(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        int fd = ((Integer)args[0]).intValue();
        ScriptableObject buffer = (ScriptableObject)args[1];
        int offset = ((Double)args[2]).intValue();
        int length = ((Double)args[3]).intValue();
        //int position = ((Integer)args[4]).intValue();

        /*
        System.out.println("FD: " + fd);
        System.out.println("BUF: " + buffer);
        System.out.println("offset: " + offset);
        System.out.println("length: " + length);
        */
        //System.out.println("pos: " + position);

        FileInputStream fis = map.get(args[0]);
        byte[] bytes = new byte[length];

        if (fis != null) {
            try {
                int len = fis.read(bytes);
                /*
                System.out.println("READ " + len + " bytes");
                System.out.println(new String(bytes));
                */
                ScriptableObject.callMethod(buffer, "utf8Write", new Object[] { new String(bytes), new Integer(offset), new Integer(len) } );
                return len;
            } catch (Exception e) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
        } else {
            return 0;
        }
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
        if (fd == 1) {
            /*
            System.out.println("PRINTING:");
            System.out.println("offset: " + offset);
            System.out.println("length: " + length);
            System.out.println("data: -" + data + "-");
            */
            System.out.print(data); // print to FD!!!
        } else if (fd == 2) {
            System.err.print(data); // print to FD!!!
        } else {
            System.out.println("DUNNO FD: " + fd + " for: " + data);
        }

        if (sc != null) { /* make callback */ }
        return data.length();
    }

    public static Scriptable stat(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        FunctionObject ctor = (FunctionObject)thisObj.get("Stats", thisObj);
        return ctor.construct(cx, thisObj, args);
    }

    public static Scriptable lstat(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        FunctionObject ctor = (FunctionObject)thisObj.get("Stats", thisObj);
        return ctor.construct(cx, thisObj, args);
    }

    public static Scriptable fstat(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        args[0] = mapName.get(args[0]);
        FunctionObject ctor = (FunctionObject)thisObj.get("Stats", thisObj);
        return ctor.construct(cx, thisObj, args);
    }
}
