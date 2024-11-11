package com.example.user_module.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.user_module.entity.Test;

import java.util.List;

@Dao
public interface TestDao {

    @Insert
    void insert(Test test);

    @Update
    void update(Test test);

    @Delete
    void delete(Test test);

    @Query("SELECT * FROM tests ORDER BY date DESC")
    LiveData<List<Test>> getAllTests();

    @Query("SELECT * FROM tests WHERE id = :testId")
    LiveData<Test> getTestById(int testId);
}
