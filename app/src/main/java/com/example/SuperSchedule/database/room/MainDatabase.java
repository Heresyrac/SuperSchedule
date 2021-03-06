package com.example.SuperSchedule.database.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.SuperSchedule.database.dao.CalendarDAO;
import com.example.SuperSchedule.database.dao.CalendarMemberDAO;
import com.example.SuperSchedule.database.dao.EventDAO;
import com.example.SuperSchedule.database.dao.RoomBackupDAO;
import com.example.SuperSchedule.database.dao.UserDAO;
import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.CalendarMember;
import com.example.SuperSchedule.entity.Event;
import com.example.SuperSchedule.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Calendar.class,
        CalendarMember.class,
        Event.class,
        User.class
        }, version = 3, exportSchema = false)
public abstract class MainDatabase extends RoomDatabase {
    public abstract CalendarDAO calendarDao();
    public abstract EventDAO eventDao();
    public abstract UserDAO userDao();
    public abstract CalendarMemberDAO calendarMemberDao();
    public abstract RoomBackupDAO roomBackupDao();
    private static MainDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static synchronized MainDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MainDatabase.class, "MainDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
