package com.example.SuperSchedule.entity;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;
@Entity
public class Calendar {
    @PrimaryKey(autoGenerate = true)
    public String uid;
    @ColumnInfo(name = "calendar_name")
    @NonNull
    public String calendarName;
    @ColumnInfo(name = "owner_user")
    public String ownerUser;
    @ColumnInfo(name = "is_shared")
    public Boolean isShared;


    public Calendar( @NonNull String calendarName,
                     String ownerUser,
                     Boolean isShared
    ) {
        this.calendarName=calendarName;
        this.ownerUser=ownerUser;
        this.isShared=isShared;

    }
}
