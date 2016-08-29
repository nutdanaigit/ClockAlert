package ayp.aug.alarmclock;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Nutdanai on 8/25/2016.
 */
public class ClockService extends IntentService {
     private static final String TAG = "ClockService";
     private static Clock clock;
     private NotificationManagerCompat nmc;
     public ClockService() {
        super(TAG);
    }


    public static Intent newIntent(Context context){
        return new Intent(context,ClockService.class);
    }

    public static void setServiceAlarm(Context context,Clock mClock, boolean isOn){
        clock = mClock;
        Intent i = ClockService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,0,i,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(isOn){
//            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                Log.d(TAG, "setServiceAlarm : " + mClock.getTime().getTime());
                am.set(AlarmManager.RTC_WAKEUP, mClock.getTime().getTime(), pi);
                Log.d(TAG, "Run by Alarm Manager");
//            }else{
//                Log.d(TAG,"Run API" + Build.VERSION.SDK_INT);
//                Log.d(TAG,"> " + Build.VERSION_CODES.LOLLIPOP);
//                Log.d(TAG,"Run by Scheduler");
//            }

        }else {
//            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            am.cancel(pi);
            pi.cancel();
            NotificationManagerCompat.from(context).cancelAll();

//            }else{
//                Log.d(TAG,"Stop ! ! !");
//            }
        }
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        //
        ClockLab clockLab = ClockLab.getInstance(this);
        PendingIntent pi = PendingIntent.getService(this,0,intent,0);
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        builder.setTicker("Ticker!");
        builder.setContentTitle("Time Up !! : " + clock.getTitle());
        builder.setContentText("Set Time = " + clockLab.setFormatTime(clock.getTime()));
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
        builder.setAutoCancel(true);
//        Ringtone ringtone = RingtoneManager.getRingtone(this,soundUri);
//        ringtone.play();
        builder.setSound(soundUri);
        builder.setContentIntent(pi);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        Notification notification = builder.build();
//        notification.flags |= Notification.FLAG_INSISTENT;

        nmc = NotificationManagerCompat.from(this);
        nmc.notify(0,notification);

        new ClockScreen().on(ClockService.this);
    }
}
