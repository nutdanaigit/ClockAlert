package ayp.aug.alarmclock;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Nutdanai on 8/24/2016.
 */
public class ClockLab {
    private static ClockLab instance;
    private static final String TAG = "ClockLab";
    List<Clock> mClockList;
    private int m ;

    public static ClockLab getInstance(Context context){
        if(instance == null){
            instance =new ClockLab();
        }
        return instance;
    }

    public ClockLab (){
        mClockList = new ArrayList<>();
    }

    public Clock getClockById(UUID uuid){
        for(Clock clock : mClockList){
            if(clock.getUuid().equals(uuid)) return clock;
        }
        return null;
    }

    public List<Clock> getClock(){
        return this.mClockList;
    }

    public void addList(Clock clock){
        mClockList.add(clock);
    }

    public void delete(int position){mClockList.remove(position);}


    public String setFormatTime(Date date) {
        Log.d(TAG, "setFormatTime: " + date);
        SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a", Locale.US);
        Log.d(TAG, "setFormatTime :  " + formatDate.format(date));
        return formatDate.format(date);
    }

    public int getIdNotification(){
        Random r = new Random();
        m=r.nextInt();
        return  m;
    }



}
