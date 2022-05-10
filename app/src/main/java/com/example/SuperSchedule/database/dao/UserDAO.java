package com.example.SuperSchedule.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.example.SuperSchedule.entity.Customer;
import com.example.SuperSchedule.entity.User;

import java.util.List;

public interface UserDAO {
    @Query("SELECT * FROM user ORDER BY name ASC")
    LiveData<List<User>> getAll();
    @Query("SELECT rowid,* FROM customer WHERE rowid = :customerId LIMIT 1")
    LiveData<Customer> findByID(int customerId);
}
