import org.mozilla.javascript.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

public class FastBuffer extends NativeArray {
//public class FastBuffer extends ScriptableObject {
    private static final long serialVersionUID = 34297528L;
    public int offset;
    public int length;
    public ScriptableObject sb;

    public FastBuffer() { super(0);  }
    public FastBuffer(ScriptableObject sb, int offset, int length) {
        super(length);
        this.sb = sb;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public Object get(int index, Scriptable start) {
        /*
        if (start.get("offset", (Scriptable)start) == NOT_FOUND) {
            return super.get(index, start);
        }
        */
        return ((ScriptableObject)sb).get(index + offset, (Scriptable)sb);
    }

    @Override
    public boolean has(int index, Scriptable start) {
        if (index < length) {
            return true;
        }
        return false;
    }

    @Override
    public void put(int index, Scriptable s, Object value) {
        System.err.println( "put " + value + " at " + index);
        sb.put(index + offset, sb, ((Number)value).byteValue());
    }

    @Override
    public String getClassName() { return "FastBuffer"; }
}
