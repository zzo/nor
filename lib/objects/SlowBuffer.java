import org.mozilla.javascript.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class SlowBuffer extends ScriptableObject {
    private static final long serialVersionUID = 34297528888642L;
    public int length;

    public SlowBuffer() { }
    public SlowBuffer(int length) {
        this.length = length;
        this.setPrototype(Context.enter().newArray((Scriptable)this, length));

        String[] funcs = { "utf8Write", "utf8Slice", "ucs2Write", "ucs2Slice", "asciiWrite", "asciiSlice" };
        this.defineFunctionProperties(funcs, this.getClass(), EMPTY);
    }

    /*
     *   NODEJS encodings:
     * 'ascii' - for 7 bit ASCII data only. This encoding method is very fast, and will strip the high bit if set. Note that this encoding converts a null character ('\0' or '\u0000') into 0x20 (character code of a space). If you want to convert a null character into 0x00, you should use 'utf8'.
     * 'utf8' - Multibyte encoded Unicode characters. Many web pages and other document formats use UTF-8.
     * 'utf16le' - 2 or 4 bytes, little endian encoded Unicode characters. Surrogate pairs (U+10000 to U+10FFFF) are supported.
     * 'ucs2' - Alias of 'utf16le'.
     * 'base64' - Base64 string encoding.
     * 'binary' - A way of encoding raw binary data into strings by using only the first 8 bits of each character. This encoding method is deprecated and should be avoided in favor of Buffer objects where possible. This encoding will be removed in future versions of Node.
     * 'hex' - Encode each byte as two hexadecimal characters.
     *
     *     JAVA encodings:
     *     US-ASCII Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set
     *     ISO-8859-1   ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
     *     UTF-8    Eight-bit UCS Transformation Format
     *     UTF-16BE Sixteen-bit UCS Transformation Format, big-endian byte order
     *     UTF-16LE Sixteen-bit UCS Transformation Format, little-endian byte order
     *     UTF-16   Sixteen-bit UCS Transformation Format, byte order identified by an optional byte-order mark
     */
    private static String convertNodeToJavaEncoding(String nodeEncoding) {
        if (nodeEncoding.equals("ascii")) {
            return "US-ASCII";
        } else if (nodeEncoding.equals("utf8")) {
            return "UTF-8";
        } else if (nodeEncoding.equals("utf16le") || nodeEncoding.equals("ucs2")) {
            return "UTF-16LE";
        } else if (nodeEncoding.equals("utf-16le") || nodeEncoding.equals("ucs-2")) {
            return "UTF-16LE";
        } else {
            return "ISO-8859-1";
        }
    }

    public static int jsStaticFunction_byteLength(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        if (args[1] == Context.getUndefinedValue()) {
            args[1] = "utf8";
        }

        String javaEnc = convertNodeToJavaEncoding(args[1].toString());
        String string = (String)args[0];
        int length = 0;
        try {
            byte b[] = string.getBytes(javaEnc);
            length = b.length;
        } catch(Exception e) {}

        //System.err.println( "byteLength of -" + string + "- is " + length);
        return length;
    }

    public static void jsStaticFunction_makeFastBuffer(
            Context cx, Scriptable thisObj, Object[] args, Function funObj) {

        /*
        int offset = ((Number)args[2]).intValue();
        int len = ((Number)args[3]).intValue();
        ScriptableObject me = (ScriptableObject)args[1];
        Scriptable meProto = me.getPrototype();
        Scriptable meProtoProto = meProto.getPrototype();
        try {
            FastBuffer result = new FastBuffer((ScriptableObject)args[0], offset, len);
            ScriptRuntime.setBuiltinProtoAndParent(result, meProto, TopLevel.Builtins.Array);
            meProto.setPrototype(result);
        } catch(Exception e) {
            System.err.println("Error defining FastBuffer: " + e);
        }
        */
    }

    public int asciiWrite(String str, int offset, int len) {
        return write(str, offset, len, "US-ASCII");
    }

    // write into this buffer at offset
    //public int jsFunction_utf8Write(String str, int offset, int length) {
    public int utf8Write(String str, int offset, int len) {
        return write(str, offset, len, "UTF-8");
    }

    public int ucs2Write(String str, int offset, int len) {
        return write(str, offset, len, "UTF-16LE");
    }

    private int write(String seq, int offset, int len, String encoding) {
        Charset charset = Charset.forName(encoding);
        ByteBuffer bb = ByteBuffer.allocate(len);
        CharBuffer cb = CharBuffer.wrap((CharSequence)seq);

        Scriptable parentScope = ((ScriptableObject)this).getParentScope();
        Scriptable SlowBuffer  = (Scriptable)parentScope.get("SlowBuffer", this);

        try {
            byte[] bytes = seq.getBytes(encoding);
            String zz = new String(bytes, encoding);
            int cl = zz.codePointCount(0, zz.length());

            CoderResult cs = charset.newEncoder().encode(cb, bb, true);
            byte[] barr = bb.array();
            for (int i = 0; i < barr.length; i++) {
                this.put(offset + i, this, barr[i]);
            }

            int charsLeft = cb.limit() - cb.remaining();
            int bytesLeft = bb.limit() - bb.remaining();
            SlowBuffer.put("_charsWritten", SlowBuffer, charsLeft);

            return bytesLeft;

        } catch(Exception e) {}

        return 0;
    }

    public CharSequence ucs2Slice(int start, int end) {
        return slice(start, end, "UTF-16LE");
    }

    public byte[] getBytes(int start, int length) {
        byte[] bbb = new byte[length];
        for (int i = 0; i < bbb.length; i++) {
            Object o = ((ScriptableObject)this).get(i + start, (Scriptable)this);
            bbb[i] = ((Number)o).byteValue();
        }
        return bbb;
    }
    
    public String asciiSlice(int start, int end) {
        return slice(start, end, "US-ASCII");
    }


    public String utf8Slice(int start, int end) {
        return slice(start, end, "UTF-8");
    }

    private String slice(int start, int end, String encoding) {
        byte[] bytes = this.getBytes(start, end - start);
        try {
            return new String(bytes, encoding);
        } catch(Exception e) { System.err.println("ERRING STRINGIFY: " + e); return null; }
    }

    @Override
    public String getClassName() { return "SlowBuffer"; }
}
