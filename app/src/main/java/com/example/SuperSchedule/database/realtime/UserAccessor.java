package com.example.SuperSchedule.database.realtime;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.SuperSchedule.database.dao.UserDAO;
import com.example.SuperSchedule.entity.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.List;

public  class UserAccessor implements UserDAO {
    private static final String LOG_TAG = "RealtimeUser";
    DatabaseReference rootRef;
    DatabaseReference userRef;
    public UserAccessor(DatabaseReference rootRef){
        this.rootRef=rootRef;
        this.userRef =rootRef.child("events");
    }

    public LiveData<List<User>> getAll(){

        Query accessQuery=userRef.orderByChild("name");
        return new FirebaseQueryLiveData<>(accessQuery);

    };

    public LiveData<User> findByID(String userUid){
        Query accessQuery=userRef.child(userUid);
        return new FirebaseQueryLiveData<>(accessQuery);

    }

    public void insert(User user){
        String key=user.uid;
        if(key==null){
            Log.e(LOG_TAG,"Can't insert user without (generated) uid");
            return;
        }
        userRef.child(key).setValue(user).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success insert data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error insert data", e);
                });

    }

    public void delete(User user){
        String key=user.uid;
        if(key==null){
            Log.e(LOG_TAG,"Can't delete user without (generated) uid");
            return;
        }
        userRef.child(key).setValue(null).addOnSuccessListener(aVoid -> {
            // Write was successful!
            Log.d(LOG_TAG, "Success delete data");
        })
                .addOnFailureListener(e -> {
                    // Write failed
                    Log.e(LOG_TAG, "Error delete data", e);
                });
    }

    public void update(User user){
        insert(user);
    }

}
