package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.user_module.R;

public class QuizResultsActivity extends AppCompatActivity {
    private TextView scoreTextView, quizTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results); // Ensure you have this layout

        scoreTextView = findViewById(R.id.scoreTextView);
        quizTitleTextView = findViewById(R.id.quizTitleTextView);

        // Retrieve data from the intent
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        String quizTitle = intent.getStringExtra("quizTitle");

        // Display the score and quiz title
        scoreTextView.setText("Score: " + score);
        quizTitleTextView.setText("Quiz: " + quizTitle);
    }
}
