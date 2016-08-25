package ayp.aug.alarmclock;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Date;

/**
 * Created by Nutdanai on 8/25/2016.
 */
public class ClockService extends IntentService {
     private static final String TAG = "ClockService";

    public ClockService() {
        super(TAG);
    }
    public static Intent newIntent(Context context){
        return new Intent(context,ClockService.class);
    }

    public static void setServiceAlarm(Context context,Clock mClock, boolean isOn){
        Intent i = ClockService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,0,i,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(isOn){
            Log.d(TAG, "setServiceAlarm : "+ mClock.getTime().getTime());
            am.set(AlarmManager.RTC_WAKEUP,mClock.getTime().getTime(),pi);
            Log.d(TAG, "Run by Alarm Manager");
        }else{
            am.cancel(pi);
            pi.cancel();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        builder.setTicker("Ticker!");
        builder.setContentTitle("Time Up !!");
        builder.setContentText("Hello!!");
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
        builder.setAutoCancel(false);
        builder.setSound(soundUri);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_INSISTENT;
        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(0,notification);
    }
}
