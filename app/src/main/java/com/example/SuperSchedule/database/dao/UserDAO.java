package com.example.SuperSchedule.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.SuperSchedule.entity.Customer;
import com.example.SuperSchedule.entity.Event;
import com.example.SuperSchedule.entity.User;

import java.util.List;
@Dao
public interface UserDAO {
    @Query("SELECT * FROM user ORDER BY name ASC")
    LiveData<List<User>> getAll();
    @Query("SELECT * FROM user WHERE uid = :userUid LIMIT 1")
    LiveData<User> findByID(String userUid);
    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

}
