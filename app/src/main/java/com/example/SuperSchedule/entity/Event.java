package com.example.SuperSchedule.entity;




import static java.lang.Integer.valueOf;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Event {
    @NonNull
    @ColumnInfo(name = "rowid")
    @PrimaryKey
    public String uid;
    @ColumnInfo(name = "event_name")
    @NonNull
    public String eventName;
    @NonNull
    public String time;// 2022-12-02-13-50(0-12)

    @ColumnInfo(name = "owner_calendar")
    public String ownerCalendar;

    @ColumnInfo(name = "is_shared")
    public Boolean isShared;

    @ColumnInfo(name = "enable_alarm")

    public Boolean enableAlarm;

    public String location;



    private int getTime(int part){
        String s=time.split("[^0-9]")[part];
        return valueOf(s);
    }
    public String getUid() { return uid; }
    @NonNull
    public String getTime() { return time; }
    public String getOwnerCalendar() { return ownerCalendar; }
    public String getLocation() { return location; }
    public Boolean getShared() { return isShared; }
    public Boolean getEnableAlarm() { return enableAlarm; }
    @NonNull
    public String getEventName() { return eventName; }

    /*
    public int getYear(){return getTime(0);}
    public int getMonth(){return getTime(1);}
    public int getDay(){return getTime(2);}
    public int getHour(){return getTime(3);}
    public int getMinute(){return getTime(4);}*/

    public void setUid(@NonNull String uid) { this.uid = uid; }

    private void setTime(int input, int part, int max){
        String str="0000"+ String.valueOf(input);
        int len=str.length();
        str=str.substring(len-max,len);
        if (time==null| time ==""){time="2000-01-01-01-01";}
        String[] s=time.split("[^0-9]",5);
        s[part]=str;
        time=s[0]+"-"+s[1]+"-"+s[2]+"-"+s[3]+"-"+s[4];
    }

    public void setEventName(@NonNull String eventName) { this.eventName = eventName; }
    public void setYear(int year){setTime(year,0,4);}
    public void setMonth(int month){setTime(month,1,2);}
    public void setDay(int day){setTime(day,2,2);}
    public void setHour(int hour){setTime(hour,3,2);}
    public void setMinute(int minute){setTime(minute,4,2);}
    public void setOwnerCalendarUid(String ownerCalendarUid){ this.ownerCalendar=ownerCalendarUid; }
    public void setIsShared(Boolean isShared) {this.isShared=isShared;}
    public void setOwnerCalendar( Calendar ownerCalendar) {
        this.ownerCalendar = ownerCalendar.uid;
        this.isShared=ownerCalendar.isShared;
    }


    public void setEnableAlarm(Boolean enableAlarm) {
        this.enableAlarm = enableAlarm;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Event(){};
    @Ignore
    public Event(@NonNull String eventName,
                 int year,
                 int month,
                 int day,
                 int hour,
                 int minute,
                 Calendar ownerCalendar,
                 boolean enableAlarm,
                 String location
                  ) {
        this.uid="Event-"+String.valueOf(System.currentTimeMillis());
        this.eventName=eventName;
        this.time="2000-01-01-00-00";
        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
        this.setOwnerCalendar(ownerCalendar);
        this.enableAlarm=enableAlarm;
        this.location=location;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("eventName", eventName);
        result.put("ownerCalendar", ownerCalendar);
        result.put("enableAlarm",enableAlarm);
        result.put("location", location);
        result.put("time", time);
        result.put("isShared",isShared);
        return result;
    }

}
