package com.example.SuperSchedule.database.realtime;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.SuperSchedule.database.dao.CalendarDAO;
import com.example.SuperSchedule.entity.Calendar;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public abstract class CalendarAccessor  implements CalendarDAO {
    /*
    DatabaseReference rootRef;
    DatabaseReference calendarRef;
    public CalendarAccessor(DatabaseReference rootRef){
        this.rootRef=rootRef;
        this.calendarRef=rootRef.child("calendars");
    }
    public LiveData<List<Calendar>> getAll(){
        Query accessQuery=calendarRef.child();
        FirebaseQueryLiveData<List<Calendar>> liveData=
                new FirebaseQueryLiveData<List<Calendar>>();
        return liveData;
    }
    @Query("SELECT rowid,* FROM calendar WHERE owner_user= :userUid")
    LiveData<List<Calendar>> getByUserId(String userUid);
    @Query("SELECT rowid,* FROM calendar WHERE rowid= :calendarUid")
    Calendar getByCalendarUid(int calendarUid);
    @Insert
    void insert(Calendar calendar);
    @Delete
    void delete(Calendar calendar);
    @Update
    void updateCustomer(Calendar calendar);
    @Query("DELETE FROM calendar")
    void deleteAll(){};
    */
}
