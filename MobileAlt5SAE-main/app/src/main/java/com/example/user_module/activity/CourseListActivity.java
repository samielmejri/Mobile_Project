package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.Adapters.CourseAdapter;
import com.example.user_module.AppDatabase;
import com.example.user_module.R;

public class CourseListActivity extends BaseActivity {

    private RecyclerView recyclerViewCourses;
    private Button buttonAddCourse;
    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this activity into the content frame
        getLayoutInflater().inflate(R.layout.activity_course_list, findViewById(R.id.content_frame));
        setTitle("Courses"); // Set the title for the toolbar

        // Initialize your views as before
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
