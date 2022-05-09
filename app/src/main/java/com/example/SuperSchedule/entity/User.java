package com.example.SuperSchedule.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;
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
}
