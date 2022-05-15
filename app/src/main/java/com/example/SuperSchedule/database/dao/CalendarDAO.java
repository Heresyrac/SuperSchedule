package com.example.SuperSchedule.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.SuperSchedule.entity.Calendar;

import java.util.List;

@Dao
public interface CalendarDAO {
    @Query("SELECT * FROM calendar")
    LiveData<List<Calendar>> getAll();
    @Query("SELECT * FROM calendar WHERE owner_user= :userUid")
    LiveData<List<Calendar>> getByUserId(String userUid);
    @Query("SELECT * FROM calendar WHERE rowid= :calendarUid LIMIT 1")
    LiveData<Calendar> getByCalendarUid(String calendarUid);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Calendar calendar);
    @Delete
    void delete(Calendar calendar);
    @Update
    void update(Calendar calendar);
    @Query("DELETE FROM calendar")
    void deleteAll();
}