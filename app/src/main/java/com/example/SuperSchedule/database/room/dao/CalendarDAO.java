package com.example.SuperSchedule.database.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.SuperSchedule.entity.Calendar;

import java.util.List;

@Dao
public interface CalendarDAO {
    @Query("SELECT rowid,* FROM calendar")
    LiveData<List<Calendar>> getAll();
    @Query("SELECT rowid,* FROM calendar WHERE owner_user= :userUid")
    LiveData<List<Calendar>> getByUserId(String userUid);
    @Query("SELECT rowid,* FROM calendar WHERE rowid= :calendarUid")
    Calendar getByCalendarUid(int calendarUid);
    @Insert
    void insert(Calendar calendar);
    @Delete
    void delete(Calendar calendar);
    @Update
    void updateCustomer(Calendar calendar);
    @Query("DELETE FROM calendar")
    void deleteAll();
}