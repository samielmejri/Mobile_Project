package com.example.user_module.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.user_module.entity.Program;

import java.util.List;

@Dao
public interface ProgramDao {

    @Insert
    void insert(Program program);

    @Update
    void update(Program program);

    @Delete
    void delete(Program program);

    @Query("SELECT * FROM programs ORDER BY startDate DESC")
    LiveData<List<Program>> getAllPrograms();

    @Query("SELECT * FROM programs WHERE id = :programId")
    LiveData<Program> getProgramById(int programId);

    // New query to retrieve programs occurring on a specific date
    @Query("SELECT * FROM programs WHERE :date BETWEEN startDate AND endDate")
    LiveData<List<Program>> getProgramsByDate(long date);

    // New query to retrieve programs within a specific date range
    @Query("SELECT * FROM programs WHERE startDate >= :startDate AND endDate <= :endDate ORDER BY startDate DESC")
    LiveData<List<Program>> getProgramsByDateRange(long startDate, long endDate);
}
