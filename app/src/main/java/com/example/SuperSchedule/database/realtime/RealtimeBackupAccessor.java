package com.example.SuperSchedule.database.realtime;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.SuperSchedule.database.dao.RealtimeBackupDAO;
import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.CalendarMember;
import com.example.SuperSchedule.entity.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class RealtimeBackupAccessor implements RealtimeBackupDAO {
    private static final String LOG_TAG = "RealtimeBackupAccessor";
    DatabaseReference rootRef;
    DatabaseReference backupRef;
    public RealtimeBackupAccessor(DatabaseReference rootRef){
        this.rootRef=rootRef;
        this.backupRef =rootRef.child("backup");
    }

    @Override
    public void setAllEvents(String userUid,List<Event> events) {
        Log.d(LOG_TAG, "setAllEvents is called");
        backupRef.child(userUid).child("events").setValue(events).addOnCompleteListener(new OnCompleteListener< Void >() {
            @Override
            public void onComplete(@NonNull Task< Void > task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "****Success backing up("+events.size()+ ") events");
                } else {
                    Log.e(LOG_TAG, "****Error backup events", task.getException());
                }
            }
        });

    }

    @Override
    public void setAllCalendars(String userUid,List<Calendar> calendars){
        Log.d(LOG_TAG, "setAllCalendars is called");
        backupRef.child(userUid).child("calendars").setValue(calendars).addOnCompleteListener(new OnCompleteListener< Void >() {
            @Override
            public void onComplete(@NonNull Task< Void > task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "****Success in backing up ("+calendars.size()+")calendars ");
                } else {
                    Log.e(LOG_TAG, "****Error in backing up calendars", task.getException());
                }
            }
        });

    }

    @Override
    public void setAllCalendarMembers(String userUid,List<CalendarMember> calendarMembers){
        Log.d(LOG_TAG, "setAllCalendarMembers is called");
        backupRef.child(userUid).child("calendarmembers").setValue(calendarMembers).addOnCompleteListener(new OnCompleteListener< Void >() {
            @Override
            public void onComplete(@NonNull Task< Void > task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "****Success in backing up ("+calendarMembers.size()+ ")  calendarmembers");
                } else {
                    Log.e(LOG_TAG, "****Error in backing up calendarmembers", task.getException());
                }
            }
        });

    }


}
