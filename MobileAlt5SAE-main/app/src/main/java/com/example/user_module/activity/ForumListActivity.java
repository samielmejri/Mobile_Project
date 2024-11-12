package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.Adapters.ForumAdapter;
import com.example.user_module.R;
import com.example.user_module.ViewModal.ForumViewModel;

public class ForumListActivity extends BaseActivity {

    private RecyclerView recyclerViewForums;
    private Button buttonAddForum;
    private ForumAdapter forumAdapter;
    private ForumViewModel forumViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this activity into the content frame of the BaseActivity
        getLayoutInflater().inflate(R.layout.activity_forum_list, findViewById(R.id.content_frame));
        setTitle("Forums"); // Set the title for the toolbar

        recyclerViewForums = findViewById(R.id.recyclerViewForums);
        buttonAddForum = findViewById(R.id.buttonAddForum);

        recyclerViewForums.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModel
        forumViewModel = new ViewModelProvider(this).get(ForumViewModel.class);

        // Observe the LiveData from ViewModel
        forumViewModel.getAllForums().observe(this, forums -> {
            // Update the adapter with new data whenever it changes
            forumAdapter = new ForumAdapter(this, forums);
            recyclerViewForums.setAdapter(forumAdapter);
        });

        buttonAddForum.setOnClickListener(v -> {
            Intent intent = new Intent(ForumListActivity.this, AddEditForumActivity.class);
            startActivity(intent);
        });
    }
}
