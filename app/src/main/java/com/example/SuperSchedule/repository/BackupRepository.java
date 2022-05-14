package com.example.SuperSchedule.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.SuperSchedule.database.dao.CalendarDAO;
import com.example.SuperSchedule.database.dao.CalendarMemberDAO;
import com.example.SuperSchedule.database.dao.RealtimeBackupDAO;
import com.example.SuperSchedule.database.dao.RoomBackupDAO;
import com.example.SuperSchedule.database.realtime.RealtimeDatabase;
import com.example.SuperSchedule.database.room.MainDatabase;

public class BackupRepository {
    String LOG_TAG="BackupRepository";
    private RealtimeBackupDAO realtimeBackupDAO;
    private RoomBackupDAO roomBackupDAO;
    RealtimeDatabase dbRemote;
    MainDatabase dbLocal;
    //private LiveData<List<Customer>> allCustomers;
    public BackupRepository(Context context){
        dbRemote = RealtimeDatabase.getInstance();
        dbLocal = MainDatabase.getInstance(context);
        realtimeBackupDAO=dbRemote.realtimeBackupDao();
        roomBackupDAO=dbLocal.roomBackupDao();
    }
    public void Backup(String userUid){
        Log.d(LOG_TAG, "BackupRepository.Backup is called for user:"+userUid );
        dbRemote.realtimeBackupDao().setAllEvents(userUid,dbLocal.roomBackupDao().getAllEvents());
        dbRemote.realtimeBackupDao().setAllCalendars(userUid,dbLocal.roomBackupDao().getAllCalendars());
        dbRemote.realtimeBackupDao().setAllCalendarMembers(userUid,dbLocal.roomBackupDao().getAllCalendarMembers());
    }

}
