import org.mozilla.javascript.*;
import java.io.File;

public class Stats extends ScriptableObject {
    private static final long serialVersionUID = 34297528882L;
    private File f;

    public void jsConstructor(String file) {
        System.out.println("NEW STATS Z: " + file);
        f = new File(file);
    }

    @Override
    public String getClassName() { return "Stats"; }
}
