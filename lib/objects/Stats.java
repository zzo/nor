import org.mozilla.javascript.*;
import java.io.File;

public class Stats extends ScriptableObject {
    private static final long serialVersionUID = 34297528882L;
    private File f;
    private long size;

    public void jsConstructor(String file) {
        f = new File(file);
        if (f.exists()) {
            size = f.length();
        } else {
            Context.throwAsScriptRuntimeEx(new Exception("File " + file + " does not exist"));
        }
    }

    public long jsGet_size() {
        return size;
    }

    public boolean jsFunction_isDirectory() {
        return f.isDirectory();
    }

    @Override
    public String getClassName() { return "Stats"; }
}
