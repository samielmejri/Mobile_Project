package com.example.user_module.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.R;
import com.example.user_module.entity.Question;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final List<Question> questionList;
    private final Context context;
    private final QuestionClickListener listener;

    public interface QuestionClickListener {
        void onShowQuestion(Question question);
        void onEditQuestion(Question question);
        void onDeleteQuestion(Question question);
    }

    public QuestionAdapter(Context context, List<Question> questionList, QuestionClickListener listener) {
        this.context = context;
        this.questionList = questionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.questionText.setText(question.getText());
        holder.questionCategory.setText(question.getCategory());

        holder.menuButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.menuButton);
            popupMenu.inflate(R.menu.question_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_show) {
                    listener.onShowQuestion(question);
                    return true;
                } else if (item.getItemId() == R.id.action_edit) {
                    listener.onEditQuestion(question);
                    return true;
                } else if (item.getItemId() == R.id.action_delete) {
                    listener.onDeleteQuestion(question);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionText, questionCategory;
        ImageView menuButton;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.question_text);
            questionCategory = itemView.findViewById(R.id.question_category);
            menuButton = itemView.findViewById(R.id.menu_button);
        }
    }
}

