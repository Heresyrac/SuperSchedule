package com.example.SuperSchedule.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.CalendarMember;
import com.example.SuperSchedule.entity.Event;

import java.util.List;


@Dao
public interface RoomBackupDAO {

    @Query("SELECT * FROM calendar")
    List<Calendar> getAllCalendars();

    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

    @Query("SELECT * FROM calendarmember")
    List<CalendarMember> getAllCalendarMembers();
}

