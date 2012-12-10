import org.mozilla.javascript.*;
/*
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
*/

public class constants {
    private static final long serialVersionUID = 693330592527337642L;

    public static Scriptable getObject(Context cx, Scriptable scope) throws Exception {
        ScriptableObject newObj = (ScriptableObject)cx.newObject(scope);

        newObj.putConst("O_RDONLY", newObj, new Integer(0x000));
        newObj.putConst("O_WRONLY", newObj, new Integer(0x001));
        newObj.putConst("O_RDWR", newObj, new Integer(0x002));

        newObj.putConst("S_IFMT", newObj, new Integer(0170000));
        newObj.putConst("S_IREG", newObj, new Integer(0100000));
        newObj.putConst("S_IFDIR", newObj, new Integer(0040000));
        newObj.putConst("S_IFCHR", newObj, new Integer(0020000));
        newObj.putConst("S_IFBLK", newObj, new Integer(0060000));
        newObj.putConst("S_IFIFO", newObj, new Integer(0010000));
        newObj.putConst("S_IFLINK", newObj, new Integer(0120000));
        newObj.putConst("S_IFSOCK", newObj, new Integer(0140000));

        newObj.putConst("O_CREAT", newObj, new Integer(0x0200));
        newObj.putConst("O_TRUNC", newObj, new Integer(0x0400));
        newObj.putConst("O_EXCL", newObj, new Integer(0x0800));

        newObj.putConst("O_NOCTTY", newObj, new Integer(0x20000));

        newObj.putConst("O_APPEND", newObj, new Integer(0x0008));
        newObj.putConst("O_DIRECTORY", newObj, new Integer(0x100000));
        newObj.putConst("O_SYMLINK", newObj, new Integer(0x200000));

        newObj.putConst("O_NOFOLLOW", newObj, new Integer(0x0100));
        newObj.putConst("O_SYNC", newObj, new Integer(0x0080));

        newObj.putConst("S_IRWXU", newObj, new Integer(0000700));
        newObj.putConst("S_IRUSR", newObj, new Integer(0000400));
        newObj.putConst("S_IWUSR", newObj, new Integer(0000200));
        newObj.putConst("S_IXUSR", newObj, new Integer(0000100));

        newObj.putConst("S_IRWXG", newObj, new Integer(0000070));
        newObj.putConst("S_IRGRP", newObj, new Integer(0000040));
        newObj.putConst("S_IWGRP", newObj, new Integer(0000020));
        newObj.putConst("S_IXGRP", newObj, new Integer(0000010));

        newObj.putConst("S_IRWXO", newObj, new Integer(0000007));
        newObj.putConst("S_IROTH", newObj, new Integer(0000004));
        newObj.putConst("S_IWOTH", newObj, new Integer(0000002));
        newObj.putConst("S_IXOTH", newObj, new Integer(0000001));

        return (Scriptable)newObj;
    }
}
