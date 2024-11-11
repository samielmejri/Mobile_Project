package com.example.user_module.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.user_module.entity.Question;

import java.util.List;

@Dao
public interface QuestionDao {
    @Insert
    long insert(Question question);

    @Update
    void updateQuestion(Question question);

    @Delete
    void deleteQuestion(Question question);

    @Query("SELECT * FROM Question WHERE id = :questionId LIMIT 1")
    Question getQuestionById(int questionId);

    @Query("SELECT * FROM Question WHERE quizId = :quizId")
    List<Question> getQuestionsByQuizId(int quizId);
}
