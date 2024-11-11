package com.example.user_module.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText questionEditText, option1EditText, option2EditText, option3EditText, option4EditText;
    private CheckBox option1CheckBox, option2CheckBox, option3CheckBox, option4CheckBox;
    private Button saveButton;
    private int quizId;
    private int questionId = -1; // Default for new questions, to be updated if editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        // Find views
        questionEditText = findViewById(R.id.questionEditText);
        option1EditText = findViewById(R.id.option1EditText);
        option2EditText = findViewById(R.id.option2EditText);
        option3EditText = findViewById(R.id.option3EditText);
        option4EditText = findViewById(R.id.option4EditText);

        option1CheckBox = findViewById(R.id.option1CheckBox);
        option2CheckBox = findViewById(R.id.option2CheckBox);
        option3CheckBox = findViewById(R.id.option3CheckBox);
        option4CheckBox = findViewById(R.id.option4CheckBox);

        saveButton = findViewById(R.id.saveButton);

        quizId = getIntent().getIntExtra("quizId", -1);
        questionId = getIntent().getIntExtra("questionId", -1);

        if (questionId != -1) {
            loadQuestionDetails();
        }

        saveButton.setOnClickListener(view -> saveQuestion());
    }

    private void loadQuestionDetails() {
        // Load the question if it's being edited
        new Thread(() -> {
            Question question = AppDatabase.getInstance(getApplicationContext()).questionDao().getQuestionById(questionId);
            if (question != null) {
                runOnUiThread(() -> {
                    questionEditText.setText(question.getText());
                    option1EditText.setText(question.getOption1());
                    option2EditText.setText(question.getOption2());
                    option3EditText.setText(question.getOption3());
                    option4EditText.setText(question.getOption4());

                    // Set the checkboxes based on correct answers
                    List<Integer> correctAnswers = question.getCorrectAnswers();
                    option1CheckBox.setChecked(correctAnswers.contains(1));
                    option2CheckBox.setChecked(correctAnswers.contains(2));
                    option3CheckBox.setChecked(correctAnswers.contains(3));
                    option4CheckBox.setChecked(correctAnswers.contains(4));
                });
            }
        }).start();
    }

    private void saveQuestion() {
        String questionText = questionEditText.getText().toString().trim();
        String option1 = option1EditText.getText().toString().trim();
        String option2 = option2EditText.getText().toString().trim();
        String option3 = option3EditText.getText().toString().trim();
        String option4 = option4EditText.getText().toString().trim();

        // Validate inputs
        if (questionText.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Integer> correctAnswers = new ArrayList<>();
        if (option1CheckBox.isChecked()) correctAnswers.add(1);
        if (option2CheckBox.isChecked()) correctAnswers.add(2);
        if (option3CheckBox.isChecked()) correctAnswers.add(3);
        if (option4CheckBox.isChecked()) correctAnswers.add(4);

        if (correctAnswers.isEmpty()) {
            Toast.makeText(this, "Please select at least one correct answer", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the new question object using the constructor that doesn't include ID for new questions
        Question newQuestion;
        if (questionId == -1) {
            newQuestion = new Question(questionText, option1, option2, option3, option4, correctAnswers, quizId);
        } else {
            // Create or update the question with the ID
            newQuestion = new Question(questionId, questionText, option1, option2, option3, option4, correctAnswers, quizId);
        }

        // Save the question in the database (insert or update)
        new Thread(() -> {
            if (questionId == -1) {
                AppDatabase.getInstance(getApplicationContext()).questionDao().insert(newQuestion);
            } else {
                AppDatabase.getInstance(getApplicationContext()).questionDao().updateQuestion(newQuestion);
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Question saved", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity and return to previous screen
            });
        }).start();
    }
}
