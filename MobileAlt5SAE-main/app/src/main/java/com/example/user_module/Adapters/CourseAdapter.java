package com.example.user_module.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.R;
import com.example.user_module.activity.AddEditCourseActivity;
import com.example.user_module.activity.CourseListActivity;
import com.example.user_module.activity.ViewCourseActivity;
import com.example.user_module.AppDatabase;
import com.example.user_module.entity.Course;

import java.util.List;
import java.util.concurrent.Executors;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;
    private List<Course> courseList;

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.textViewTitle.setText(course.getTitle());
        holder.textViewCategory.setText(course.getCategory());

        // Set up the menu button with options
        holder.buttonMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.buttonMenu);
            popupMenu.inflate(R.menu.menu_course_item);

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_view) {
                    viewCourse(course);
                    return true;
                } else if (item.getItemId() == R.id.menu_edit) {
                    editCourse(course);
                    return true;
                } else if (item.getItemId() == R.id.menu_delete) {
                    deleteCourse(course);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    private void viewCourse(Course course) {
        Intent intent = new Intent(context, ViewCourseActivity.class);
        intent.putExtra("courseId", course.getId());
        context.startActivity(intent);
    }

    private void editCourse(Course course) {
        Intent intent = new Intent(context, AddEditCourseActivity.class);
        intent.putExtra("courseId", course.getId());
        context.startActivity(intent);
    }

    private void deleteCourse(Course course) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.courseDao().delete(course);

            ((CourseListActivity) context).runOnUiThread(() -> {
                courseList.remove(course);
                notifyDataSetChanged();
                Toast.makeText(context, "Course deleted", Toast.LENGTH_SHORT).show();
            });
        });
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewCategory;
        ImageButton buttonMenu;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            buttonMenu = itemView.findViewById(R.id.buttonMenu);
        }
    }
}
