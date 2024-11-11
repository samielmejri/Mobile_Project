package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Post;

import java.util.concurrent.Executors;

public class CreatePostActivity extends AppCompatActivity {

    private EditText editTextContent;
    private Button buttonSavePost;
    private int forumId;
    private int postId = -1; // Default to -1 to indicate "new" post

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        editTextContent = findViewById(R.id.editTextContent);
        buttonSavePost = findViewById(R.id.buttonSavePost);

        // Get forumId and postId from intent
        forumId = getIntent().getIntExtra("forumId", -1);
        postId = getIntent().getIntExtra("postId", -1);

        // Load post data if editing
        if (postId != -1) {
            loadPostData();
        }

        buttonSavePost.setOnClickListener(v -> savePost());
    }

    private void loadPostData() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.forumDao().getPostById(postId).observe(this, post -> {
            if (post != null) {
                editTextContent.setText(post.getContent());
            }
        });
    }

    private void savePost() {
        String content = editTextContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Please write something", Toast.LENGTH_SHORT).show();
            return;
        }

        Post post = new Post();
        post.setForumId(forumId);
        post.setContent(content);

        // If editing, set the post ID
        if (postId != -1) {
            post.setId(postId);
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            if (postId == -1) {
                db.forumDao().insertPost(post); // Insert new post
            } else {
                db.forumDao().updatePost(post); // Update existing post
            }

            // Return to previous activity with a result
            runOnUiThread(() -> {
                Toast.makeText(this, "Post saved", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // Set result to notify the calling activity
                finish(); // Close activity
            });
        });
    }
}
