package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Quiz;

import java.util.List;
import java.util.concurrent.Executors;
import android.util.Log;
import android.view.Gravity;
import android.widget.PopupMenu;

public class QuizListActivity extends BaseActivity {

    private LinearLayout quizListContainer;
    private Button buttonAddQuiz;

    // Register an ActivityResultLauncher for add/edit quiz activity
    private final ActivityResultLauncher<Intent> addEditQuizLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadQuizzes();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this activity within the BaseActivity content frame
        getLayoutInflater().inflate(R.layout.activity_quiz_list, findViewById(R.id.content_frame));
        setTitle("Quizzes"); // Set title for the toolbar

        quizListContainer = findViewById(R.id.quizListContainer);
        buttonAddQuiz = findViewById(R.id.buttonAddQuiz);

        // Load quizzes initially
        loadQuizzes();

        // Set listener to open AddEditQuizActivity for adding a quiz
        buttonAddQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditQuizActivity.class);
            addEditQuizLauncher.launch(intent);
        });
    }

    private void loadQuizzes() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.quizDao().getAllQuizzes().observe(this, quizzes -> {
            if (quizzes != null) {
                displayQuizzes(quizzes);
            }
        });
    }

    private void displayQuizzes(List<Quiz> quizzes) {
        quizListContainer.removeAllViews();

        for (Quiz quiz : quizzes) {
            LinearLayout quizItemLayout = new LinearLayout(this);
            quizItemLayout.setOrientation(LinearLayout.HORIZONTAL);
            quizItemLayout.setPadding(16, 16, 16, 16);

            TextView quizView = new TextView(this);
            quizView.setText(quiz.getTitle());
            quizView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            // Create the menu button
            Button menuButton = new Button(this);
            menuButton.setText("⋮");  // Vertical ellipsis as menu icon
            menuButton.setOnClickListener(v -> showPopupMenu(menuButton, quiz));

            // Add TextView and menu button to the item layout
            quizItemLayout.addView(quizView);
            quizItemLayout.addView(menuButton);

            // Add the item layout to the main container
            quizListContainer.addView(quizItemLayout);
        }
    }

    private void showPopupMenu(View view, Quiz quiz) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_quiz_item, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_view) {
                viewQuiz(quiz);
                return true;
            } else if (item.getItemId() == R.id.menu_edit) {
                editQuiz(quiz);
                return true;
            } else if (item.getItemId() == R.id.menu_delete) {
                deleteQuiz(quiz);
                return true;
            } else if (item.getItemId() == R.id.menu_pass_quiz) {
                Log.d("QuizActivity", "Pass Quiz clicked"); // Log when Pass Quiz is clicked
                passQuiz(quiz); // Handle passing the quiz
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

    private void passQuiz(Quiz quiz) {
        Log.d("QuizActivity", "Starting Pass Quiz for quiz ID: " + quiz.getId());
        Intent intent = new Intent(this, TakeQuizActivity.class);
        intent.putExtra("quizId", quiz.getId()); // Pass the quiz ID to the activity
        startActivity(intent);
    }

    private void viewQuiz(Quiz quiz) {
        Intent intent = new Intent(this, ViewQuizActivity.class);
        intent.putExtra("quizId", quiz.getId());
        startActivity(intent);
    }

    private void editQuiz(Quiz quiz) {
        Intent intent = new Intent(this, AddEditQuizActivity.class);
        intent.putExtra("quizId", quiz.getId());
        addEditQuizLauncher.launch(intent);
    }

    private void deleteQuiz(Quiz quiz) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            db.quizDao().delete(quiz);

            runOnUiThread(() -> {
                Toast.makeText(this, "Quiz deleted", Toast.LENGTH_SHORT).show();
                loadQuizzes(); // Refresh list after deletion
            });
        });
    }
}
