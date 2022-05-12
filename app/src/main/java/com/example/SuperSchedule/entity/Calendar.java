package com.example.SuperSchedule.entity;



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
public class Calendar {
    @NonNull
    @PrimaryKey
    public String uid;
    @ColumnInfo(name = "calendar_name")
    public String calendarName;
    @ColumnInfo(name = "owner_user")
    public String ownerUser;
    @ColumnInfo(name = "is_shared")
    public Boolean isShared;

    public String getCalendarName() { return calendarName; }
    public Boolean getShared() { return isShared; }
    public String getOwnerUser() {  return ownerUser; }
    public String getUid() { return uid; }

    public Calendar(){};
    @Ignore
    public Calendar(@NonNull String calendarName,
                    String ownerUser,
                    Boolean isShared
    ) {
        this.uid="Calendar-"+String.valueOf(System.currentTimeMillis());
        this.calendarName=calendarName;
        this.ownerUser=ownerUser;
        this.isShared=isShared;

    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("calendarName", calendarName);
        result.put("ownerUser", ownerUser);
        result.put("isShared",isShared);
        return result;
    }
}
