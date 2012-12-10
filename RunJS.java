//package experimental.users.trostler.serverjs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;

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

  private static final Logger logger = Logger.getLogger(RunJS.class.getName());

  public static void main(String args[]) {
    RunJS rj = new RunJS(args);
  }

  public static ScriptableObject require(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
    try {
      Scriptable obj;
      String filename = (String)args[0];

      // First try native object (maybe pass something in different for these?)
      InputStream input = Class.forName("RunJS").getResourceAsStream(baseDir + "lib/" + filename + ".js");
      if (input == null) {
        // no dice - local?
        File f = new File(filename);
        if (!f.exists()) {
          throw cx.reportRuntimeError("require cannot find " + filename);
        }
        input = new FileInputStream(f);
      }

      obj = cx.newObject(thisObj);
      obj.setParentScope(thisObj);
      ScriptableObject.putProperty(obj, "exports", cx.newObject(obj));

      cx.evaluateReader(obj, new InputStreamReader(input, "UTF8"), filename, 0, null);

      return (ScriptableObject)ScriptableObject.getProperty(obj, "exports");

    } catch(Exception e) {
      throw cx.reportRuntimeError("require error: " + e.toString());
    }
  }

  public void loadGlobals(Context cx, ScriptableObject scope) {

      /*
    String[] globalFuncs   = { "require" };
    scope.defineFunctionProperties(globalFuncs, RunJS.class, ScriptableObject.DONTENUM);
    */

    String[] globalModules = { "console" };

    for (int i = 0; i < globalModules.length; i++) {
      // Load up
      InputStream input = this.getClass().getResourceAsStream(baseDir + "lib/" + globalModules[i] + ".js");
      Reader reader = new InputStreamReader(input);
      try {
        Object rez = cx.evaluateReader(scope, reader, globalModules[i], 0, null);
      } catch(IOException ie) {
        System.err.println(ie.toString());
      }
    }
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

      // Dump in stdout & stderr
      Object jsOut = Context.javaToJS(System.out, scope);
      ScriptableObject.putProperty(scope, "stdout", jsOut);
      Object jsErr = Context.javaToJS(System.err, scope);
      ScriptableObject.putProperty(scope, "stderr", jsErr);

      // Drop in command line
      Object[] oo = new Object[args.length];
      for (int i = 0, j = 0; i < args.length; i++, j++) {
        if (args[i].equals("-c")) {
          onlyCheck = true;
          j--;
          continue;
        }
        oo[j] = args[i];
      }

      Scriptable arguments = cx.newArray(scope, oo);
      ScriptableObject.putProperty(scope, "arguments", arguments);

      // Load up globals
      loadGlobals(cx, scope);

      // Suck in filename
      mainFile = args[0];
      Reader file;

      ScriptableObject.putProperty(scope, "__filename", mainFile);

      if (onlyCheck) {
        mainFile = args[1];
        InputStream input = this.getClass().getResourceAsStream(baseDir + "jslint/jslint.js");
        file = new InputStreamReader(input);
      } else {
        // In a JAR?
        InputStream input = this.getClass().getResourceAsStream(mainFile);
        if (input != null) {
          file = new InputStreamReader(input);
        } else {
          // Nope try file system
          file = new FileReader(mainFile);
        }
      }

      // Now evaluate the mainFile
      Object result = cx.evaluateReader(scope, file, mainFile, 0, null);

      Object[] argsArray = new Object[args.length];
      for (int i = 0; i < args.length; i++) {
          argsArray[i] =args[i];
      }

      if (true) { //meaning running a node.js like function that expects a 'process' paramter
          try {
            ScriptableObject.defineClass(scope, process.class);
            Scriptable process = cx.newObject(scope, "process", new Object[] { cx.newArray(scope, argsArray) });
            scope.put("process", scope, process); // put in global scope
            //and for some reason it needs to be passed into node.js
            ((Function)result).call(cx, scope, scope, new Object[] { process });
          } catch(Exception e) {
              System.err.println("Cannot load up process class: " + e.toString());
              System.exit(1);
          }
      }

      //System.err.println(Context.toString(result));

    } catch (FileNotFoundException e) {
      System.err.println("Cannot file file: " + mainFile);
      System.exit(1);
    } catch (IOException ie) {
      System.err.println("Error executing JS: " + ie.toString());
      System.exit(1);
    } finally {
      // Exit from the context.
      Context.exit();
    }
  }
}
