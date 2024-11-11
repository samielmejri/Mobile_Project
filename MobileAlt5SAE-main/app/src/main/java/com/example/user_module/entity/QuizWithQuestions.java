package com.example.user_module.entity;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

public class QuizWithQuestions {
    @Embedded
    public Quiz quiz;

    @Relation(
            parentColumn = "id",    // Primary key in the Quiz table
            entityColumn = "quizId" // Foreign key in the Question table
    )
    public List<Question> questions;
}
