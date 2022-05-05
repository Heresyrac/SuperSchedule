package com.example.SuperSchedule.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;
@Fts4
@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String name;
    public String password;
    public String email;
    public String phone;


    public User( String name,
                 String password,
                 String email,
                 String phone
    ) {
        this.name=name;
        this.password=password;
        this.email=email;
        this.phone=phone;

    }
}
