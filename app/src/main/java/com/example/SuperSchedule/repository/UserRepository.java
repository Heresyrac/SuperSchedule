package com.example.SuperSchedule.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.SuperSchedule.database.dao.CalendarDAO;
import com.example.SuperSchedule.database.dao.CalendarMemberDAO;
import com.example.SuperSchedule.database.dao.UserDAO;
import com.example.SuperSchedule.database.realtime.RealtimeDatabase;
import com.example.SuperSchedule.database.room.MainDatabase;
import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.CalendarMember;
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
    private final UserDAO userDAORemote;
    private final CalendarDAO calendarDAOLocal;
    private final CalendarMemberDAO calendarMemberDAOLocal;
    private final CalendarDAO calendarDAORemote;
    private final CalendarMemberDAO calendarMemberDAORemote;
    private final RealtimeDatabase dbRemote;

    //private UserDAO userDAOLocal;
    //private LiveData<List<Customer>> allCustomers;
    public UserRepository(Context context){
        dbRemote = RealtimeDatabase.getInstance();
        MainDatabase dbLocal = MainDatabase.getInstance(context);
        userDAORemote=dbRemote.userDao();
        calendarDAOLocal= dbLocal.calendarDao();
        calendarMemberDAOLocal= dbLocal.calendarMemberDao();
        calendarDAORemote=dbRemote.calendarDao();
        calendarMemberDAORemote=dbRemote.calendarMemberDao();
        //userDAORemote=dbLocal.userDAO();
    }
    public LiveData<User> getCurrentUser() {return new CurrentUserLiveData();}
       /* MediatorLiveData<User> mergeLiveData=new MediatorLiveData<>();
        LiveData<User> curUser=new CurrentUserLiveData();
        LiveData<User> liveData1=Transformations.switchMap(curUser, (user) ->
                getByUserUid( (user!=null)?user.getUid():""));
        mergeLiveData.addSource(liveData1, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user!=null){
                    User user1=mergeLiveData.getValue();
                    if(user1!=null){
                        user1.setPhone(user.getPhone());
                        user1.setName(user.getName());
                    }
                    mergeLiveData.setValue(user1);
                }
            }
        });
        mergeLiveData.addSource(curUser, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user!=null){
                    User user1=mergeLiveData.getValue();
                    if(user1!=null){
                        user.setPhone(user1.getPhone());
                        user.setName(user1.getName());
                    }
                    mergeLiveData.setValue(user);
                }
                else{
                    mergeLiveData.setValue(null);
                }
            }
        });
        return mergeLiveData;

    }*/
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
                if(user.uid==null){
                    Log.e("UserRepository", "Cant insert a user without UID");
                    return;
                }
                userDAORemote.insert(user);

                Calendar calendarLocal=new Calendar(user.getName()+"'s Local Calendar",user.getUid(),false);
                calendarLocal.uid="DefaultLocalCalendar-"+user.uid;
                calendarDAOLocal.insert(calendarLocal); //local insert->创建并替换
                CalendarMember newMembership1= new
                        CalendarMember(calendarLocal.uid,calendarLocal.ownerUser,3);
                calendarMemberDAOLocal.insert(newMembership1);//新增用户时初始化一个默认的本地Calendar

                Calendar calendarRemote=new Calendar(user.getName()+"'s Local Calendar",user.getUid(),true);
                calendarRemote.uid="DefaultRemoteCalendar-"+user.uid;
                calendarDAORemote.update(calendarRemote); //remote update->创建并替换
                CalendarMember newMembership2= new
                        CalendarMember(calendarRemote.uid,calendarRemote.ownerUser,2);
                calendarMemberDAORemote.insert(newMembership2);//新增用户时初始化一个默认的共享Calendar

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
        insert(user);
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
    public void signupWithEmailPassword(String email, String password,OnCompleteListener<AuthResult> listener){
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String TAG="SIGNUP";
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
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
    public void signinWithEmailPassword(String email, String password,OnCompleteListener<AuthResult> listener){
        FirebaseAuth mAuth=FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }
    public void updateCurrentUserEmail(String email){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null) {
            Log.e(TAG, "Can't update user email without Sign IN !");
            return;
        }
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
        if (user==null) {
            Log.e(TAG, "Can't update user name without Sign IN !");
            return;
        }
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
