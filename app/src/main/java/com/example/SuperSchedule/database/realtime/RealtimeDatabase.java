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
    EventDAO eventDAO;
    CalendarDAO calendarDAO;
    CalendarMemberDAO calendarMemberDAO;
    UserDAO userDAO;
    private static RealtimeDatabase uniqueInstance = null;
    //private Object holdedShareContent = null;
    private RealtimeDatabase() {
        /*eventDAO=new EventAccessor(rootRef);
        calendarDAO=new CalendarAccessor(rootRef);
        calendarMemberDAO=new CalendarMemberAccessor(rootRef);
        userDAO=new UserAccessor(rootRef);*/
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
    public EventDAO eventDAO(){
        return eventDAO;
    }
    public CalendarDAO calendarDAO(){
        return calendarDAO;
    }
    public CalendarMemberDAO calendarMemberDAO(){
        return calendarMemberDAO;
    }
    public UserDAO userDAO(){
        return userDAO;
    }
}
