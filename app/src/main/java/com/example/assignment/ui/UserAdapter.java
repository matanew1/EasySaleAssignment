package com.example.assignment.ui;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.db.UserEntity;
import com.example.assignment.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying a list of users in a RecyclerView.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserEntity> users = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserEntity currentUser = users.get(position);
        holder.textViewName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        holder.textViewEmail.setText(currentUser.getEmail());
        try {
            ImageLoader.loadImage(holder.imageViewAvatar.getContext(), currentUser.getAvatar(), holder.imageViewAvatar);
        } catch (Exception e) {
            Log.e("Glide", "Error loading image: " + e.getMessage());
        }

        // Set click listener for the entire itemView
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null && position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUsers(List<UserEntity> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public UserEntity getUserAtPosition(int position) {
        return users.get(position);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewEmail;
        private final ImageView imageViewAvatar;

        public UserViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewEmail = itemView.findViewById(R.id.text_view_email);
            imageViewAvatar = itemView.findViewById(R.id.image_view_avatar);
        }
    }
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
