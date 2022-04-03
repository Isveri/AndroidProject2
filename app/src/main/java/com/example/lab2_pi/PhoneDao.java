package com.example.lab2_pi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhoneDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Phone phone);

    @Query("DELETE FROM phones")
    void deleteAll();

    @Query("SELECT * FROM phones")
    LiveData<List<Phone>> getAllPhones();

    @Update
    void updatePhone(Phone phone);

    @Delete
    void deletePhone(Phone phone);
}
