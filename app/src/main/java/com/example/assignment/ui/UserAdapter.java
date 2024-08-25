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
    static List<UserEntity> users = new ArrayList<>();
    private static OnDeleteClickListener deleteListener;


    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView);
    }

    /**
     * Updates the contents of the ViewHolder's itemView to reflect the item at the given
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserEntity currentUser = users.get(position);
        holder.textViewName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        holder.textViewEmail.setText(currentUser.getEmail());
        try {
            ImageLoader.loadImage(holder.imageViewAvatar.getContext(), currentUser.getAvatar(), holder.imageViewAvatar);
        } catch (Exception e) {
            Log.e("Glide", "Error loading image: "+e.getMessage());
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * Sets the list of users to be displayed in the adapter.
     * @param users The list of users to be displayed.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setUsers(List<UserEntity> users) {
        UserAdapter.users = users;
        notifyDataSetChanged();
    }

    /**
     * Removes a user from the list of users
     * @param position The position of the user to be removed
     * @return The removed user
     */
    public UserEntity getUserAtPosition(int position) {
        return users.get(position);
    }


    /**
     * ViewHolder for displaying a user in a RecyclerView.
     */
    @SuppressWarnings("FieldCanBeLocal")
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewEmail;
        private final ImageView imageViewAvatar;
        private final ImageView imageViewDelete;

        /**
         * Constructor for UserViewHolder.
         * @param itemView The View in which to display the data.
         */
        public UserViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewEmail = itemView.findViewById(R.id.text_view_email);
            imageViewAvatar = itemView.findViewById(R.id.image_view_avatar);
            imageViewDelete = itemView.findViewById(R.id.image_view_trash);

            // Set click listener for the delete button
            imageViewDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (deleteListener != null && position != RecyclerView.NO_POSITION) {
                    deleteListener.onDeleteClick(position);
                }
            });
        }
    }

    /**
     * Sets the listener for the delete button click.
     * @param deleteListener The listener to set.
     */
    public void setDeleteListener(OnDeleteClickListener deleteListener) {
        UserAdapter.deleteListener = deleteListener;
    }


    /**
     * Interface for handling delete button clicks.
     */
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

}
