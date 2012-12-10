import org.mozilla.javascript.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class natives {
    private static final long serialVersionUID = 298970592547642L;
    // Wish i could figure these out dynamically but since bundled in jar cannot?
    private static String[] node_natives = new String[] { "_debugger.js", "_linklist.js", "assert.js", "buffer.js", "buffer_ieee754.js", "child_process.js", "cluster.js", "console.js", "constants.js", "crypto.js", "dgram.js", "dns.js", "domain.js", "events.js", "freelist.js", "fs.js", "http.js", "https.js", "module.js", "net.js", "os.js", "path.js", "punycode.js", "querystring.js", "readline.js", "repl.js", "stream.js", "string_decoder.js", "sys.js", "timers.js", "tls.js", "tty.js", "url.js", "util.js", "vm.js", "zlib.js" };

    public static Scriptable getObject(Context cx, Scriptable scope) {
        Scriptable newObj = cx.newObject(scope);
        loadNatives((ScriptableObject)newObj);
        return newObj;
    }

    public static void loadNatives(ScriptableObject me) {
        for (int i = 0; i < node_natives.length; i++) {
            try {
                InputStream input = natives.class.getResourceAsStream("node_natives/" + node_natives[i]);
                BufferedReader br = new BufferedReader(new InputStreamReader(input));

                StringBuffer sb = new StringBuffer();
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    sb.append(sCurrentLine + "\n");
                }
    
                String name = node_natives[i].replace(".js", "");
                me.put(name, me, sb.toString());

            } catch (java.io.FileNotFoundException e) {
                System.err.println("Cannot load native module: " + node_natives[i]);
            } catch (java.io.IOException io) {
                System.err.println("IOException loading native module: " + io.toString());
            }
        }

        me.put("config", me, "#gyp\n{\n}\n");
    }
}
