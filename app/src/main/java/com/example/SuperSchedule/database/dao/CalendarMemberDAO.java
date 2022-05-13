package com.example.SuperSchedule.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.SuperSchedule.entity.CalendarMember;
import com.example.SuperSchedule.entity.Customer;

import java.util.Calendar;
import java.util.List;
@Dao
public interface CalendarMemberDAO {
    @Query("SELECT * FROM calendarmember WHERE user_uid= :userUid")
    LiveData<List<CalendarMember>> getByUserUid(String userUid);
    @Query("SELECT * FROM calendarmember WHERE calendar_uid= :calendarUid")
    LiveData<List<CalendarMember>> getByCalendarUid(String calendarUid);
    @Query("SELECT * FROM calendarmember WHERE " +
            "user_uid= :userUid AND calendar_uid= :calendarUid LIMIT 1")
    LiveData<CalendarMember> getByUserCalendar( String userUid,String calendarUid);
    @Insert
    void insert(CalendarMember calendarMember);
    @Delete
    void delete(CalendarMember calendarMember);
    @Update
    void update(CalendarMember calendarMember);
    @Query("DELETE FROM calendarmember")
    void deleteAll();
}
