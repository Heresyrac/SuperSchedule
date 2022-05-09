package com.example.SuperSchedule.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;
@Fts4
@Entity
public class Event {
    @NonNull
    @ColumnInfo(name = "rowid")
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "event_name")
    @NonNull
    public String eventName;
    @NonNull
    public int year;
    @NonNull
    public int month;
    @NonNull
    public int day;
    @NonNull
    public int hour;
    @NonNull
    public int minute;

    @ColumnInfo(name = "owner_calendar")
    public int ownerCalendar;
    @ColumnInfo(name = "enable_alarm")

    public Boolean enableAlarm;

    public String location;
    public Event( @NonNull String eventName,
                  @NonNull int year,
                  @NonNull int month,
                  @NonNull int day,
                  @NonNull int hour,
                  @NonNull int minute,
                  int ownerCalendar,
                  boolean enableAlarm,
                  String location
                  ) {
        this.eventName=eventName;
        this.year=year;
        this.month=month;
        this.day=day;
        this.hour=hour;
        this.minute=minute;
        this.ownerCalendar=ownerCalendar;
        this.enableAlarm=enableAlarm;
        this.location=location;
    }
}
