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
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.R;
import com.example.user_module.activity.AddEditQuizActivity;
import com.example.user_module.activity.QuizListActivity;
import com.example.user_module.activity.ViewQuizActivity;
import com.example.user_module.entity.Quiz;
import com.example.user_module.AppDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private Context context;
    private List<Quiz> quizList;
    private ActivityResultLauncher<Intent> editQuizLauncher;

    public QuizAdapter(Context context, List<Quiz> quizList, ActivityResultLauncher<Intent> editQuizLauncher) {
        this.context = context;
        this.quizList = quizList;
        this.editQuizLauncher = editQuizLauncher;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.quizTitle.setText(quiz.getTitle());
        holder.quizDescription.setText(quiz.getDescription());

        // Set up the menu button with a popup menu
        holder.buttonMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.buttonMenu);
            popupMenu.inflate(R.menu.menu_quiz_item);

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_view) {
                    viewQuiz(quiz);
                    return true;
                } else if (item.getItemId() == R.id.menu_edit) {
                    editQuiz(quiz);
                    return true;
                } else if (item.getItemId() == R.id.menu_delete) {
                    deleteQuiz(quiz);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return quizList != null ? quizList.size() : 0;
    }

    private void viewQuiz(Quiz quiz) {
        Intent intent = new Intent(context, ViewQuizActivity.class);
        intent.putExtra("quizId", quiz.getId());
        context.startActivity(intent);
    }

    private void editQuiz(Quiz quiz) {
        Intent intent = new Intent(context, AddEditQuizActivity.class);
        intent.putExtra("quizId", quiz.getId());
        editQuizLauncher.launch(intent); // Use the launcher to edit
    }

    private void deleteQuiz(Quiz quiz) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.quizDao().delete(quiz);

            ((QuizListActivity) context).runOnUiThread(() -> {
                quizList.remove(quiz);
                notifyDataSetChanged();
                Toast.makeText(context, "Quiz deleted", Toast.LENGTH_SHORT).show();
            });
        });
    }

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quizTitle, quizDescription;
        ImageButton buttonMenu;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitle = itemView.findViewById(R.id.quizTitle);
            quizDescription = itemView.findViewById(R.id.quizDescription);
            buttonMenu = itemView.findViewById(R.id.buttonMenu);
        }
    }
}
