package com.example.user_module.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.user_module.entity.Forum;
import com.example.user_module.entity.Post;

import java.util.List;

@Dao
public interface ForumDao {

    @Insert
    void insert(Forum forum);

    @Update
    void update(Forum forum);

    @Delete
    void delete(Forum forum);

    @Query("SELECT * FROM forums ORDER BY createdAt DESC")
    LiveData<List<Forum>> getAllForums();

    @Query("SELECT * FROM forums WHERE id = :id")
    LiveData<Forum> getForumById(int id);

    @Insert
    void insertPost(Post post);

    @Update
    void updatePost(Post post);

    @Delete
    void deletePost(Post post);

    @Query("SELECT * FROM posts WHERE forumId = :forumId ORDER BY id DESC")
    LiveData<List<Post>> getPostsForForum(int forumId);

    @Query("SELECT * FROM posts WHERE id = :postId")
    LiveData<Post> getPostById(int postId);



}
