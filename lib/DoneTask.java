import org.mozilla.javascript.*;
import java.util.TimerTask;

    class DoneTask extends TimerTask {
        Context cx;
        Scriptable thisObj;

        public DoneTask(Context cx, Scriptable thisObj) {
            this.cx = cx;
            this.thisObj = thisObj;
        }

        public void run() {
            // Call 'ontimeout' on this...
            ((ScriptableObject)thisObj).callMethod(this.thisObj, "ontimeout", null);
            //timer.cancel(); //Terminate the timer thread
        }
    }


