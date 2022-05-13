package com.example.SuperSchedule.database.realtime;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.SuperSchedule.database.dao.EventDAO;
import com.example.SuperSchedule.entity.Event;
import com.google.firebase.database.DatabaseReference;
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
        return new FirebaseQueryLiveData<>(accessQuery);
    };
    public LiveData<List<Event>> getByCalendarUid(String calendarUid){
        Query accessQuery=eventRef.child("calendarUid").orderByChild("time");
        return new FirebaseQueryLiveData<>(accessQuery);
    };
    public LiveData<List<Event>> getBetweenTime(String calendarUid,String time1,String time2){
        Query accessQuery=eventRef.child("calendarUid")
                .orderByChild("time")
                .startAt(time1)
                .endAt(time2);
        return new FirebaseQueryLiveData<>(accessQuery);
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
        eventRef.child(owner).child(key).setValue(event).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success insert data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error insert data", e);
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

        eventRef.child(owner).child(key).setValue(null).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success insert data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error insert data", e);
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
        eventRef.child(owner).child(key).setValue(event).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success insert data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error insert data", e);
                });
    }


}
