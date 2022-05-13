package com.example.SuperSchedule.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.SuperSchedule.database.dao.UserDAO;
import com.example.SuperSchedule.database.realtime.RealtimeDatabase;
import com.example.SuperSchedule.database.room.MainDatabase;
import com.example.SuperSchedule.entity.User;

import java.util.List;

public class UserRepository {
    private UserDAO userDAORemote;
    private RealtimeDatabase dbRemote;
    private MainDatabase dbLocal;
    //private UserDAO userDAOLocal;
    //private LiveData<List<Customer>> allCustomers;
    public UserRepository(Application application){
        dbRemote = RealtimeDatabase.getInstance();
        dbLocal = MainDatabase.getInstance(application);

        userDAORemote=dbRemote.userDAO();
        //userDAORemote=dbLocal.userDAO();
    }

    public LiveData<User> getByUserId(String userUid) {
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
}
