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
import com.example.user_module.activity.AddEditTestActivity;
import com.example.user_module.activity.TestListActivity;
import com.example.user_module.activity.ViewTestActivity;
import com.example.user_module.AppDatabase;
import com.example.user_module.entity.Test;

import java.util.List;
import java.util.concurrent.Executors;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private Context context;
    private List<Test> testList;

    public TestAdapter(Context context, List<Test> testList) {
        this.context = context;
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test test = testList.get(position);
        holder.textViewTitle.setText(test.getTitle());
        holder.textViewDate.setText(String.valueOf(test.getDate())); // Display date as needed

        // Set up the menu button with options
        holder.buttonMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.buttonMenu);
            popupMenu.inflate(R.menu.menu_test_item);

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_view) {
                    viewTest(test);
                    return true;
                } else if (item.getItemId() == R.id.menu_edit) {
                    editTest(test);
                    return true;
                } else if (item.getItemId() == R.id.menu_delete) {
                    deleteTest(test);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    private void viewTest(Test test) {
        Intent intent = new Intent(context, ViewTestActivity.class);
        intent.putExtra("testId", test.getId());
        context.startActivity(intent);
    }

    private void editTest(Test test) {
        Intent intent = new Intent(context, AddEditTestActivity.class);
        intent.putExtra("testId", test.getId());
        context.startActivity(intent);
    }

    private void deleteTest(Test test) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.testDao().delete(test);

            ((TestListActivity) context).runOnUiThread(() -> {
                testList.remove(test);
                notifyDataSetChanged();
                Toast.makeText(context, "Test deleted", Toast.LENGTH_SHORT).show();
            });
        });
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDate;
        ImageButton buttonMenu;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            buttonMenu = itemView.findViewById(R.id.buttonMenu);
        }
    }
}
