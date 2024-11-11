package com.example.user_module.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.Dao.QuestionDao;
import com.example.user_module.entity.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AddEditQuestionActivity extends AppCompatActivity {

    private EditText questionText, questionCategory;
    private EditText option1EditText, option2EditText, option3EditText, option4EditText;
    private CheckBox checkboxOption1, checkboxOption2, checkboxOption3, checkboxOption4;
    private Button saveQuestionButton;
    private int quizId = -1;
    private int questionId = -1;
    private QuestionDao questionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_question);

        // Initialize views
        questionText = findViewById(R.id.question_text);
        questionCategory = findViewById(R.id.question_category);
        option1EditText = findViewById(R.id.option1);
        option2EditText = findViewById(R.id.option2);
        option3EditText = findViewById(R.id.option3);
        option4EditText = findViewById(R.id.option4);
        checkboxOption1 = findViewById(R.id.checkbox_option1);
        checkboxOption2 = findViewById(R.id.checkbox_option2);
        checkboxOption3 = findViewById(R.id.checkbox_option3);
        checkboxOption4 = findViewById(R.id.checkbox_option4);
        saveQuestionButton = findViewById(R.id.save_question_button);

        // Initialize the DAO
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        questionDao = db.questionDao();

        // Get quizId and questionId from intent
        quizId = getIntent().getIntExtra("quizid", -1);
        questionId = getIntent().getIntExtra("question_id", -1);

        if (questionId != -1) {
            // Edit mode: load existing question details
            Executors.newSingleThreadExecutor().execute(() -> {
                Question question = questionDao.getQuestionById(questionId);
                if (question != null) {
                    runOnUiThread(() -> {
                        questionText.setText(question.getText());
                        questionCategory.setText(question.getCategory());
                        option1EditText.setText(question.getOption1());
                        option2EditText.setText(question.getOption2());
                        option3EditText.setText(question.getOption3());
                        option4EditText.setText(question.getOption4());
                        checkboxOption1.setChecked(question.getCorrectAnswers().contains(1));
                        checkboxOption2.setChecked(question.getCorrectAnswers().contains(2));
                        checkboxOption3.setChecked(question.getCorrectAnswers().contains(3));
                        checkboxOption4.setChecked(question.getCorrectAnswers().contains(4));
                    });
                }
            });
        }

        saveQuestionButton.setOnClickListener(v -> saveQuestion());
    }

    private void saveQuestion() {
        String text = questionText.getText().toString().trim();
        String category = questionCategory.getText().toString().trim();
        String option1 = option1EditText.getText().toString().trim();
        String option2 = option2EditText.getText().toString().trim();
        String option3 = option3EditText.getText().toString().trim();
        String option4 = option4EditText.getText().toString().trim();

        if (text.isEmpty() || category.isEmpty() || option1.isEmpty() || option2.isEmpty() ||
                option3.isEmpty() || option4.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Collect correct answers
        List<Integer> correctAnswers = new ArrayList<>();
        if (checkboxOption1.isChecked()) correctAnswers.add(1);
        if (checkboxOption2.isChecked()) correctAnswers.add(2);
        if (checkboxOption3.isChecked()) correctAnswers.add(3);
        if (checkboxOption4.isChecked()) correctAnswers.add(4);

        Executors.newSingleThreadExecutor().execute(() -> {
            Question question;
            if (questionId == -1) {
                // Add new question (Room will automatically assign an ID)
                question = new Question(text, option1, option2, option3, option4, correctAnswers, quizId);
            } else {
                // Update existing question
                question = questionDao.getQuestionById(questionId);
                if (question == null) return; // If question doesn't exist, do nothing

                // Update question fields
                question.setQuizId(quizId);
                question.setText(text);
                question.setCategory(category);
                question.setOption1(option1);
                question.setOption2(option2);
                question.setOption3(option3);
                question.setOption4(option4);
                question.setCorrectAnswers(correctAnswers);
            }

            // Insert or update the question in the database
            if (questionId == -1) {
                questionDao.insert(question); // Insert new question
            } else {
                questionDao.updateQuestion(question); // Update existing question
            }

            // Return to the previous activity
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        });
    }
}
