package com.example.user_module.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;

public class ViewTestActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewDescription, textViewDate;
    private int testId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDate = findViewById(R.id.textViewDate);

        // Retrieve the test ID passed from the adapter
        testId = getIntent().getIntExtra("testId", -1);
        loadTestDetails();
    }

    private void loadTestDetails() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.testDao().getTestById(testId).observe(this, test -> {
            if (test != null) {
                textViewTitle.setText(test.getTitle());
                textViewDescription.setText(test.getDescription());
                textViewDate.setText(String.valueOf(test.getDate())); // Display the date as needed
            } else {
                Toast.makeText(this, "Test details not found", Toast.LENGTH_SHORT).show();
                finish(); // Close activity if test is not found
            }
        });
    }
}
