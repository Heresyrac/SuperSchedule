package com.example.SuperSchedule.database.realtime;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.SuperSchedule.database.dao.CalendarDAO;
import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
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
        return Transformations.switchMap(new FirebaseQueryLiveData(accessQuery), (data) ->{
            GenericTypeIndicator<List<Calendar>> t = new GenericTypeIndicator<List<Calendar>>() {};
            MutableLiveData<List<Calendar>> r=new MutableLiveData<>();
            r.setValue(data.getValue(t));
            return r;
        });
    }
    public LiveData<List<Calendar>> getByUserId(String userUid){
        Query accessQuery=calendarRef.orderByChild("ownerUser").equalTo(userUid);
        return Transformations.switchMap(new FirebaseQueryLiveData(accessQuery), (data) ->{
            GenericTypeIndicator<List<Calendar>> t = new GenericTypeIndicator<List<Calendar>>() {};
            MutableLiveData<List<Calendar>> r=new MutableLiveData<>();
            r.setValue(data.getValue(t));
            return r;
        });
    }
    public LiveData<Calendar> getByCalendarUid(String calendarUid){
        Query accessQuery=calendarRef.orderByChild("uid").equalTo(calendarUid);
        return Transformations.switchMap(new FirebaseQueryLiveData(accessQuery), (data) ->{
            MutableLiveData<Calendar> r=new MutableLiveData<>();
            r.setValue(data.getValue(Calendar.class));
            return r;
        });
    }
    public void insert(Calendar calendar){
        String key =calendarRef.push().getKey();
        calendar.uid=key;
        calendarRef.child(key).setValue(calendar).addOnCompleteListener(new OnCompleteListener< Void >() {
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
    public void delete(Calendar calendar){
        String key=calendar.uid;
        if(key==null){
            Log.e(LOG_TAG,"Can't delete calendar without uid");
            return;
        }
        calendarRef.child(key).setValue(null).addOnCompleteListener(new OnCompleteListener< Void >() {
            @Override
            public void onComplete(@NonNull Task< Void > task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "****Success delete data");
                } else {
                    Log.e(LOG_TAG, "****Error delete data", task.getException());
                }
            }
        });

    }

    public void update(Calendar calendar){//Don't change the owner
        String key=calendar.uid;
        if(key==null){
            Log.e(LOG_TAG,"Can't update calendar without uid");
            return;
        }

        Map<String, Object> calendarValue = calendar.toMap();



        calendarRef.child(key).updateChildren(calendarValue).addOnCompleteListener(new OnCompleteListener< Void >() {
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
    public void deleteAll() {
        calendarRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "****Success delete data");
                } else {
                    Log.e(LOG_TAG, "****Error delete all data", task.getException());
                }
            }
        });
    }
}
