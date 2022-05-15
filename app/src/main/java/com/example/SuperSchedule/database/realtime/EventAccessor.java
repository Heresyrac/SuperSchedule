package com.example.SuperSchedule.database.realtime;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.SuperSchedule.database.dao.EventDAO;
import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAccessor implements EventDAO {
    private static final String LOG_TAG = "RealtimeEvent";
    DatabaseReference rootRef;
    DatabaseReference eventRef;
    public EventAccessor(DatabaseReference rootRef){
        this.rootRef=rootRef;
        this.eventRef =rootRef.child("events");
    }

    public LiveData<Event> getByEventUid(String calendarUid,String eventUid){
        Query accessQuery=eventRef.child(calendarUid).child(eventUid);
        return Transformations.switchMap(new FirebaseQueryLiveData(accessQuery), (data) ->{
            MutableLiveData<Event> r=new MutableLiveData<>();
            r.setValue(data.getValue(Event.class));
            return r;
        });
    };
    public LiveData<List<Event>> getByCalendarUid(String calendarUid){
        Query accessQuery=eventRef.child("calendarUid").orderByChild("time");
        return Transformations.switchMap(new FirebaseQueryLiveData(accessQuery), (data) ->{
            GenericTypeIndicator<List<Event>> t = new GenericTypeIndicator<List<Event>>() {};
            MutableLiveData<List<Event>> r=new MutableLiveData<>();
            r.setValue(data.getValue(t));
            return r;
        });
    };
    public LiveData<List<Event>> getBetweenTime(String calendarUid,String time1,String time2){
        Query accessQuery=eventRef.child("calendarUid")
                .orderByChild("time")
                .startAt(time1)
                .endAt(time2);
        return Transformations.switchMap(new FirebaseQueryLiveData(accessQuery), (data) ->{
            GenericTypeIndicator<List<Event>> t = new GenericTypeIndicator<List<Event>>() {};
            MutableLiveData<List<Event>> r=new MutableLiveData<>();
            r.setValue(data.getValue(t));
            return r;
        });
    };
    //time->"2022-10-10-23-59"
    public void insert(Event event) {
        String owner=event.ownerCalendar;
        if(owner==null){
            Log.e(LOG_TAG,"Can't delete event without owner Calendar");
            return;
        }
        String key =eventRef.push().getKey();
        event.uid=key;
        eventRef.child(owner).child(key).setValue(event).addOnCompleteListener(new OnCompleteListener< Void >() {
            @Override
            public void onComplete(@NonNull Task< Void > task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "****Success insert data");
                } else {
                    Log.e(LOG_TAG, "****Error insert data", task.getException());
                }
            }
        });
    }

    public void delete(Event event){
        String owner=event.ownerCalendar;
        String key=event.uid;
        if(owner==null){
            Log.e(LOG_TAG,"Can't delete event without owner Calendar");
            return;
        }
        if(key==null){
            Log.e(LOG_TAG,"Can't delete event without uid");
            return;
        }

        eventRef.child(owner).child(key).setValue(null).addOnCompleteListener(new OnCompleteListener< Void >() {
            @Override
            public void onComplete(@NonNull Task< Void > task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "****Success insert data");
                } else {
                    Log.e(LOG_TAG, "****Error insert data", task.getException());
                }
            }
        });
    }

    public void update(Event event){
        String owner=event.ownerCalendar;
        String key=event.uid;
        if(owner==null){
            Log.e(LOG_TAG,"Can't update event without owner Calendar");
            return;
        }
        if(key==null){
            Log.e(LOG_TAG,"Can't update event without uid");
            return;
        }
        eventRef.child(owner).child(key).setValue(event).addOnCompleteListener(new OnCompleteListener< Void >() {
            @Override
            public void onComplete(@NonNull Task< Void > task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "****Success update data");
                } else {
                    Log.e(LOG_TAG, "****Error update data", task.getException());
                }
            }
        });
    }


}
