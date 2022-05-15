package com.example.SuperSchedule.database.realtime;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.SuperSchedule.database.dao.UserDAO;
import com.example.SuperSchedule.entity.CalendarMember;
import com.example.SuperSchedule.entity.Event;
import com.example.SuperSchedule.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;

import java.util.List;

public  class UserAccessor implements UserDAO {
    private static final String LOG_TAG = "RealtimeUser";
    DatabaseReference rootRef;
    DatabaseReference userRef;
    public UserAccessor(DatabaseReference rootRef){
        this.rootRef=rootRef;
        this.userRef =rootRef.child("users");
    }

    public LiveData<List<User>> getAll(){

        Query accessQuery=userRef.orderByChild("name");
        return Transformations.switchMap(new FirebaseQueryLiveData(accessQuery), (data) ->{
            GenericTypeIndicator<List<User>> t = new GenericTypeIndicator<List<User>>() {};
            MutableLiveData<List<User>> r=new MutableLiveData<>();
            r.setValue(data.getValue(t));
            return r;
        });

    };

    public LiveData<User> findByID(String userUid){
        Query accessQuery=userRef.child(userUid);
        return Transformations.switchMap(new FirebaseQueryLiveData(accessQuery), (data) ->{
            MutableLiveData<User> r=new MutableLiveData<>();
            r.setValue(data.getValue(User.class));
            return r;
        });
    }

    public void insert(User user){
        String key=user.uid;
        if(key==null){
            Log.e(LOG_TAG,"Can't insert user without (generated) uid");
            return;
        }
        userRef.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener< Void >() {
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

    public void delete(User user){
        String key=user.uid;
        if(key==null){
            Log.e(LOG_TAG,"Can't delete user without (generated) uid");
            return;
        }
        userRef.child(key).setValue(null).addOnCompleteListener(new OnCompleteListener< Void >() {
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

    public void update(User user){
        insert(user);
    }

}
