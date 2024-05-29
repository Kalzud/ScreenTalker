package com.eou.screentalker.Adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Listeners.FriendListener;
import com.eou.screentalker.Listeners.UserListener;
import com.eou.screentalker.Models.FriendModel;
import com.eou.screentalker.R;
import com.eou.screentalker.databinding.ContainerUserBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>{

    private final List<FriendModel> friends;
    private final FriendListener friendListener;
    private int select = 1;

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
            Picasso.get().load(Uri.parse(friend.friend_pImage))
                    .resize(200, 200)  // Target dimensions
                    .centerCrop()  // Maintain aspect ratio
                    .placeholder(R.drawable.image_placeholder)  // Placeholder while loading
                    .error(R.drawable.ic_baseline_image_not_supported_24)  // Image for loading errors
                    .config(Bitmap.Config.RGB_565)  // Reduce memory usage (optional)
                    .into(binding.pImage);
            binding.getRoot().setOnClickListener(v-> {
                if (select == 1){
                    select = 2;
                    binding.tick.setVisibility(View.VISIBLE);
//                    System.out.println("2");
                }else{
                    select = 1;
                    binding.tick.setVisibility(View.GONE);
//                    System.out.println("1");
                }
                friendListener.onFriendClicked(friend);
            });
        }
    }
}
