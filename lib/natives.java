import org.mozilla.javascript.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class natives {
    private static final long serialVersionUID = 298970592547642L;

    public static Scriptable getObject(Context cx, Scriptable scope) {
        Scriptable newObj = cx.newObject(scope);
        loadNatives((ScriptableObject)newObj);
        return newObj;
    }

    public static void loadNatives(ScriptableObject me) {
        File nativeDir = new File("node_natives");
        File[] nativesjs = nativeDir.listFiles();
        for (int i = 0; i < nativesjs.length; i++) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(nativesjs[i]));
                StringBuffer sb = new StringBuffer();
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    sb.append(sCurrentLine + "\n");
                }
    
                String name = nativesjs[i].getName().replace(".js", "");
                me.put(name, me, sb.toString());

            } catch (java.io.FileNotFoundException e) {
                System.err.println("Cannot load native module: " + nativesjs[i].getName());
            } catch (java.io.IOException io) {
                System.err.println("IOException loading native module: " + io.toString());
            }
        }

        me.put("config", me, "#gyp\n{\n}\n");
    }
}
