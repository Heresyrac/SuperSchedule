package com.example.SuperSchedule.repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.SuperSchedule.database.dao.CalendarDAO;
import com.example.SuperSchedule.database.dao.CustomerDAO;
import com.example.SuperSchedule.database.realtime.RealtimeDatabase;
import com.example.SuperSchedule.database.room.CustomerDatabase;
import com.example.SuperSchedule.database.room.MainDatabase;
import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.Customer;
import com.google.firebase.database.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CalendarRepository {
    private CalendarDAO calendarDAORemote;
    private CalendarDAO calendarDAOLocal;
    //private LiveData<List<Customer>> allCustomers;
    public CalendarRepository(Application application){
        RealtimeDatabase dbRemote = RealtimeDatabase.getInstance();
        MainDatabase dbLocal = MainDatabase.getInstance(application);
        calendarDAORemote =dbRemote.calendarDAO();
        calendarDAOLocal =dbLocal.calendarDao();
        //allCustomers= customerDao.getAll();
    }
    // Room executes this query on a separate thread
    public LiveData<List<Calendar>> getAllCalendar() {
        LiveData<List<Calendar>> liveData1=calendarDAOLocal.getAll();
        LiveData<List<Calendar>> liveData2=calendarDAORemote.getAll();
        return merge(liveData1,liveData2);
    }
    public LiveData<Calendar> getByCalendarUid(String calendarUid) {
        LiveData<Calendar> liveData1=calendarDAOLocal.getByCalendarUid(calendarUid);
        LiveData<Calendar> liveData2=calendarDAORemote.getByCalendarUid(calendarUid);
        return merge1(liveData1,liveData2);
    }
    public LiveData<List<Calendar>> getByUserUid(String userUid) {
        LiveData<List<Calendar>> liveData1=calendarDAOLocal.getByUserId(userUid);
        LiveData<List<Calendar>> liveData2=calendarDAORemote.getByUserId(userUid);
        return merge(liveData1,liveData2);
    }

    public void insert(final Calendar calendar){
        RealtimeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(calendar.isShared==Boolean.TRUE){
                    calendarDAORemote.insert(calendar);
                }
                else{
                    calendarDAOLocal.insert(calendar);
                }

            }
        });
    }
    public void deleteAll(){
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                calendarDAOLocal.deleteAll();
                calendarDAORemote.deleteAll();
            }
        });
    }
    public void delete(final Calendar calendar){
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(calendar.isShared==Boolean.TRUE){
                    calendarDAORemote.delete(calendar);
                }
                else{
                    calendarDAOLocal.delete(calendar);
                }

            }
        });
    }
    public void update(final Calendar calendar){
        CustomerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(calendar.isShared==Boolean.TRUE){
                    calendarDAORemote.update(calendar);
                }
                else{
                    calendarDAOLocal.update(calendar);
                }
            }
        });
    }
    private MediatorLiveData<List<Calendar>> merge( LiveData<List<Calendar>> liveData1,
                                                    LiveData<List<Calendar>> liveData2){
        MediatorLiveData<List<Calendar>> mergeLiveData=new MediatorLiveData<>();
        mergeLiveData.addSource(liveData1, new Observer<List<Calendar>>() {
            @Override
            public void onChanged(List<Calendar> l) {
                List<Calendar> tempData=liveData1.getValue();
                tempData.addAll(liveData2.getValue());
                mergeLiveData.setValue(tempData);
            }
        });
        mergeLiveData.addSource(liveData2, new Observer<List<Calendar>>() {
            @Override
            public void onChanged(List<Calendar> l) {
                List<Calendar> tempData=liveData1.getValue();
                tempData.addAll(liveData2.getValue());
                mergeLiveData.setValue(tempData);
            }
        });
        return mergeLiveData;
    }
    private MediatorLiveData<Calendar> merge1( LiveData<Calendar> liveData1,
                                                    LiveData<Calendar> liveData2){
        MediatorLiveData<Calendar> mergeLiveData=new MediatorLiveData<>();
        mergeLiveData.addSource(liveData1, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                if(calendar!=null){
                    mergeLiveData.setValue(calendar);
                }
            }
        });
        mergeLiveData.addSource(liveData2, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                if(calendar!=null){
                    mergeLiveData.setValue(calendar);
                }
            }
        });
        return mergeLiveData;
    }
}
