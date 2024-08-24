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

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.db.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying a list of users in a RecyclerView.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    static List<UserEntity> users = new ArrayList<>();
    private static OnItemClickListener itemListener;
    private static OnDeleteClickListener deleteListener;

    public void setItemListener(OnItemClickListener itemListener) {
        UserAdapter.itemListener = itemListener;
    }

    public void setDeleteListener(OnDeleteClickListener deleteListener) {
        UserAdapter.deleteListener = deleteListener;
    }

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
            Glide.with(holder.imageViewAvatar.getContext())
                    .load(currentUser.getAvatar())
                    .into(holder.imageViewAvatar);
        } catch (Exception e) {
            Log.e("Glide", "Error loading image: "+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUsers(List<UserEntity> users) {
        UserAdapter.users = users;
        notifyDataSetChanged();
    }

    public UserEntity getUserAtPosition(int position) {
        return users.get(position);
    }

    // Make UserViewHolder static if it does not access outer class members
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewEmail;
        private final ImageView imageViewAvatar;
        private final ImageView imageViewDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewEmail = itemView.findViewById(R.id.text_view_email);
            imageViewAvatar = itemView.findViewById(R.id.image_view_avatar);
            imageViewDelete = itemView.findViewById(R.id.image_view_trash);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (itemListener != null && position != RecyclerView.NO_POSITION) {
                    itemListener.onItemClick(position);
                }
            });

            imageViewDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (deleteListener != null && position != RecyclerView.NO_POSITION) {
                    deleteListener.onDeleteClick(position);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

}
