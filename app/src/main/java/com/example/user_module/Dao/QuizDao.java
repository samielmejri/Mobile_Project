package com.example.user_module.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.user_module.entity.Quiz;
import com.example.user_module.entity.QuizWithQuestions;

import java.util.List;

@Dao
public interface QuizDao {
    @Insert
    void insertQuiz(Quiz quiz);

    @Query("SELECT * FROM Quiz")
    LiveData<List<Quiz>> getAllQuizzes(); // LiveData ensures automatic UI updates

    @Query("SELECT * FROM Quiz WHERE id = :quizId LIMIT 1")
    Quiz getQuizById(int quizId);

    @Transaction
    @Query("SELECT * FROM Quiz WHERE id = :quizId LIMIT 1")
    QuizWithQuestions getQuizWithQuestions(int quizId);

    @Update
    void updateQuiz(Quiz quiz);

    @Delete
    void delete(Quiz quiz); // Retain only one delete method
}
