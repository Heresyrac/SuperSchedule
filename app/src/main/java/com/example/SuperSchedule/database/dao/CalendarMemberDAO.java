package com.example.SuperSchedule.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.example.SuperSchedule.entity.Calendar;

import java.util.List;

public interface CalendarMemberDAO {
    @Query("SELECT rowid,* FROM calendarmember")
    LiveData<List<Calendar>> getAll();
    @Query("SELECT rowid,* FROM calendarmember WHERE user_uid= :userUid")
    LiveData<List<Calendar>> getByUserId(String userUid);
    @Query("SELECT rowid,* FROM calendarmember WHERE calendar_uid= :calendarUid")
    LiveData<List<Calendar>> getByUserId(int calendarUid);
    @Query("SELECT rowid,* FROM calendarmember WHERE rowid= :calendarMemberUid")
    LiveData<Calendar> getByUid(int calendarMemberUid);
}
