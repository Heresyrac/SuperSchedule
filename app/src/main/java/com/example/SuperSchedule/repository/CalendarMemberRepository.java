package com.example.SuperSchedule.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.SuperSchedule.database.dao.CalendarMemberDAO;
import com.example.SuperSchedule.database.realtime.RealtimeDatabase;

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
    public CalendarMemberRepository(Context context){
        dbRemote = dbRemote.getInstance();
        dbLocal = MainDatabase.getInstance(context);

        calendarMemberDAORemote=dbRemote.calendarMemberDao();
        calendarMemberDAORemote=dbLocal.calendarMemberDao();
    }

    public LiveData<List<CalendarMember>> getByCalendarUid(String calendarUid) {
        LiveData< List<CalendarMember>> liveData1=calendarMemberDAOLocal.getByCalendarUid(calendarUid);
        LiveData< List<CalendarMember>> liveData2=calendarMemberDAORemote.getByCalendarUid(calendarUid);
        return merge(liveData1,liveData2);
    }
    public LiveData<List< CalendarMember>> getByUserUid(String userUid) {
        LiveData<List< CalendarMember>> liveData1=calendarMemberDAOLocal.getByUserUid(userUid);
        LiveData<List< CalendarMember>> liveData2=calendarMemberDAORemote.getByUserUid(userUid);
        return merge(liveData1,liveData2);
    }

    public void insert(final  CalendarMember calendarMember){
        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(calendarMember.userAuthLv!=3){
                    calendarMemberDAORemote.insert(calendarMember);
                }
                else{
                    calendarMemberDAOLocal.insert(calendarMember);
                }

            }
        });
    }
    public void deleteAll(){
        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                calendarMemberDAOLocal.deleteAll();
                calendarMemberDAORemote.deleteAll();
            }
        });
    }
    public void delete(final CalendarMember calendarMember){
        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(calendarMember.userAuthLv!=3){
                    calendarMemberDAORemote.delete(calendarMember);
                    /*
                    Workmanager->delete useless membership


                    */
                }
                else{
                    calendarMemberDAOLocal.delete(calendarMember);
                }

            }
        });
    }
    public void update(final  CalendarMember calendarMember){
        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(calendarMember.userAuthLv!=3){
                    calendarMemberDAORemote.update(calendarMember);
                }
                else{
                    calendarMemberDAOLocal.update(calendarMember);
                }
            }
        });
    }
    private MediatorLiveData<List<CalendarMember>> merge( LiveData<List<CalendarMember>> liveData1,
                                                    LiveData<List<CalendarMember>> liveData2){
        MediatorLiveData<List<CalendarMember>> mergeLiveData=new MediatorLiveData<>();
        mergeLiveData.addSource(liveData1, new Observer<List<CalendarMember>>() {
            @Override
            public void onChanged(List<CalendarMember> l) {
                List<CalendarMember> tempData=liveData1.getValue();
                tempData.addAll(liveData2.getValue());
                mergeLiveData.setValue(tempData);
            }
        });
        mergeLiveData.addSource(liveData2, new Observer<List<CalendarMember>>() {
            @Override
            public void onChanged(List<CalendarMember> l) {
                List<CalendarMember> tempData=liveData1.getValue();
                tempData.addAll(liveData2.getValue());
                mergeLiveData.setValue(tempData);
            }
        });
        return mergeLiveData;
    }
    private MediatorLiveData<CalendarMember> merge1( LiveData<CalendarMember> liveData1,
                                               LiveData<CalendarMember> liveData2){
        MediatorLiveData<CalendarMember> mergeLiveData=new MediatorLiveData<>();
        mergeLiveData.addSource(liveData1, new Observer<CalendarMember>() {
            @Override
            public void onChanged(CalendarMember calendarMember) {
                if(calendarMember!=null){
                    mergeLiveData.setValue(calendarMember);
                }
            }
        });
        mergeLiveData.addSource(liveData2, new Observer<CalendarMember>() {
            @Override
            public void onChanged(CalendarMember calendarMember) {
                if(calendarMember!=null){
                    mergeLiveData.setValue(calendarMember);
                }
            }
        });
        return mergeLiveData;
    }

}
