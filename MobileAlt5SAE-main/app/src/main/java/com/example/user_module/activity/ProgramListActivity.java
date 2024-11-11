package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.Adapters.ProgramAdapter;
import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Program;

import java.util.Calendar;
import java.util.List;

public class ProgramListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPrograms;
    private Button buttonAddProgram;
    private ProgramAdapter programAdapter;
    private CalendarView calendarView;

    // Define ActivityResultLauncher for adding/editing programs
    private final ActivityResultLauncher<Intent> addEditProgramLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadPrograms(); // Reload the list to reflect changes after add/edit
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_list);

        recyclerViewPrograms = findViewById(R.id.recyclerViewPrograms);
        buttonAddProgram = findViewById(R.id.buttonAddProgram);
        calendarView = findViewById(R.id.calendarView);

        recyclerViewPrograms.setLayoutManager(new LinearLayoutManager(this));
        loadPrograms(); // Initial load of all programs

        // Set up CalendarView to filter programs by selected date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            long selectedTimestamp = selectedDate.getTimeInMillis();
            loadProgramsByDate(selectedTimestamp); // Load programs for the selected date
        });

        buttonAddProgram.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditProgramActivity.class);
            addEditProgramLauncher.launch(intent); // Launch AddEditProgramActivity for adding a program
        });
    }

    private void loadPrograms() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.programDao().getAllPrograms().observe(this, programs -> {
            if (programAdapter == null) {
                // Pass the addEditProgramLauncher for handling edits
                programAdapter = new ProgramAdapter(this, programs, addEditProgramLauncher);
                recyclerViewPrograms.setAdapter(programAdapter);
            } else {
                // Update the existing adapter data if already initialized
                programAdapter.setProgramList(programs);
                programAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadProgramsByDate(long date) {
        AppDatabase db = AppDatabase.getInstance(this);
        db.programDao().getProgramsByDate(date).observe(this, programs -> {
            if (programAdapter == null) {
                programAdapter = new ProgramAdapter(this, programs, addEditProgramLauncher);
                recyclerViewPrograms.setAdapter(programAdapter);
            } else {
                programAdapter.setProgramList(programs);
                programAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPrograms(); // Refresh the list when returning to this activity
    }
}
