package com.example.SuperSchedule.database.realtime;

import android.content.Context;

import androidx.room.Room;

import com.example.SuperSchedule.database.dao.CalendarDAO;
import com.example.SuperSchedule.database.dao.CalendarMemberDAO;
import com.example.SuperSchedule.database.dao.CustomerDAO;
import com.example.SuperSchedule.database.dao.EventDAO;
import com.example.SuperSchedule.database.dao.UserDAO;
import com.example.SuperSchedule.database.room.CustomerDatabase;
import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.CalendarMember;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RealtimeDatabase {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = database.getReference();
    EventDAO eventDao;
    CalendarDAO calendarDao;
    CalendarMemberDAO calendarMemberDao;
    UserDAO userDao;
    private static RealtimeDatabase uniqueInstance = null;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //private Object holdedShareContent = null;
    private RealtimeDatabase() {
        database.setPersistenceEnabled(true);
        database.getReference("scores").keepSynced(true);
        eventDao=new EventAccessor(rootRef);
        calendarDao=new CalendarAccessor(rootRef);
        calendarMemberDao=new CalendarMemberAccessor(rootRef);
        userDao=new UserAccessor(rootRef);
    }

    public static RealtimeDatabase getInstance() {
        Object obj = new Object();
        synchronized (obj) {
            if (uniqueInstance == null) {
                synchronized (obj) {
                    uniqueInstance = new RealtimeDatabase();
                }
            }
        }
        return uniqueInstance;
    }
    public EventDAO eventDao(){ return eventDao; }
    public CalendarDAO calendarDao(){ return calendarDao; }
    public CalendarMemberDAO calendarMemberDao(){
        return calendarMemberDao;
    }
    public UserDAO userDao(){
        return userDao;
    }
}
