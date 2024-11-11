package com.example.user_module.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.user_module.Converters;

import java.util.List;

@Entity(tableName = "Question",indices = {@Index(value = "quizId")})
@TypeConverters(Converters.class) // Link to the Converters class for List<Integer> handling
public class Question {

    @PrimaryKey(autoGenerate = true)
    private int id; // This will be auto-generated by Room for new questions.

    private String text;
    private String category;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private List<Integer> correctAnswers;
    @ColumnInfo(name = "quizId")
    private int quizId;

    // Constructor for adding new questions (without an ID)
    public Question(String text, String option1, String option2, String option3, String option4, List<Integer> correctAnswers, int quizId) {
        this.text = text;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswers = correctAnswers;
        this.quizId = quizId;
    }

    // Constructor for updating existing questions (with an ID)
    @Ignore
    public Question(int id, String text, String option1, String option2, String option3, String option4, List<Integer> correctAnswers, int quizId) {
        this.id = id;
        this.text = text;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswers = correctAnswers;
        this.quizId = quizId;
    }

    // Getters and setters for the new fields
    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Integer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    // Getters and Setters for other fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
