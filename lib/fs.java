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
        int fd = ((Number)args[0]).intValue();
        ScriptableObject buffer = (ScriptableObject)args[1];
        int offset = ((Number)args[2]).intValue();
        int length = ((Number)args[3]).intValue();
        //int position = ((Number)args[4]).intValue();

        FileInputStream fis = map.get(args[0]);
        byte[] bytes = new byte[length];

        if (fis != null) {
            try {
                int len = fis.read(bytes);
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
        int fd = ((Number)args[0]).intValue();
        int offset = ((Number)args[2]).intValue();
        int length = ((Number)args[3]).intValue();
        int position = 0;
        Scriptable sc = null;

        if (!args[4].equals(Context.getUndefinedValue())) {
            position = ((Number)args[4]).intValue();
        }

        if (args.length > 5 && !args[5].equals(Context.getUndefinedValue())) {
            sc = (Scriptable)args[5];
        }

        SlowBuffer parent = (SlowBuffer)((NativeObject)args[1]).get("parent", (Scriptable)args[1]);
        int off = ScriptRuntime.toInt32(((Scriptable)args[1]).get("offset", (Scriptable)args[1]));
        byte[] bytes = parent.getBytes(off, length);
        if (fd == 1) {
            System.out.write(bytes, 0, length);
        } else if (fd == 2) {
            System.err.write(bytes, 0, length);
        } else {
            System.err.println("DUNNO FD: " + fd + " for: " + bytes);
        }

        if (sc != null) { /* make callback */ }
        return length;
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
