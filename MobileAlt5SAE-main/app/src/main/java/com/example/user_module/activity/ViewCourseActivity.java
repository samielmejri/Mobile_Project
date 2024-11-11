package com.example.user_module.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;

public class ViewCourseActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewCategory, textViewDifficultyLevel;
    private Button buttonViewPdf, buttonViewVideo;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewCategory = findViewById(R.id.textViewCategory);
        textViewDifficultyLevel = findViewById(R.id.textViewDifficultyLevel);
        buttonViewPdf = findViewById(R.id.buttonViewPdf);
        buttonViewVideo = findViewById(R.id.buttonViewVideo);

        // Retrieve the course ID passed from the adapter
        courseId = getIntent().getIntExtra("courseId", -1);
        loadCourseDetails();
    }

    private void loadCourseDetails() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.courseDao().getCourseById(courseId).observe(this, course -> { // Updated method name to getCourseById
            if (course != null) {
                textViewTitle.setText(course.getTitle());
                textViewCategory.setText(course.getCategory());
                textViewDifficultyLevel.setText(course.getDifficultyLevel());

                // Set up the PDF button
                if (course.getPdfUrl() != null && !course.getPdfUrl().isEmpty()) {
                    buttonViewPdf.setVisibility(View.VISIBLE);
                    buttonViewPdf.setOnClickListener(v -> openPdf(course.getPdfUrl()));
                } else {
                    buttonViewPdf.setVisibility(View.GONE);
                }

                // Set up the YouTube video button
                if (course.getVideoUrl() != null && !course.getVideoUrl().isEmpty()) {
                    buttonViewVideo.setVisibility(View.VISIBLE);
                    buttonViewVideo.setOnClickListener(v -> openVideo(course.getVideoUrl()));
                } else {
                    buttonViewVideo.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(this, "Course details not found", Toast.LENGTH_SHORT).show();
                finish(); // Close activity if course is not found
            }
        });
    }

    private void openPdf(String pdfUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void openVideo(String videoUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(intent);
    }
}
