package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Course;

import java.util.concurrent.Executors;

public class AddEditCourseActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextCategory, editTextDifficultyLevel, editTextPdfUrl, editTextVideoUrl;
    private Button buttonSaveCourse;
    private int courseId = -1; // Default to -1 to indicate a new course

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextDifficultyLevel = findViewById(R.id.editTextDifficultyLevel);
        editTextPdfUrl = findViewById(R.id.editTextPdfUrl);
        editTextVideoUrl = findViewById(R.id.editTextVideoUrl);
        buttonSaveCourse = findViewById(R.id.buttonSaveCourse);

        // Check if we are editing an existing course
        courseId = getIntent().getIntExtra("courseId", -1);
        if (courseId != -1) {
            loadCourseData();
        }

        buttonSaveCourse.setOnClickListener(v -> saveCourse());
    }

    private void loadCourseData() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.courseDao().getCourseById(courseId).observe(this, course -> { // Updated method name to getCourseById
            if (course != null) {
                editTextTitle.setText(course.getTitle());
                editTextCategory.setText(course.getCategory());
                editTextDifficultyLevel.setText(course.getDifficultyLevel());
                editTextPdfUrl.setText(course.getPdfUrl());
                editTextVideoUrl.setText(course.getVideoUrl());
            }
        });
    }


    private void saveCourse() {
        String title = editTextTitle.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String difficultyLevel = editTextDifficultyLevel.getText().toString().trim();
        String pdfUrl = editTextPdfUrl.getText().toString().trim();
        String videoUrl = editTextVideoUrl.getText().toString().trim();

        if (title.isEmpty() || category.isEmpty() || difficultyLevel.isEmpty()) {
            Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Course course = new Course();
        course.setTitle(title);
        course.setCategory(category);
        course.setDifficultyLevel(difficultyLevel);
        course.setPdfUrl(pdfUrl);
        course.setVideoUrl(videoUrl);

        if (courseId != -1) {
            course.setId(courseId); // Set ID if editing
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            if (courseId == -1) {
                db.courseDao().insert(course); // Insert new course
            } else {
                db.courseDao().update(course); // Update existing course
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Course saved", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish(); // Close activity
            });
        });
    }
}
