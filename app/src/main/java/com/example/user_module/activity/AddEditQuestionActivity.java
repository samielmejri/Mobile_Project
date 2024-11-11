package com.example.user_module.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.Dao.QuestionDao;
import com.example.user_module.entity.Question;

import java.util.concurrent.Executors;

public class AddEditQuestionActivity extends AppCompatActivity {

    private EditText questionText, questionCategory;
    private Button saveQuestionButton;
    private int quizId = -1;
    private int questionId = -1;
    private QuestionDao questionDao; // Declare the DAO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_question);

        // Initialize views
        questionText = findViewById(R.id.question_text);
        questionCategory = findViewById(R.id.question_category);
        saveQuestionButton = findViewById(R.id.save_question_button);

        // Initialize the DAO
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        questionDao = db.questionDao();

        // Get quizId and questionId from intent
        quizId = getIntent().getIntExtra("quiz_id", -1);
        questionId = getIntent().getIntExtra("question_id", -1);

        if (questionId != -1) {
            // Edit mode: load existing question details
            Executors.newSingleThreadExecutor().execute(() -> {
                Question question = questionDao.getQuestionById(questionId);
                if (question != null) {
                    runOnUiThread(() -> {
                        questionText.setText(question.getText());
                        questionCategory.setText(question.getCategory());
                    });
                }
            });
        }

        saveQuestionButton.setOnClickListener(v -> saveQuestion());
    }

    private void saveQuestion() {
        String text = questionText.getText().toString().trim();
        String category = questionCategory.getText().toString().trim();

        if (text.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            if (questionId == -1) {
                // Add new question
                Question newQuestion = new Question();
                newQuestion.setQuizId(quizId);
                newQuestion.setText(text);
                newQuestion.setCategory(category);
                questionDao.insert(newQuestion);
            } else {
                // Update existing question
                Question question = new Question();
                question.setId(questionId);
                question.setQuizId(quizId);
                question.setText(text);
                question.setCategory(category);
                questionDao.updateQuestion(question);
            }

            // Return to the previous activity
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        });
    }
}
