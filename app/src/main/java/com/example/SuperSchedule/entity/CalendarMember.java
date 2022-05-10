package com.example.SuperSchedule.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;
@Fts4
@Entity
public class CalendarMember {
    @ColumnInfo(name = "rowid")
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "calendar_uid")
    @NonNull
    public int calendarUid;
    @ColumnInfo(name = "user_uid")
    @NonNull
    public String userUid;
    @ColumnInfo(name = "user_auth_lv")
    public int userAuthLv;  //0->viewer
                            //1->editor
                            //2->owner




    public CalendarMember( @NonNull int calendarUid,
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
}
