package com.example.user_module.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.R;

public class ScoreActivity extends AppCompatActivity {

    private TextView scoreTextView, totalQuestionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreTextView = findViewById(R.id.scoreTextView);
        totalQuestionsTextView = findViewById(R.id.totalQuestionsTextView);

        // Get the score and total questions from the Intent
        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);

        // Display the score and total questions
        scoreTextView.setText("Score: " + score);
        totalQuestionsTextView.setText("Total Questions: " + totalQuestions);
    }
}
