package com.example.user_module.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

        // Set the options
        holder.option1.setText(question.getOption1());
        holder.option2.setText(question.getOption2());
        holder.option3.setText(question.getOption3());
        holder.option4.setText(question.getOption4());

        // Assuming the question entity has a method to get correct answers (using a list of integers for example)
        List<Integer> correctAnswers = question.getCorrectAnswers();

        // Highlight correct options
        setOptionState(holder.option1, correctAnswers, 1);
        setOptionState(holder.option2, correctAnswers, 2);
        setOptionState(holder.option3, correctAnswers, 3);
        setOptionState(holder.option4, correctAnswers, 4);

        // Setup menu button click listener
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

    private void setOptionState(CheckBox option, List<Integer> correctAnswers, int optionNumber) {
        if (correctAnswers.contains(optionNumber)) {
            option.setChecked(true);
            option.setTextColor(Color.GREEN);  // Correct answer
        } else {
            option.setChecked(false);
            option.setTextColor(Color.BLACK);  // Incorrect answer
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionText, questionCategory;
        CheckBox option1, option2, option3, option4;
        ImageView menuButton;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.question_text);
            questionCategory = itemView.findViewById(R.id.question_category);
            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
            option3 = itemView.findViewById(R.id.option3);
            option4 = itemView.findViewById(R.id.option4);
            menuButton = itemView.findViewById(R.id.menu_button);
        }
    }
}
