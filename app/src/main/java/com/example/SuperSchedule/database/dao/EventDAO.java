package com.example.SuperSchedule.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.Event;

import java.util.List;

@Dao
public interface EventDAO {

    @Query("SELECT * FROM event WHERE rowid= :eventUid " +
            "AND owner_calendar= :calendarUid" +
            " ORDER BY time ASC LIMIT 1")
    LiveData<Event> getByEventId(String calendarUid,String eventUid);

    @Query("SELECT * FROM event WHERE owner_calendar= :calendarUid " +
            "ORDER BY time ASC")
    LiveData<List<Event>> getByCalendarId(String calendarUid);


    @Query("SELECT * FROM event WHERE rowid= :calendarUid AND " +
            "time BETWEEN :time1 AND :time2 "+
            "ORDER BY time ASC")
    LiveData<List<Event>> getBetweenTime(String calendarUid,String time1,String time2);
    //time->"2022-10-10-23-59"



    @Insert
    void insert(Event event);

    @Delete
    void delete(Event event);

    @Update
    void update(Event event);

}
