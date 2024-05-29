package com.eou.screentalker.Adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Listeners.UserListener;
import com.eou.screentalker.Models.UserModel;

import com.eou.screentalker.R;
import com.eou.screentalker.databinding.ContainerUserBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private final List<UserModel> users;
    private final UserListener userListener;

    public  UsersAdapter(List<UserModel> users, UserListener userListener){
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContainerUserBinding containerUserBinding = ContainerUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new UserViewHolder(containerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class  UserViewHolder extends RecyclerView.ViewHolder{
        ContainerUserBinding binding;
        UserViewHolder(ContainerUserBinding containerUserBinding){
            super(containerUserBinding.getRoot());
            binding = containerUserBinding;
        }

        void setUserData(UserModel user){
            binding.textName.setText(user.getUsername());
            Picasso.get().load(Uri.parse(user.getpImage_url()))
                    .resize(200, 200)  // Target dimensions
                    .centerCrop()  // Maintain aspect ratio
                    .placeholder(R.drawable.image_placeholder)  // Placeholder while loading
                    .error(R.drawable.ic_baseline_image_not_supported_24)  // Image for loading errors
                    .config(Bitmap.Config.RGB_565)  // Reduce memory usage (optional)
                    .into(binding.pImage);
            binding.getRoot().setOnClickListener(v-> userListener.onUserClicked(user));
        }
    }
}
