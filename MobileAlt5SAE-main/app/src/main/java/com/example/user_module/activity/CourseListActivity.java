package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.Adapters.CourseAdapter;
import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.entity.Course;

import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCourses;
    private Button buttonAddCourse;
    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        buttonAddCourse = findViewById(R.id.buttonAddCourse);

        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));
        loadCourses();

        buttonAddCourse.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditCourseActivity.class);
            startActivity(intent);
        });
    }

    private void loadCourses() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.courseDao().getAllCourses().observe(this, courses -> {
            courseAdapter = new CourseAdapter(this, courses);
            recyclerViewCourses.setAdapter(courseAdapter);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCourses(); // Refresh the list when returning to this activity
    }
}
