package com.example.SuperSchedule.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.SuperSchedule.database.dao.CalendarMemberDAO;
import com.example.SuperSchedule.database.realtime.RealtimeDatabase;
import com.example.SuperSchedule.database.room.CustomerDatabase;
import com.example.SuperSchedule.database.room.MainDatabase;
import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.CalendarMember;

import java.util.List;

public class CalendarMemberRepository {
    RealtimeDatabase dbRemote;
    MainDatabase dbLocal;
    private CalendarMemberDAO calendarMemberDAORemote;
    private CalendarMemberDAO calendarMemberDAOLocal;
    //private LiveData<List<Customer>> allCustomers;
    public CalendarMemberRepository(Application application){
        dbRemote = dbRemote.getInstance();
        dbLocal = MainDatabase.getInstance(application);

        calendarMemberDAORemote=dbRemote.calendarMemberDAO();
        //calendarMemberDAORemote=dbLocal.calendarMemberDAO();
    }
    public LiveData<List<CalendarMember>> getByCalendarUid(String calendarUid) {
        return calendarMemberDAORemote.getByCalendarId(calendarUid);
    }
    public LiveData<List<CalendarMember>> getByUserUid(String userUid) {
        return calendarMemberDAORemote.getByUserId(userUid);
    }
    public LiveData<CalendarMember> getByUserCalendar(String userUid,String calendarUid) {
        return calendarMemberDAORemote.getByUserCalendar(userUid,calendarUid);
    }

    public void insert(final CalendarMember calendarMember){
        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                calendarMemberDAORemote.insert(calendarMember);
            }
        });
    }
    public void deleteAll(){
        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                calendarMemberDAORemote.deleteAll();
            }
        });
    }
    public void delete(final CalendarMember calendarMember){
        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                calendarMemberDAORemote.delete(calendarMember);
            }
        });
    }
    public void update(final CalendarMember calendarMember){
        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                calendarMemberDAORemote.update(calendarMember);
            }
        });
    }
}
