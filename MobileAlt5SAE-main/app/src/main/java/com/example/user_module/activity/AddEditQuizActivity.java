package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.Dao.QuizDao;
import com.example.user_module.entity.Quiz;

import java.util.concurrent.Executors;

public class AddEditQuizActivity extends AppCompatActivity {

    private EditText quizTitle, quizDescription;
    private Button saveQuizButton;
    private int quizId = -1; // Default to -1, indicating this is a new quiz by default
    private QuizDao quizDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_quiz);

        // Initialize views
        quizTitle = findViewById(R.id.quiz_title);
        quizDescription = findViewById(R.id.quiz_description);
        saveQuizButton = findViewById(R.id.save_quiz_button);

        // Get the instance of the QuizDao from AppDatabase
        quizDao = AppDatabase.getInstance(getApplicationContext()).quizDao();

        // Retrieve the quiz ID from the intent to determine if we're in add or edit mode
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("quizId")) {
            quizId = intent.getIntExtra("quizId", -1);
        }

        if (quizId != -1) {
            // Edit mode: load the existing quiz details
            loadQuizDetails();
        }

        // Set up the save button to either add or update a quiz
        saveQuizButton.setOnClickListener(v -> saveQuiz());
    }

    // Method to load quiz details if we're in edit mode
    private void loadQuizDetails() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Quiz quiz = quizDao.getQuizById(quizId);
            if (quiz != null) {
                runOnUiThread(() -> {
                    quizTitle.setText(quiz.getTitle());
                    quizDescription.setText(quiz.getDescription());
                });
            }
        });
    }

    // Method to save the quiz to the database
    private void saveQuiz() {
        String title = quizTitle.getText().toString().trim();
        String description = quizDescription.getText().toString().trim();

        // Validate inputs
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform the database operation on a background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            if (quizId == -1) {
                // Add new quiz
                Quiz newQuiz = new Quiz();
                newQuiz.setTitle(title);
                newQuiz.setDescription(description);
                quizDao.insertQuiz(newQuiz);
            } else {
                // Update existing quiz
                Quiz updatedQuiz = new Quiz();
                updatedQuiz.setId(quizId);
                updatedQuiz.setTitle(title);
                updatedQuiz.setDescription(description);
                quizDao.updateQuiz(updatedQuiz);
            }

            // Notify the user and return to the previous screen
            runOnUiThread(() -> {
                Toast.makeText(this, "Quiz saved successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        });
    }
}
