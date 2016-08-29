package ayp.aug.alarmclock;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Nutdanai on 8/24/2016.
 */
public class Clock {
    public UUID uuid;
    public String title;
    public Date time;
    public boolean check;
    public static Clock instanceClock;

    public static Clock newInstanceClock(){
        if(instanceClock == null){
            instanceClock = new Clock();
        }
        return instanceClock;
    }

    public Clock(){
        this(UUID.randomUUID());
    }
    public Clock(UUID uuid){
        this.uuid = uuid;
        time = new Date();
    }

    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public void setTitle(String title) { this.title = title; }
    public void setTime(Date time) { this.time = time;}
    public void setCheck(boolean check) {this.check = check;}

    public UUID getUuid() { return uuid; }
    public String getTitle() { return title; }
    public Date getTime() {return time; }
    public boolean isCheck() {return check;}
}
