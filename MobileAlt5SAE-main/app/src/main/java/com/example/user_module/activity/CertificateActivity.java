package com.example.user_module.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.R;

public class CertificateActivity extends AppCompatActivity {

    private TextView certificateTitle, certificateQuizTitle, certificateScore, certificateMessage;
    private Button retryQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        certificateTitle = findViewById(R.id.certificateTitle);
        certificateQuizTitle = findViewById(R.id.certificateQuizTitle);
        certificateScore = findViewById(R.id.certificateScore);
        certificateMessage = findViewById(R.id.certificateMessage);
        retryQuizButton = findViewById(R.id.retryQuizButton);

        String quizTitle = getIntent().getStringExtra("quizTitle");
        int score = getIntent().getIntExtra("score", 0);

        certificateQuizTitle.setText(quizTitle);
        certificateScore.setText("Score: " + score);

        // Set a motivational message based on the score
        if (score == 100) {
            certificateMessage.setText("Outstanding! You nailed it!");
        } else if (score >= 75) {
            certificateMessage.setText("Great job! Keep it up!");
        } else {
            certificateMessage.setText("Good effort! Keep learning!");
        }

        retryQuizButton.setOnClickListener(v -> {
            // Go back to the quiz activity to retry
            finish();
        });
    }
}
