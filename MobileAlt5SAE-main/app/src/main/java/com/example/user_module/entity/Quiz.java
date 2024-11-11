package com.example.user_module.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int numberOfQuestions;
    private String category;

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public String getCategory() {
        return category;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
