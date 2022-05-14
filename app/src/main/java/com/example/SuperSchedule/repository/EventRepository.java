package com.example.SuperSchedule.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.SuperSchedule.database.dao.EventDAO;
import com.example.SuperSchedule.database.dao.EventDAO;
import com.example.SuperSchedule.database.realtime.RealtimeDatabase;
import com.example.SuperSchedule.database.room.CustomerDatabase;
import com.example.SuperSchedule.database.room.MainDatabase;
import com.example.SuperSchedule.entity.Event;

import java.util.List;

public class EventRepository {
    private EventDAO eventDAORemote;
    private EventDAO eventDAOLocal;
    RealtimeDatabase dbRemote;
    MainDatabase dbLocal;
    //private LiveData<List<Customer>> allCustomers;
    public EventRepository(Application application){
        dbRemote = RealtimeDatabase.getInstance();
        dbLocal = MainDatabase.getInstance(application);
        eventDAORemote =dbRemote.eventDao();
        eventDAOLocal =dbLocal.eventDao();
        //allCustomers= customerDao.getAll();
    }
    // Room executes this query on a separate thread

    public LiveData<Event> getByEventUid(String calendarUid, String eventUid) {
        LiveData<Event> liveData1=eventDAOLocal.getByEventUid(calendarUid,eventUid);
        LiveData<Event> liveData2=eventDAORemote.getByEventUid(calendarUid,eventUid);
        return merge1(liveData1,liveData2);
    }
    public LiveData<List<Event>> getByCalendarUid(String calendarUid) {
        LiveData<List<Event>> liveData1=eventDAOLocal.getByCalendarUid(calendarUid);
        LiveData<List<Event>> liveData2=eventDAORemote.getByCalendarUid(calendarUid);
        return merge(liveData1,liveData2);
    }
    public LiveData<List<Event>> getBetweenTime(String calendarUid,
            /*time->YYYY-MM-DD-hh-mm*/String time1,String time2) {
        LiveData<List<Event>> liveData1=eventDAOLocal.getBetweenTime(calendarUid,time1,time2);
        LiveData<List<Event>> liveData2=eventDAORemote.getBetweenTime(calendarUid,time1,time2);
        return merge(liveData1,liveData2);
    }
    public LiveData<List<Event>> getBetweenDay(String calendarUid,String time1,String time2) {
        /*time->YYYY-MM-DD*/
        LiveData<List<Event>> liveData1=eventDAOLocal.getBetweenTime(calendarUid,
                time1+"-00-00",time2+"-24-00");
        LiveData<List<Event>> liveData2=eventDAORemote.getBetweenTime(calendarUid,
                time1+"-00-00",time2+"-24-00");
        return merge(liveData1,liveData2);
    }

    public void insert(final Event event){
        RealtimeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(event.isShared==Boolean.TRUE){
                    eventDAORemote.insert(event);
                }
                else{
                    eventDAOLocal.insert(event);
                }
            }
        });
    }

    public void delete(final Event event){
        RealtimeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(event
                        .isShared==Boolean.TRUE){
                    eventDAORemote.delete(event);
                }
                else{
                    eventDAOLocal.delete(event);
                }
            }
        });
    }
    public void update(final Event event){
        RealtimeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(event.isShared==Boolean.TRUE){
                    eventDAORemote.update(event);
                }
                else{
                    eventDAOLocal.update(event);
                }
            }
        });
    }
    private MediatorLiveData<List<Event>> merge(LiveData<List<Event>> liveData1,
                                                   LiveData<List<Event>> liveData2){
        MediatorLiveData<List<Event>> mergeLiveData=new MediatorLiveData<>();
        mergeLiveData.addSource(liveData1, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> l) {
                List<Event> tempData=liveData1.getValue();
                tempData.addAll(liveData2.getValue());
                mergeLiveData.setValue(tempData);
            }
        });
        mergeLiveData.addSource(liveData2, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> l) {
                List<Event> tempData=liveData1.getValue();
                tempData.addAll(liveData2.getValue());
                mergeLiveData.setValue(tempData);
            }
        });
        return mergeLiveData;
    }
    private MediatorLiveData<Event> merge1( LiveData<Event> liveData1,
                                               LiveData<Event> liveData2){
        MediatorLiveData<Event> mergeLiveData=new MediatorLiveData<>();
        mergeLiveData.addSource(liveData1, new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                if(event!=null){
                    mergeLiveData.setValue(event);
                }
            }
        });
        mergeLiveData.addSource(liveData2, new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                if(event!=null){
                    mergeLiveData.setValue(event);
                }
            }
        });
        return mergeLiveData;
    }
}
