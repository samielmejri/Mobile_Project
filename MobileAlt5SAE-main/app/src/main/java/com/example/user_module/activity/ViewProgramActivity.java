package com.example.user_module.activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Program;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewProgramActivity extends AppCompatActivity {

    private TextView textViewName, textViewDescription, textViewType, textViewDate;
    private int programId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_program);

        textViewName = findViewById(R.id.textViewName);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewType = findViewById(R.id.textViewType);
        textViewDate = findViewById(R.id.textViewDate);

        // Retrieve the program ID passed from ProgramAdapter
        programId = getIntent().getIntExtra("programId", -1);
        loadProgramDetails();
    }

    private void loadProgramDetails() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.programDao().getProgramById(programId).observe(this, program -> {
            if (program != null) {
                textViewName.setText(program.getName());
                textViewDescription.setText(program.getDescription());
                textViewType.setText(program.getType());

                // Format and display start and end dates
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                String formattedStartDate = dateFormat.format(new Date(program.getStartDate()));
                String formattedEndDate = dateFormat.format(new Date(program.getEndDate()));

                textViewDate.setText("Start: " + formattedStartDate + "\nEnd: " + formattedEndDate);
            }
        });
    }
}
