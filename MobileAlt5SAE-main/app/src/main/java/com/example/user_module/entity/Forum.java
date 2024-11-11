package com.example.user_module.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "forums")
public class Forum {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private long createdAt;

    // Constructor used by Room (includes all fields)
    public Forum(int id, String name, String description, long createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Additional constructor for creating new Forum objects without an ID
    @Ignore
    public Forum(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
