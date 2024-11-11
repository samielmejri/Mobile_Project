package com.example.user_module.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tests")
public class Test {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private long date; // Store the date as a timestamp

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}
