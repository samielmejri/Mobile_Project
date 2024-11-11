package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.Adapters.TestAdapter;
import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Test;

import java.util.List;

public class TestListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTests;
    private Button buttonAddTest;
    private TestAdapter testAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        recyclerViewTests = findViewById(R.id.recyclerViewTests);
        buttonAddTest = findViewById(R.id.buttonAddTest);

        recyclerViewTests.setLayoutManager(new LinearLayoutManager(this));
        loadTests();

        buttonAddTest.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTestActivity.class);
            startActivity(intent);
        });
    }

    private void loadTests() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.testDao().getAllTests().observe(this, tests -> {
            testAdapter = new TestAdapter(this, tests);
            recyclerViewTests.setAdapter(testAdapter);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTests(); // Refresh the list when returning to this activity
    }
}
