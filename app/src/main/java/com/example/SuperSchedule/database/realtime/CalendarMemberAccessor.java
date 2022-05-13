package com.example.SuperSchedule.database.realtime;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.SuperSchedule.database.dao.CalendarMemberDAO;
import com.example.SuperSchedule.entity.CalendarMember;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarMemberAccessor implements CalendarMemberDAO {
    private static final String LOG_TAG = "RealtimeCalMember";
    DatabaseReference rootRef;
    DatabaseReference calendarMemberRef;
    public CalendarMemberAccessor(DatabaseReference rootRef){
        this.rootRef=rootRef;
        this.calendarMemberRef =rootRef.child("calendarmembers");
    }
    public LiveData<List<CalendarMember>> getByUserUid(String userUid){
        Query accessQuery= calendarMemberRef
                .child("by_user")
                .child(userUid)
                .orderByChild("userAuthLv");
        return new FirebaseQueryLiveData<>(accessQuery);
    }
    public LiveData<List<CalendarMember>> getByCalendarUid(String calendarUid){
        Query accessQuery= calendarMemberRef
                .child("by_calendar")
                .child(calendarUid)
                .orderByChild("userAuthLv");
        return new FirebaseQueryLiveData<>(accessQuery);
    }
    public LiveData<CalendarMember> getByUserCalendar( String userUid,String calendarUid){
        Query accessQuery= calendarMemberRef
                .child("by_calendar")
                .child(calendarUid)
                .child(userUid);
        return new FirebaseQueryLiveData<>(accessQuery);
    };
    public void insert(CalendarMember calendarMember){
        String key1=calendarMember.userUid;
        String key2=calendarMember.calendarUid;
        if(key1==null|key2==null){
            Log.e(LOG_TAG,"Can't update calendar without userUid or calendarUid");
            return;
        }
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/by_user/" +
                calendarMember.userUid+"/"+
                calendarMember.calendarUid,calendarMember.toMap());
        childUpdates.put("/by_calendar/" +
                calendarMember.calendarUid+"/"+
                calendarMember.userUid,calendarMember.toMap());

        calendarMemberRef.updateChildren(childUpdates).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success insert data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error insert data", e);
                });

    }

    public void delete(CalendarMember calendarMember){
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/by_user/" +
                calendarMember.userUid+"/"+
                calendarMember.calendarUid,null);
        childUpdates.put("/by_calendar/" +
                calendarMember.calendarUid+"/"+
                calendarMember.userUid,null);

        calendarMemberRef.updateChildren(childUpdates).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success insert data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error insert data", e);
                });;

    }

    public void update(CalendarMember calendarMember){
        insert(calendarMember);
    }
    public void deleteAll(){
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/by_user",null);
        childUpdates.put("/by_calendar",null);

        calendarMemberRef.updateChildren(childUpdates).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success insert data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error insert data", e);
                });;
    }

}
