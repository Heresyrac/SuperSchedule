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
    @Query("SELECT rowid,* FROM event " +
            "ORDER BY time ASC")
    LiveData<List<Event>> getAll();

    @Query("SELECT rowid,* FROM event WHERE rowid= :eventUid " +
            "ORDER BY time ASC LIMIT 1")
    LiveData<Event> getByEventId(int eventUid);
    @Query("SELECT rowid,* FROM event WHERE owner_calendar= :calendarUid " +
            "ORDER BY time ASC")
    LiveData<List<Event>> getByCalendarId(int calendarUid);


    @Query("SELECT rowid,* FROM event WHERE rowid= :calendarUid AND " +
            "time BETWEEN :time1 AND :time2 "+
            "ORDER BY time ASC")
    LiveData<List<Event>> getBetweenDay(int calendarUid,String time1,String time2);
    //time->"2022-10-10-23-59"



    @Insert
    void insert(Calendar calendar);

    @Delete
    void delete(Calendar calendar);

    @Update
    void updateCustomer(Calendar calendar);

    @Query("DELETE FROM calendar")
    void deleteAll();
}
