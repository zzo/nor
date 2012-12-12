import org.mozilla.javascript.*;

public class SlowBuffer extends ScriptableObject {
    private static final long serialVersionUID = 34297528888642L;
    private StringBuffer buffer;

    public void jsConstructor(int length) {
        //System.out.println("JS CONS SLOW BUFFER: " + length);
        //this.buffer = new StringBuffer(length);
        this.buffer = new StringBuffer(length);
        //this.associateValue("buffer", new StringBuffer(length));
        //System.out.println("creating buffer of length: " + length);
    }

    // be smarter
    public static int jsStaticFunction_byteLength(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        String buf = args[0].toString();
        String enc = args[1].toString();
        return buf.length();
    }

    // Need to figure out what to do here
    public static void jsStaticFunction_makeFastBuffer(
            Context cx, Scriptable thisObj, Object[] args, Function funObj) {

        /*
        System.out.println("MAKE FAST BUFFER");
        Scriptable scope = ScriptableObject.getTopLevelScope(thisObj);

        Scriptable parent = (Scriptable)args[0];
        Scriptable me = (Scriptable)args[1];
        int offset = ((Double)args[2]).intValue();
        int length = ((Integer)args[3]).intValue();

        System.out.println("PARENT: " + ((ScriptableObject)args[0]).getClassName());
        System.out.println("ME: " + ((ScriptableObject)args[1]).getClassName());
        System.out.println("offset: " + offset);
        System.out.println("length: " + length);
        //System.out.println(ScriptableObject.callMethod((ScriptableObject)parent, "getBuffer", new Object[]{}));
        System.out.println("ASS: " + ((ScriptableObject)parent).getAssociatedValue("buffer"));
        */

        /*
        // slice what we got
        StringBuffer currentBuffer = (StringBuffer)((ScriptableObject)parent).getAssociatedValue("buffer");
        String newstr = currentBuffer.substring(offset, length);

        System.out.println("NEW BUFFER: " + newstr);

        // Create new buffer obj w/new string
        //Scriptable newObj = cx.newObject(scope, "Buffer", new Object[] { new Integer(newstr.length()) });
        Scriptable newObj = cx.newObject(scope, "Buffer", new Object[] { "mark" });

        System.out.println("SET ASS");
        ((StringBuffer)((ScriptableObject)newObj).getAssociatedValue("buffer")).append(newstr);

        System.out.println("RETURN NEW OBJ");
        //return newObj;
        */
        //return (Scriptable)args[1];
    }

    // write into this buffer at offset
    //public int jsFunction_utf8Write(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
    public int jsFunction_utf8Write(String str, int offset, int length) {

        /*
        System.out.println("Writing into buffer:");
        System.out.println("str now: -" + str + "-");
        System.out.println("off now: " + offset);
        System.out.println("len now: " + length);
        */
//        System.out.println("buff len: " + this.buffer.length());

        /*
        StringBuffer sb = ((StringBuffer)this.getAssociatedValue("buffer"));
        if (offset + length > sb.length()) {
            sb.setLength((offset + length) * 2);
        }
        */
        //System.out.println("buff len: " + sb.length());

        //((StringBuffer)this.getAssociatedValue("buffer")).insert(offset, str);
        //return str.length();
        buffer.insert(offset, str);
        return str.length();
    }

    public String jsFunction_utf8Slice(int start, int end) {
        //StringBuffer sb = ((StringBuffer)this.getAssociatedValue("buffer"));
        return buffer.substring(start, end);
    }
    /*
    public StringBuffer jsGet_buffer() {
        return this.buffer;
    }
    */

    @Override
    public String getClassName() { return "SlowBuffer"; }
}
