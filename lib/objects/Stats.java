import org.mozilla.javascript.*;
import java.io.File;

public class Stats extends ScriptableObject {
    private static final long serialVersionUID = 34297528882L;
    private File f;
    private long size;

    public void jsConstructor(String file) {
        f = new File(file);
        size = f.length();
    }

    public long jsGet_size() {
        return size;
    }

    @Override
    public String getClassName() { return "Stats"; }
}
