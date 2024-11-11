package com.example.user_module.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.user_module.entity.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses ORDER BY title ASC")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM courses WHERE id = :courseId")
    LiveData<Course> getCourseById(int courseId); // Updated method name to getCourseById
}
