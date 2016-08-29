package ayp.aug.alarmclock;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

/**
 * Created by Nutdanai on 8/26/2016.
 */
public class ClockScreen {

    private static final String  SCREEN_CLASS_TAG = "SCREEN";

    public ClockScreen(){}

    public void on(Context context){
        PowerManager powerManager =(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if(!powerManager.isScreenOn()) {
            PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, SCREEN_CLASS_TAG);
            wl.acquire();

//            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//            final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
//            kl.disableKeyguard();


        }
    }
}
