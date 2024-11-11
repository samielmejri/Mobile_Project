package com.example.user_module.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(tableName = "posts",
        foreignKeys = @ForeignKey(entity = Forum.class,
                parentColumns = "id",
                childColumns = "forumId",
                onDelete = ForeignKey.CASCADE))
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int forumId; // Foreign key to Forum
    private String content;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getForumId() { return forumId; }
    public void setForumId(int forumId) { this.forumId = forumId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
