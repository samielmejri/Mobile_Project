package com.example.user_module.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.user_module.AppDatabase;
import com.example.user_module.R;
import com.example.user_module.activity.ForumListActivity;
import com.example.user_module.activity.ViewForumActivity;
import com.example.user_module.entity.Forum;
import com.example.user_module.activity.AddEditForumActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {

    private Context context;
    private List<Forum> forumList;

    public ForumAdapter(Context context, List<Forum> forumList) {
        this.context = context;
        this.forumList = forumList;
    }

    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum, parent, false);
        return new ForumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        Forum forum = forumList.get(position);
        holder.textViewTitle.setText(forum.getName());
        holder.textViewDescription.setText(forum.getDescription());

        // Menu button click listener
        holder.buttonMenu.setOnClickListener(v -> {
            showPopupMenu(v, forum);
        });
    }

    @Override
    public int getItemCount() {
        return forumList.size();
    }

    private void showPopupMenu(View view, Forum forum) {
        androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(context, view);
        popupMenu.inflate(R.menu.menu_forum_item);

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.forum_menu_view) {
                viewForum(forum);
                return true;
            } else if (itemId == R.id.forum_menu_edit) {
                editForum(forum);
                return true;
            } else if (itemId == R.id.forum_menu_delete) {
                deleteForum(forum);
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    private void viewForum(Forum forum) {
        Intent intent = new Intent(context, ViewForumActivity.class);
        intent.putExtra("forumId", forum.getId()); // Pass the forum ID to ViewForumActivity
        context.startActivity(intent);
    }


    private void editForum(Forum forum) {
        Intent intent = new Intent(context, AddEditForumActivity.class);
        intent.putExtra("forumId", forum.getId()); // Pass the forum ID for editing
        context.startActivity(intent);
    }


    private void deleteForum(Forum forum) {
        // Show a confirmation dialog
        new AlertDialog.Builder(context)
                .setTitle("Delete Forum")
                .setMessage("Are you sure you want to delete this forum?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete in background thread if confirmed
                    Executors.newSingleThreadExecutor().execute(() -> {
                        AppDatabase db = AppDatabase.getInstance(context);
                        db.forumDao().delete(forum);

                        // Update the list on the main thread
                        ((ForumListActivity) context).runOnUiThread(() -> {
                            forumList.remove(forum);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Forum deleted", Toast.LENGTH_SHORT).show();
                        });
                    });
                })
                .setNegativeButton("No", null) // Do nothing on "No"
                .show();
    }



    public static class ForumViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDescription;
        ImageButton buttonMenu;

        public ForumViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            buttonMenu = itemView.findViewById(R.id.buttonMenu);
        }
    }
}
