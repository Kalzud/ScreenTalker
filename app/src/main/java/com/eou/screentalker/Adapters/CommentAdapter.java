package com.eou.screentalker.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Activities.ProfileActivity;
import com.eou.screentalker.Models.CommentModel;
import com.eou.screentalker.databinding.ContainerCommentBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final List<CommentModel> comments;
    private static String pImage_url = null;
    private final String senderID;

    public CommentAdapter(List<CommentModel> comments, String senderID) {
        this.comments = comments;
        this.senderID = senderID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(ContainerCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CommentViewHolder) holder).setData(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static  class CommentViewHolder extends RecyclerView.ViewHolder{
        private final ContainerCommentBinding binding;

        CommentViewHolder(ContainerCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(CommentModel comment){
            binding.textMessage.setText(comment.message);
            binding.date.setText(comment.dateTime);
            Picasso.get().load(Uri.parse(comment.image)).into(binding.pImage);
            binding.pImage.setOnClickListener(v ->{
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("id", comment.senderID);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
