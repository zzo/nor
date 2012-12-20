//package experimental.users.trostler.serverjs;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;

import org.mozilla.javascript.*;
/**
 *
 * Collects its arguments from the command line, executes the
 * script, and prints the result.
 *
 * @author trostler@google.com
 */
public class RunJS {
  private static String baseDir = "/";

  public static void main(String args[]) {
    RunJS rj = new RunJS(args);
  }

  public RunJS(String[] args) {
    // Creates and enters a Context. The Context stores information
    // about the execution environment of a script.
    Context cx = Context.enter();
    boolean onlyCheck = false;
    String mainFile = "";

    try {
      // Initialize the standard objects (Object, Function, etc.)
      // This must be done before scripts can be executed. Returns
      // a scope object that we use in later calls.
      ScriptableObject scope = cx.initStandardObjects();

      InputStream input = this.getClass().getResourceAsStream("node.js");
      Reader file = new InputStreamReader(input);

      // Now evaluate the mainFile
      Object result = cx.evaluateReader(scope, file, mainFile, 0, null);

      Object[] argsArray = new Object[args.length + 1];
      for (int i = 0; i < args.length+1; i++) {
          if (i == 0) {
              argsArray[0] = "node.js";
          } else {
              argsArray[i] = args[i-1];
          }
      }

          ScriptableObject.defineClass(scope, process.class);
          Scriptable process = cx.newObject(scope, "process", new Object[] { cx.newArray(scope, argsArray) });

            ((Function)result).call(cx, scope, scope, new Object[] { process });
      } catch(StackOverflowError soe) {
          /*
          System.out.println( "CAUGHT SOE");
          cx.throwAsScriptRuntimeEx(soe);
          */
          //              ScriptRuntime.typeError("msg.arg.isnt.array");
          String msg = ScriptRuntime.getMessage0("msg.arraylength.bad");
          throw ScriptRuntime.constructError("RangeError", msg);
      } catch(Exception e) {
          System.err.println("Cannot load up process class: " + e.toString());
    } finally {
      // Exit from the context.
      Context.exit();
    }
  }
}
