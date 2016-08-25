package ayp.aug.alarmclock;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Nutdanai on 8/24/2016.
 */
public class ClockLab {
    private static ClockLab instance;
    List<Clock> mClockList;
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







}
