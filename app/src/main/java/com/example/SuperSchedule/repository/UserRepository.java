package com.example.SuperSchedule.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.SuperSchedule.database.dao.UserDAO;
import com.example.SuperSchedule.database.realtime.RealtimeDatabase;
import com.example.SuperSchedule.database.room.MainDatabase;
import com.example.SuperSchedule.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.List;
import java.util.concurrent.Executor;

public class UserRepository {
    String TAG="UserRepository";
    private UserDAO userDAORemote;
    private RealtimeDatabase dbRemote;
    private MainDatabase dbLocal;
    //private UserDAO userDAOLocal;
    //private LiveData<List<Customer>> allCustomers;
    public UserRepository(Application application){
        dbRemote = RealtimeDatabase.getInstance();
        dbLocal = MainDatabase.getInstance(application);
        userDAORemote=dbRemote.userDao();
        //userDAORemote=dbLocal.userDAO();
    }
    public LiveData<User> getCurrentUser(){
        return new CurrentUserLiveData();
    }
    public LiveData<User> getByUserUid(String userUid) {
        return userDAORemote.findByID(userUid);
    }
    public LiveData<List<User>> getAll() {
        return userDAORemote.getAll();
    }

    public void insert(final User user){

        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userDAORemote.insert(user);
            }
        });
    }
    public void delete(final User user){
        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userDAORemote.delete(user);
                /*
                Workmanager-> delete all related membership



                */
            }
        });
    }
    public void update(final User user) {
        dbRemote.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userDAORemote.update(user);
            }
        });
    }
    public void signupWithEmailPassword(String email, String password){
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String TAG="SIGNUP";
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });


    }
    public void signinWithEmailPassword(String email, String password){
        FirebaseAuth mAuth=FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }
    public void updateCurrentUserEmail(String email){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });
    }
    public void updateCurrentUserName(String name){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("name")
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User name updated.");
                        }
                    }
                });

    }


}
