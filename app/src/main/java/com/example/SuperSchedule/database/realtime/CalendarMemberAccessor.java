package com.example.SuperSchedule.database.realtime;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.SuperSchedule.database.dao.CalendarMemberDAO;
import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.CalendarMember;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CalendarMemberAccessor implements CalendarMemberDAO {
    private static final String LOG_TAG = "RealtimeCalMember";
    DatabaseReference rootRef;
    DatabaseReference calendarmemberRef;
    public CalendarMemberAccessor(DatabaseReference rootRef){
        this.rootRef=rootRef;
        this.calendarmemberRef =rootRef.child("calendarmembers");
    }
    public LiveData<List<CalendarMember>> getByUserId(String userUid){
        Query accessQuery= calendarmemberRef
                .child("by_user")
                .child(userUid)
                .orderByChild("userAuthLv");
        return new FirebaseQueryLiveData<>(accessQuery);
    }
    public LiveData<Calendar> getByCalendarUid(String calendarUid){
        Query accessQuery= calendarmemberRef
                .child("by_calendar")
                .child(calendarUid)
                .orderByChild("userAuthLv");
        return new FirebaseQueryLiveData<>(accessQuery);
    }
    public LiveData<CalendarMember> getByUserCalendar( String userUid,String calendarUid){
        Query accessQuery= calendarmemberRef
                .child("by_calendar")
                .child(calendarUid)
                .child(userUid);
        return new FirebaseQueryLiveData<>(accessQuery);
    };
    public void insert(CalendarMember calendarMember){
        Map<String, Object> calendarMemberUpdate = new HashMap<>();
        calendarValue.put("uid", calendar.uid);
        calendarValue.put("ownerUser", calendar.ownerUser);

        calendarmemberRef.child("by_user")
                .child(calendarMember.userUid)
                .child(calendarMember.calendarUid)
                .setValue(calendarMember)
                .addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success insert data A");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error insert data A", e);
                });
        calendarmemberRef.child(key).setValue(calendarMember).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success insert data A");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error insert data A", e);
                });
    }
    public void insert(Calendar calendar){
        String key = calendarmemberRef.push().getKey();
        calendar.uid=key;
        calendarmemberRef.child(key).setValue(calendar).addOnSuccessListener(aVoid -> {
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
        }
        calendarmemberRef.child(key).setValue(null).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success delete data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error delete data", e);
                });

    }

    public void update(CalendarMember calendarMember){
        String userUid=calendarMember.userUid;
        String calendarUid=calendarMember.calendarUid;

        if(userUid==null | calendarUid==null){
            Log.e(LOG_TAG,"Can't update calendarmember without user(or calendar)");
            return;
        }
        calendarmemberRef.child(key).setValue(null).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success update data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error delete data", e);
                });
    }
    public void deleteAll(){
        calendarmemberRef.setValue(null).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success delete all data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error delete delete data", e);
                });
    }

}
}
