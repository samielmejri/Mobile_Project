package com.example.user_module.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Question;
import java.util.concurrent.Executors;

public class AddQuestionActivity extends AppCompatActivity {
    private EditText questionTextEditText;
    private Button saveQuestionButton;
    private int quizId;
    private int questionId = -1;  // Default to -1, indicating no question to edit
    private static final String TAG = "AddQuestionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        questionTextEditText = findViewById(R.id.questionTextEditText);
        saveQuestionButton = findViewById(R.id.saveQuestionButton);

        quizId = getIntent().getIntExtra("quizId", -1);
        questionId = getIntent().getIntExtra("questionId", -1);

        if (questionId != -1) {
            // If questionId is provided, load the question details for editing
            loadQuestionDetails();
        }

        saveQuestionButton.setOnClickListener(v -> saveQuestion());
    }

    private void loadQuestionDetails() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Question question = AppDatabase.getInstance(getApplicationContext()).questionDao().getQuestionById(questionId);
            if (question != null) {
                runOnUiThread(() -> questionTextEditText.setText(question.getText()));
            } else {
                Log.e(TAG, "Question not found with id: " + questionId);
            }
        });
    }

    private void saveQuestion() {
        String questionText = questionTextEditText.getText().toString().trim();

        if (questionText.isEmpty()) {
            Toast.makeText(this, "Question text is required", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());

                if (questionId == -1) {
                    // Add a new question
                    Question question = new Question();
                    question.setQuizId(quizId);
                    question.setText(questionText);

                    Log.d(TAG, "Attempting to insert question: " + questionText + " for quizId: " + quizId);

                    db.questionDao().insert(question);

                    Log.d(TAG, "Question inserted successfully.");
                    runOnUiThread(() -> Toast.makeText(this, "Question added", Toast.LENGTH_SHORT).show());
                } else {
                    // Edit existing question
                    Question question = db.questionDao().getQuestionById(questionId);
                    if (question != null) {
                        question.setText(questionText);
                        db.questionDao().updateQuestion(question);

                        Log.d(TAG, "Question updated successfully.");
                        runOnUiThread(() -> Toast.makeText(this, "Question updated", Toast.LENGTH_SHORT).show());
                    } else {
                        Log.e(TAG, "Question not found for updating with id: " + questionId);
                        runOnUiThread(() -> Toast.makeText(this, "Question not found", Toast.LENGTH_SHORT).show());
                    }
                }

                // Finish activity and return to the previous screen
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Failed to save question", e);
                runOnUiThread(() -> Toast.makeText(this, "Failed to save question", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
