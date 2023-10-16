package com.eou.screentalker.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Listeners.FriendListener;
import com.eou.screentalker.Listeners.UserListener;
import com.eou.screentalker.Models.FriendModel;
import com.eou.screentalker.databinding.ContainerUserBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>{

    private final List<FriendModel> friends;
    private final FriendListener friendListener;

    public  FriendsAdapter(List<FriendModel> friends, FriendListener friendListener){
            this.friends = friends;
            this.friendListener = friendListener;
            }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ContainerUserBinding containerUserBinding = ContainerUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new FriendViewHolder(containerUserBinding);
            }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
            holder.setUserData(friends.get(position));
            }

    @Override
    public int getItemCount() {
            return friends.size();
            }

    class FriendViewHolder extends RecyclerView.ViewHolder{
        ContainerUserBinding binding;
        FriendViewHolder(ContainerUserBinding containerUserBinding){
            super(containerUserBinding.getRoot());
            binding = containerUserBinding;
        }

        void setUserData(FriendModel friend){
            binding.textName.setText(friend.friend_username);
    //            binding.pImage.setImageURI(Uri.parse(friend.getpImage_url()));
            Picasso.get().load(Uri.parse(friend.friend_pImage)).into(binding.pImage);
            binding.getRoot().setOnClickListener(v-> friendListener.onFriendClicked(friend));
        }
    }
}
