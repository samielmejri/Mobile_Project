package com.example.user_module.Adapters;

import android.app.Activity;
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
import com.example.user_module.activity.CreatePostActivity;
import com.example.user_module.entity.Post;

import java.util.List;
import java.util.concurrent.Executors;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private static final int REQUEST_CODE_EDIT_POST = 2;

    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.textViewContent.setText(post.getContent());

        // Menu button click listener
        holder.buttonMenu.setOnClickListener(v -> showPopupMenu(v, post));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    private void showPopupMenu(View view, Post post) {
        androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(context, view);
        popupMenu.inflate(R.menu.menu_post_item);

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_edit) {
                editPost(post);
                return true;
            } else if (itemId == R.id.menu_delete) {
                deletePost(post);
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

    private void editPost(Post post) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        intent.putExtra("forumId", post.getForumId());
        intent.putExtra("postId", post.getId()); // Pass post ID to edit the specific post
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_EDIT_POST);
    }



    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContent;
        ImageButton buttonMenu;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            buttonMenu = itemView.findViewById(R.id.buttonMenu);
        }
    }


    private void deletePost(Post post) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.forumDao().deletePost(post);

            ((Activity) context).runOnUiThread(() -> {
                postList.remove(post);
                notifyDataSetChanged();
                Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show();
            });
        });
    }

}
