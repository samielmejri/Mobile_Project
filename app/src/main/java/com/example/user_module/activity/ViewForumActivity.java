package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.Adapters.PostAdapter;
import com.example.user_module.entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ViewForumActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CREATE_POST = 1;

    private TextView textViewTitle, textViewDescription;
    private Button buttonAddPost;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private int forumId;
    private List<Post> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_forum);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        buttonAddPost = findViewById(R.id.buttonAddPost);
        recyclerViewPosts = findViewById(R.id.recyclerViewPosts);

        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(this, postList);
        recyclerViewPosts.setAdapter(postAdapter);

        forumId = getIntent().getIntExtra("forumId", -1);

        loadForumDetails();
        loadPosts();

        buttonAddPost.setOnClickListener(v -> addNewPost());
    }

    private void loadForumDetails() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.forumDao().getForumById(forumId).observe(this, forum -> {
            if (forum != null) {
                textViewTitle.setText(forum.getName());
                textViewDescription.setText(forum.getDescription());
            }
        });
    }

    private void loadPosts() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.forumDao().getPostsForForum(forumId).observe(this, posts -> {
            postList.clear();
            postList.addAll(posts);
            postAdapter.notifyDataSetChanged();
        });
    }

    private final ActivityResultLauncher<Intent> createPostLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadPosts(); // Reload posts if a new post was added or edited successfully
                }
            });


    private void addNewPost() {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra("forumId", forumId); // Pass the forumId to associate the post with the correct forum
        createPostLauncher.launch(intent); // Use the launcher to start the activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_POST && resultCode == RESULT_OK) {
            loadPosts(); // Reload posts to show the newly added one
        }
    }


}


