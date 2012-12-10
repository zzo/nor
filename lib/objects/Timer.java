import org.mozilla.javascript.*;

import java.util.TimerTask;

public class Timer extends ScriptableObject {
    private static final long serialVersionUID = 34297528888642L;
    private java.util.Timer timer;
 
     class DoneTask extends TimerTask {
        Scriptable thisObj;

        public DoneTask(Scriptable thisObj) {
            this.thisObj = thisObj;
        }

        public void run() {
            // Call 'ontimeout' on this...
            ((ScriptableObject)thisObj).callMethod(this.thisObj, "ontimeout", null);
            //timer.cancel(); //Terminate the timer thread
        }
    }

   public Timer() {}

    public void jsConstructor() {
        timer = new java.util.Timer();
    }

    public void jsFunction_close() {
        timer.cancel();
    }

    public void start(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        int timeout = ((Double)args[0]).intValue();
        boolean repeat = ((Boolean)args[1]).booleanValue();

        TimerTask timerTask = new DoneTask(thisObj);

        if (repeat) {
            timer.schedule(timerTask, timeout, timeout);
        } else {
            timer.schedule(timerTask, timeout);
        }
    }

    public void jsFunction_stop() {
        timer.cancel();
    }

    @Override
    public String getClassName() { return "Timer"; }
}
