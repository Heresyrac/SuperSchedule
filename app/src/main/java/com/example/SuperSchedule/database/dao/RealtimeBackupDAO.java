package com.example.SuperSchedule.database.dao;

import androidx.room.Query;

import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.CalendarMember;
import com.example.SuperSchedule.entity.Event;

import java.util.List;

public interface RealtimeBackupDAO {
    void setAllCalendars(String userUid,List<Calendar> calendars);

    void setAllEvents(String userUid,List<Event> events);

    void setAllCalendarMembers(String userUid,List<CalendarMember> calendarMembers);
}
