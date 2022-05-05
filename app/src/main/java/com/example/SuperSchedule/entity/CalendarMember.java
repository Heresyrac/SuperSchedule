package com.example.SuperSchedule.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;
@Fts4
@Entity(primaryKeys = {"calendarUid", "userUid"})
public class CalendarMember {
    public int uid;
    @ColumnInfo(name = "calendar_uid")
    @NonNull
    public int calendarUid;
    @ColumnInfo(name = "user_uid")
    @NonNull
    public int userUid;
    @ColumnInfo(name = "user_uid")
    public int userAuthLv;  //0->viewer
                            //1->editor
                            //2->owner




    public CalendarMember( @NonNull int calendarUid,
                           @NonNull int userUid,
                           int userAuthLv
    ) {
        this.calendarUid=calendarUid;
        this.userUid=userUid;
        this.userAuthLv=userAuthLv;

    }
}
