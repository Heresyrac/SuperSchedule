package com.example.SuperSchedule.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

@Entity(indices = {@Index(value = {"calendar_uid", "user_uid"},
        unique = true)})
public class CalendarMember {
    @ColumnInfo(name = "rowid")
    @PrimaryKey(autoGenerate = true)
    public String uid;

    @ColumnInfo(name = "calendar_uid")
    @NonNull
    public String calendarUid;
    @ColumnInfo(name = "user_uid")
    @NonNull
    public String userUid;
    @ColumnInfo(name = "user_auth_lv")
    public int userAuthLv;  //0->viewer
                            //1->editor
                            //2->owner


    public String getUid() {return uid; }
    @NonNull
    public String getCalendarUid() { return calendarUid; }
    public int getUserAuthLv() { return userAuthLv; }
    @NonNull
    public String getUserUid() { return userUid; }


    public CalendarMember(){};
    public CalendarMember( @NonNull String calendarUid,
                           @NonNull String userUid,
                           int userAuthLv
    ) {
        this.calendarUid=calendarUid;
        this.userUid=userUid;
        this.userAuthLv=userAuthLv;

    }
    public CalendarMember( Calendar calendar,
                           User user,
                           int userAuthLv
    ) {
        this.calendarUid=calendar.uid;
        this.userUid=user.uid;
        this.userAuthLv=userAuthLv;

    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("calendarUid", calendarUid);
        result.put("userUid", userUid);
        result.put("userAuthLv",userAuthLv);
        return result;
    }
}
