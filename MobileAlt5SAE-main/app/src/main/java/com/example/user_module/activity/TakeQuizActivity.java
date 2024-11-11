package com.example.user_module.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.Dao.QuizDao;
import com.example.user_module.R;
import com.example.user_module.entity.Question;

import java.util.List;

public class TakeQuizActivity extends AppCompatActivity {
    private TextView questionTextView;
    private CheckBox option1, option2, option3, option4;
    private Button nextButton;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<Question> questions;
    private String quizTitle;
    private QuizDao quizDao;
    private int quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);

        quizId = getIntent().getIntExtra("quizId", -1);
        if (quizId == -1) {
            Toast.makeText(this, "Quiz ID not passed correctly", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        quizDao = AppDatabase.getInstance(this).quizDao();

        // Fetch questions asynchronously
        new GetQuestionsTask().execute(quizId);

        nextButton.setOnClickListener(v -> {
            checkAnswer();
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                displayQuestion(currentQuestionIndex);
            } else {
                submitQuiz();
            }
        });
    }

    // AsyncTask to fetch questions in the background
    private class GetQuestionsTask extends AsyncTask<Integer, Void, List<Question>> {
        @Override
        protected List<Question> doInBackground(Integer... params) {
            // Query the database to get questions for the quiz
            return quizDao.getQuestionsForQuiz(params[0]);
        }

        @Override
        protected void onPostExecute(List<Question> result) {
            // Set the fetched questions to the local list and display the first question
            questions = result;
            if (questions != null && !questions.isEmpty()) {
                displayQuestion(currentQuestionIndex);
            } else {
                Toast.makeText(TakeQuizActivity.this, "No questions found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayQuestion(int index) {
        if (questions != null && !questions.isEmpty()) {
            Question question = questions.get(index);
            questionTextView.setText(question.getText());
            option1.setText(question.getOption1());
            option2.setText(question.getOption2());
            option3.setText(question.getOption3());
            option4.setText(question.getOption4());

            // Clear the options
            option1.setChecked(false);
            option2.setChecked(false);
            option3.setChecked(false);
            option4.setChecked(false);
        }
    }

    private void checkAnswer() {
        if (questions == null || questions.isEmpty()) return;

        Question question = questions.get(currentQuestionIndex);

        // Get the list of correct answers for this question
        List<Integer> correctAnswers = question.getCorrectAnswers();

        // Check if the user selected the exact correct answers
        boolean isCorrect = true;

        // Check selected options
        if (option1.isChecked() && !correctAnswers.contains(1)) {
            isCorrect = false;
        }
        if (option2.isChecked() && !correctAnswers.contains(2)) {
            isCorrect = false;
        }
        if (option3.isChecked() && !correctAnswers.contains(3)) {
            isCorrect = false;
        }
        if (option4.isChecked() && !correctAnswers.contains(4)) {
            isCorrect = false;
        }

        // Ensure that all the correct answers are selected, and no extra answers are selected
        int selectedCount = 0;
        if (option1.isChecked()) selectedCount++;
        if (option2.isChecked()) selectedCount++;
        if (option3.isChecked()) selectedCount++;
        if (option4.isChecked()) selectedCount++;

        // If there are extra answers selected or missing, it's considered incorrect
        if (selectedCount != correctAnswers.size()) {
            isCorrect = false;
        }

        // Increment the score if the answer is correct
        if (isCorrect) {
            score++; // Increment score for correct answer
        }
    }

    private void submitQuiz() {
        // Check if the user has a perfect score
        if (score == questions.size()) {
            // Only show the Certificate and motivational message if the score is perfect
            Toast.makeText(this, "Quiz submitted! Perfect Score!", Toast.LENGTH_SHORT).show();

            // Generate a motivational message for a perfect score
            String motivationalMessage = generateMotivationalMessage();

            // Navigate to the Certificate screen with the score, quiz title, and motivational message
            Intent intent = new Intent(this, CertificateActivity.class);
            intent.putExtra("quizTitle", quizTitle);
            intent.putExtra("score", score);
            intent.putExtra("motivationalMessage", motivationalMessage);
            startActivity(intent);
        } else {
            // If the score is not perfect, just show a generic message
            Toast.makeText(this, "Quiz submitted! Keep practicing!", Toast.LENGTH_SHORT).show();

            // You can also redirect to a different screen or show a different message if needed
            // For example, show a results screen or summary
            Intent intent = new Intent(this, QuizResultsActivity.class); // Replace with your results screen
            intent.putExtra("score", score);
            intent.putExtra("quizTitle", quizTitle);
            startActivity(intent);
        }
    }

    private String generateMotivationalMessage() {
        if (score == questions.size()) {
            return "Outstanding! You're a true champion!";
        } else if (score >= questions.size() * 0.75) {
            return "Great job! Keep up the good work!";
        } else if (score >= questions.size() * 0.5) {
            return "Good effort! You're on the right track!";
        } else {
            return "Keep going! Every step brings you closer!";
        }
    }
}
