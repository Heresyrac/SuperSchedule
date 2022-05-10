package com.example.SuperSchedule.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseUser;

@Entity
public class User {
    @NonNull
    @PrimaryKey
    public String uid;

    public String name;
    public String password;
    public String email;
    public String phone;


    public User( String uid,
                 String name,
                 String password,
                 String email,
                 String phone
    ) {
        this.uid=uid;
        this.name=name;
        this.password=password;
        this.email=email;
        this.phone=phone;

    }

    public User(FirebaseUser user){
        this.uid=user.getUid();
        this.name=user.getDisplayName();
        this.password=null;
        this.email=user.getEmail();
        this.phone=user.getPhoneNumber();
    }
}
