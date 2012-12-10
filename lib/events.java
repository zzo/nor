import org.mozilla.javascript.*;
/*
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
*/

public class events extends ScriptableObject {
    private static final long serialVersionUID = 2989705927337642L;
    public events() { System.out.println("CREATING NEW events"); }

    @Override
    public String getClassName() { return "events"; }
}
