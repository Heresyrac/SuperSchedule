package com.example.SuperSchedule.database.room.dao;

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
            "ORDER BY year,month,day,hour,minute ASC")
    LiveData<List<Event>> getAll();

    @Query("SELECT rowid,* FROM event WHERE rowid= :calendarUid " +
            "ORDER BY year,month,day,hour,minute ASC")
    LiveData<List<Event>> getByCalendarId(int calendarUid);

    @Query("SELECT rowid,* FROM event WHERE rowid= :calendarUid " +
            "AND year= :year " +
            "ORDER BY year,month,day,hour,minute ASC")
    LiveData<List<Event>> getByYear(int calendarUid,int year);

    @Query("SELECT rowid,* FROM event WHERE rowid= :calendarUid " +
            "AND year= :year AND month= :month " +
            "ORDER BY year,month,day,hour,minute ASC")
    LiveData<List<Event>> getByMonth(int calendarUid,int year,int month);

    @Query("SELECT rowid,* FROM event WHERE rowid= :calendarUid " +
            "AND year= :year AND month= :month AND day= :day " +
            "ORDER BY year,month,day,hour,minute ASC")
    LiveData<List<Event>> getByDay(int calendarUid,int year,int month, int day);

    @Query("SELECT rowid,* FROM event WHERE rowid= :calendarUid AND " +
            "year<<20+month<<10+day BETWEEN " +
            ":year1<<20+:month1<<10+:day1 AND :year2<<20+:month2<<10+:day2 " +
            "ORDER BY year,month,day,hour,minute ASC")
    LiveData<List<Event>> getBetweenDay(int calendarUid,int year1,int month1, int day1,
                                        int year2, int month2,int day2);



    @Insert
    void insert(Calendar calendar);

    @Delete
    void delete(Calendar calendar);

    @Update
    void updateCustomer(Calendar calendar);

    @Query("DELETE FROM calendar")
    void deleteAll();
}
