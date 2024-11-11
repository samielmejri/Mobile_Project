package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Test;

import java.util.concurrent.Executors;

public class AddEditTestActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextDate;
    private Button buttonSaveTest;
    private int testId = -1; // Default to -1 to indicate a new test

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_test);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        buttonSaveTest = findViewById(R.id.buttonSaveTest);

        // Check if we are editing an existing test
        testId = getIntent().getIntExtra("testId", -1);
        if (testId != -1) {
            loadTestData();
        }

        buttonSaveTest.setOnClickListener(v -> saveTest());
    }

    private void loadTestData() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.testDao().getTestById(testId).observe(this, test -> {
            if (test != null) {
                editTextTitle.setText(test.getTitle());
                editTextDescription.setText(test.getDescription());
                editTextDate.setText(String.valueOf(test.getDate())); // Display the date as needed
            }
        });
    }

    private void saveTest() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        long date;
        try {
            date = Long.parseLong(editTextDate.getText().toString().trim()); // Use a proper date format if necessary
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Test test = new Test();
        test.setTitle(title);
        test.setDescription(description);
        test.setDate(date);

        if (testId != -1) {
            test.setId(testId); // Set ID if editing
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            if (testId == -1) {
                db.testDao().insert(test); // Insert new test
            } else {
                db.testDao().update(test); // Update existing test
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Test saved", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish(); // Close activity
            });
        });
    }
}
