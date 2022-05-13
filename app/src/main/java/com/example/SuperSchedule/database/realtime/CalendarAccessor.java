package com.example.SuperSchedule.database.realtime;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.SuperSchedule.database.dao.CalendarDAO;
import com.example.SuperSchedule.entity.Calendar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarAccessor  implements CalendarDAO {
    private static final String LOG_TAG = "RealtimeCalendar";
    DatabaseReference rootRef;
    DatabaseReference calendarRef;
    public CalendarAccessor(DatabaseReference rootRef){
        this.rootRef=rootRef;
        this.calendarRef=rootRef.child("calendars");
    }
    public LiveData<List<Calendar>> getAll(){
        Query accessQuery=calendarRef;
        return new FirebaseQueryLiveData<>(accessQuery);
    }
    public LiveData<List<Calendar>> getByUserId(String userUid){
        Query accessQuery=calendarRef.orderByChild("ownerUser").equalTo(userUid);
        return new FirebaseQueryLiveData<>(accessQuery);
    }
    public LiveData<Calendar> getByCalendarUid(String calendarUid){
        Query accessQuery=calendarRef.orderByChild("uid").equalTo(calendarUid);
        return new FirebaseQueryLiveData<>(accessQuery);
    }
    public void insert(Calendar calendar){
        String key =calendarRef.push().getKey();
        calendar.uid=key;
        calendarRef.child(key).setValue(calendar).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success insert data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error insert data", e);
                });

    }
    public void delete(Calendar calendar){
        String key=calendar.uid;
        if(key==null){
            Log.e(LOG_TAG,"Can't delete calendar without uid");
            return;
        }
        calendarRef.child(key).setValue(null).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success delete data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error delete data", e);
                });

    }

    public void update(Calendar calendar){//Don't change the owner
        String key=calendar.uid;
        if(key==null){
            Log.e(LOG_TAG,"Can't update calendar without uid");
            return;
        }

        Map<String, Object> calendarValue = calendar.toMap();



        calendarRef.child(key).updateChildren(calendarValue).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success update data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error delete data", e);
                });
    }
    public void deleteAll(){
        calendarRef.setValue(null).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success delete all data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error delete delete data", e);
                });
    }

}
