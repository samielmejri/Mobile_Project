package com.example.user_module.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Program;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddEditProgramActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription;
    private Spinner spinnerProgramType;  // Change from EditText to Spinner
    private EditText editTextStartDate, editTextEndDate;
    private Button buttonSaveProgram;
    private int programId = -1;
    private long startDate = 0, endDate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_program);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerProgramType = findViewById(R.id.spinnerProgramType);  // Initialize Spinner
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        buttonSaveProgram = findViewById(R.id.buttonSaveProgram);

        programId = getIntent().getIntExtra("programId", -1);

        // Set up the Spinner with predefined program types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.program_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProgramType.setAdapter(adapter);

        // Load program data if editing
        if (programId != -1) {
            loadProgramData();
        }

        // Set up date pickers
        editTextStartDate.setOnClickListener(v -> showDatePickerDialog((date) -> {
            startDate = date;
            editTextStartDate.setText(formatDate(date));
        }));

        editTextEndDate.setOnClickListener(v -> showDatePickerDialog((date) -> {
            endDate = date;
            editTextEndDate.setText(formatDate(date));
        }));

        buttonSaveProgram.setOnClickListener(v -> saveProgram());
    }

    private void loadProgramData() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.programDao().getProgramById(programId).observe(this, program -> {
            if (program != null) {
                editTextName.setText(program.getName());
                editTextDescription.setText(program.getDescription());

                // Set the program type in the Spinner
                String type = program.getType();
                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerProgramType.getAdapter();
                int position = adapter.getPosition(type);
                spinnerProgramType.setSelection(position);

                // Set start and end dates
                startDate = program.getStartDate();
                endDate = program.getEndDate();
                editTextStartDate.setText(formatDate(startDate));
                editTextEndDate.setText(formatDate(endDate));
            }
        });
    }

    private void saveProgram() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String type = spinnerProgramType.getSelectedItem().toString();  // Get selected type from Spinner

        if (name.isEmpty() || description.isEmpty() || type.isEmpty() || startDate == 0 || endDate == 0) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Program program = new Program();
        program.setName(name);
        program.setDescription(description);
        program.setType(type);  // Set the type to the selected value
        program.setStartDate(startDate);
        program.setEndDate(endDate);

        if (programId != -1) {
            program.setId(programId);
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            if (programId == -1) {
                db.programDao().insert(program);
            } else {
                db.programDao().update(program);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Program saved", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        });
    }

    private void showDatePickerDialog(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    listener.onDateSelected(calendar.getTimeInMillis());
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return dateFormat.format(timestamp);
    }

    private interface OnDateSelectedListener {
        void onDateSelected(long date);
    }
}
