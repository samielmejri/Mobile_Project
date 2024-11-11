package com.example.user_module.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Question;
import com.example.user_module.entity.Quiz;

import java.util.List;
import java.util.concurrent.Executors;

public class ViewQuizActivity extends AppCompatActivity {

    private TextView quizTitleTextView, quizDescriptionTextView;
    private LinearLayout questionsContainer;
    private Button addQuestionButton;
    private int quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quiz);

        quizTitleTextView = findViewById(R.id.quizTitleTextView);
        quizDescriptionTextView = findViewById(R.id.quizDescriptionTextView);
        questionsContainer = findViewById(R.id.questionsContainer);
        addQuestionButton = findViewById(R.id.addQuestionButton);

        quizId = getIntent().getIntExtra("quizId", -1);

        if (quizId != -1) {
            loadQuizDetails();
            loadQuestions();
        } else {
            Toast.makeText(this, "Invalid quiz ID", Toast.LENGTH_SHORT).show();
        }

        addQuestionButton.setOnClickListener(view -> {
            Intent intent = new Intent(ViewQuizActivity.this, AddQuestionActivity.class);
            intent.putExtra("quizId", quizId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload questions when returning from AddQuestionActivity
        loadQuestions();
    }

    private void loadQuizDetails() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Quiz quiz = AppDatabase.getInstance(getApplicationContext()).quizDao().getQuizById(quizId);
            if (quiz != null) {
                runOnUiThread(() -> {
                    quizTitleTextView.setText(quiz.getTitle());
                    quizDescriptionTextView.setText(quiz.getDescription());
                });
            }
        });
    }

    private void loadQuestions() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Question> questions = AppDatabase.getInstance(getApplicationContext()).questionDao().getQuestionsByQuizId(quizId);
            runOnUiThread(() -> {
                questionsContainer.removeAllViews();
                if (questions.isEmpty()) {
                    Toast.makeText(this, "No questions found", Toast.LENGTH_SHORT).show();
                } else {
                    for (Question question : questions) {
                        // Create a container for the question and options
                        LinearLayout questionLayout = new LinearLayout(this);
                        questionLayout.setOrientation(LinearLayout.VERTICAL);
                        questionLayout.setPadding(8, 8, 8, 8);

                        // Create TextView for the question text
                        TextView questionTextView = new TextView(this);
                        questionTextView.setText(question.getText());
                        questionTextView.setTextSize(16);
                        questionTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        // Create CheckBoxes for the options
                        CheckBox option1CheckBox = new CheckBox(this);
                        option1CheckBox.setText(question.getOption1());
                        CheckBox option2CheckBox = new CheckBox(this);
                        option2CheckBox.setText(question.getOption2());
                        CheckBox option3CheckBox = new CheckBox(this);
                        option3CheckBox.setText(question.getOption3());
                        CheckBox option4CheckBox = new CheckBox(this);
                        option4CheckBox.setText(question.getOption4());

                        // Set the correct answers by checking the correct options
                        List<Integer> correctAnswers = question.getCorrectAnswers();
                        highlightCorrectAnswer(option1CheckBox, correctAnswers, 1);
                        highlightCorrectAnswer(option2CheckBox, correctAnswers, 2);
                        highlightCorrectAnswer(option3CheckBox, correctAnswers, 3);
                        highlightCorrectAnswer(option4CheckBox, correctAnswers, 4);

                        // Add options to the layout
                        questionLayout.addView(questionTextView);
                        questionLayout.addView(option1CheckBox);
                        questionLayout.addView(option2CheckBox);
                        questionLayout.addView(option3CheckBox);
                        questionLayout.addView(option4CheckBox);

                        // Add the question layout to the container
                        questionsContainer.addView(questionLayout);
                    }
                }
            });
        });
    }

    // This is your updated highlightCorrectAnswer method
    private void highlightCorrectAnswer(CheckBox optionCheckBox, List<Integer> correctAnswers, int optionNumber) {
        if (correctAnswers != null && correctAnswers.contains(optionNumber)) {
            optionCheckBox.setChecked(true);
            optionCheckBox.setTextColor(getResources().getColor(android.R.color.holo_green_dark)); // Highlight correct answer
        } else {
            optionCheckBox.setChecked(false);
            optionCheckBox.setTextColor(getResources().getColor(android.R.color.black)); // Default color for incorrect
        }

        // Optionally disable the checkboxes
        optionCheckBox.setEnabled(false);
    }

    private void showQuestionOptions(Question question) {
        // Show options in an AlertDialog with if-else conditions for menu actions
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Question Options");
        builder.setItems(new CharSequence[]{"View", "Edit", "Delete"}, (dialog, which) -> {
            if (which == 0) {
                // View question details
                viewQuestionDetails(question);
            } else if (which == 1) {
                // Edit question
                editQuestion(question);
            } else if (which == 2) {
                // Delete question
                confirmDeleteQuestion(question);
            }
        });
        builder.show();
    }

    private void viewQuestionDetails(Question question) {
        // Display question details in a dialog
        new AlertDialog.Builder(this)
                .setTitle("Question Details")
                .setMessage(question.getText())
                .setPositiveButton("OK", null)
                .show();
    }

    private void editQuestion(Question question) {
        // Start AddQuestionActivity with quizId and questionId for editing
        Intent intent = new Intent(this, AddQuestionActivity.class);
        intent.putExtra("quizId", quizId);
        intent.putExtra("questionId", question.getId());
        startActivity(intent);
    }

    private void confirmDeleteQuestion(Question question) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Question")
                .setMessage("Are you sure you want to delete this question?")
                .setPositiveButton("Yes", (dialog, which) -> deleteQuestion(question))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteQuestion(Question question) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase.getInstance(getApplicationContext()).questionDao().deleteQuestion(question);
            runOnUiThread(() -> {
                Toast.makeText(this, "Question deleted", Toast.LENGTH_SHORT).show();
                loadQuestions(); // Refresh the list after deletion
            });
        });
    }
}
