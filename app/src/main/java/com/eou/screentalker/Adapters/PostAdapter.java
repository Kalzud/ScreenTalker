package com.eou.screentalker.Adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Models.PostModel;
import com.eou.screentalker.R;
import com.eou.screentalker.databinding.PostCardBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{
    private final List<PostModel> postModels;

    public PostAdapter(List<PostModel> communityModels) {
        this.postModels = communityModels;
    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostCardBinding postCardBinding = PostCardBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PostAdapter.MyViewHolder(postCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {
        holder.setPostData(postModels.get(position));
    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        PostCardBinding binding;
        MyViewHolder(PostCardBinding postCardBinding) {
            super(postCardBinding.getRoot());
            binding = postCardBinding;
        }

        void setPostData(PostModel post){
            binding.tag.setText(post.tag);
            Picasso.get().load(Uri.parse(post.post))
                    .resize(200, 200)  // Target dimensions
                    .centerCrop()  // Maintain aspect ratio
                    .placeholder(R.drawable.image_placeholder)  // Placeholder while loading
                    .error(R.drawable.ic_baseline_image_not_supported_24)  // Image for loading errors
                    .config(Bitmap.Config.RGB_565)  // Reduce memory usage (optional)
                    .into(binding.post);
        }

    }
}
