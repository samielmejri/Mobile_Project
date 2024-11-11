package com.example.user_module.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Forum;

import java.util.concurrent.Executors;

public class AddEditForumActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private Button buttonSaveForum;
    private int forumId = -1; // Default to -1 to indicate "new" forum

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_forum);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSaveForum = findViewById(R.id.buttonSaveForum);

        // Check if an existing forum is being edited
        forumId = getIntent().getIntExtra("forumId", -1);
        if (forumId != -1) {
            loadForumDetails(); // Load existing forum details for editing
        }

        buttonSaveForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveForum();
            }
        });
    }

    // Load the details of the forum if we're in "edit" mode
    private void loadForumDetails() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.forumDao().getForumById(forumId).observe(this, forum -> {
            if (forum != null) {
                editTextTitle.setText(forum.getName());
                editTextDescription.setText(forum.getDescription());
            }
        });
    }

    private void saveForum() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter both title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create or update a forum object based on whether it's new or existing
        Forum forum = new Forum(title, description);
        if (forumId != -1) {
            forum.setId(forumId); // Set the ID for updating the existing forum
        }

        // Use Executors to perform the database operation in a background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            if (forumId == -1) {
                db.forumDao().insert(forum); // Insert if it's a new forum
            } else {
                db.forumDao().update(forum); // Update if it's an existing forum
            }

            // Switch back to the main thread to show a Toast and close the activity
            runOnUiThread(() -> {
                Toast.makeText(this, "Forum saved", Toast.LENGTH_SHORT).show();
                finish(); // Close activity
            });
        });
    }
}
