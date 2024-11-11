package com.example.user_module.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
            System.out.println("Number of questions retrieved: " + questions.size());
            runOnUiThread(() -> {
                questionsContainer.removeAllViews();
                if (questions.isEmpty()) {
                    Toast.makeText(this, "No questions found", Toast.LENGTH_SHORT).show();
                } else {
                    for (Question question : questions) {
                        // Create a container for the question text and menu button
                        LinearLayout questionLayout = new LinearLayout(this);
                        questionLayout.setOrientation(LinearLayout.HORIZONTAL);
                        questionLayout.setPadding(8, 8, 8, 8);

                        // Create TextView for question text
                        TextView questionTextView = new TextView(this);
                        questionTextView.setText(question.getText());
                        questionTextView.setTextSize(16);
                        questionTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

                        // Create Button for menu options
                        Button menuButton = new Button(this);
                        menuButton.setText("Options");
                        menuButton.setOnClickListener(v -> showQuestionOptions(question));

                        // Add TextView and Button to the layout
                        questionLayout.addView(questionTextView);
                        questionLayout.addView(menuButton);

                        // Add the question layout to the container
                        questionsContainer.addView(questionLayout);
                    }
                }
            });
        });
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
