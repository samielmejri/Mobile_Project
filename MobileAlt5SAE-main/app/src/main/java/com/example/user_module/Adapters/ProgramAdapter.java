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
import com.example.user_module.activity.AddEditProgramActivity;
import com.example.user_module.activity.ProgramListActivity;
import com.example.user_module.activity.ViewProgramActivity;
import com.example.user_module.entity.Program;
import com.example.user_module.AppDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {

    private Context context;
    private List<Program> programList;
    private ActivityResultLauncher<Intent> editProgramLauncher;

    // Updated constructor to accept ActivityResultLauncher
    public ProgramAdapter(Context context, List<Program> programList, ActivityResultLauncher<Intent> editProgramLauncher) {
        this.context = context;
        this.programList = programList;
        this.editProgramLauncher = editProgramLauncher;
    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_program, parent, false);
        return new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        Program program = programList.get(position);
        holder.textViewName.setText(program.getName());
        holder.textViewDescription.setText(program.getDescription());

        // Set up the menu button with a popup menu
        holder.buttonMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.buttonMenu);
            popupMenu.inflate(R.menu.menu_program_item);

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_view) {
                    viewProgram(program);
                    return true;
                } else if (item.getItemId() == R.id.menu_edit) {
                    editProgram(program);
                    return true;
                } else if (item.getItemId() == R.id.menu_delete) {
                    deleteProgram(program);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

    private void viewProgram(Program program) {
        Intent intent = new Intent(context, ViewProgramActivity.class);
        intent.putExtra("programId", program.getId());
        context.startActivity(intent);
    }

    private void editProgram(Program program) {
        Intent intent = new Intent(context, AddEditProgramActivity.class);
        intent.putExtra("programId", program.getId());
        editProgramLauncher.launch(intent); // Use the launcher to edit
    }

    private void deleteProgram(Program program) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.programDao().delete(program);

            ((ProgramListActivity) context).runOnUiThread(() -> {
                programList.remove(program);
                notifyDataSetChanged();
                Toast.makeText(context, "Program deleted", Toast.LENGTH_SHORT).show();
            });
        });
    }

    public void setProgramList(List<Program> programList) {
        this.programList = programList;
    }

    public static class ProgramViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription;
        ImageButton buttonMenu;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            buttonMenu = itemView.findViewById(R.id.buttonMenu);
        }
    }
}
