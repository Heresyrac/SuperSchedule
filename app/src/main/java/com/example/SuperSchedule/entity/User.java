package com.example.SuperSchedule.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {
    @NonNull
    @PrimaryKey
    public String uid;

    public String name;
    public String password;
    public String email;
    public String phone;

    public User(){};

    public void setEmail(String email) { this.email = email; }

    public void setUid(@NonNull String uid) { this.uid = uid; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setPassword(String password) { this.password = password; }

    public void setName(String name) { this.name = name; }

    @NonNull
    public String getUid() { return uid; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    @Ignore
    public User(String uid,
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
    @Ignore
    public User(FirebaseUser user){
        this.uid=user.getUid();
        this.name=user.getDisplayName();
        this.password=null;
        this.email=user.getEmail();
        this.phone=user.getPhoneNumber();
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("email", email);
        result.put("name", name);
        result.put("password",password);
        result.put("phone", phone);
        return result;
    }
}
