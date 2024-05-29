package com.eou.screentalker.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Activities.ProfileActivity;
import com.eou.screentalker.Models.Group_chat_messageModel;
import com.eou.screentalker.R;
import com.eou.screentalker.databinding.ContainerGroupReceivedMessageBinding;
import com.eou.screentalker.databinding.ContainerGroupSentMessageBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Group_chatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final List<Group_chat_messageModel> gChatMessages;
    private static String pImage_url = " ";
    private final String senderID;
    private static String username = "";

    public static  final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public Group_chatAdapter(List<Group_chat_messageModel> gChatMessages,String senderID) {
        this.gChatMessages = gChatMessages;
        this.senderID = senderID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT){
            return new Group_chatAdapter.SentGroupMessageViewHolder(
                    ContainerGroupSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,false
                    )
            );
        }else{
            return new Group_chatAdapter.ReceivedGroupMessageViewHolder(
                    ContainerGroupReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT) {
            ((Group_chatAdapter.SentGroupMessageViewHolder) holder).setData(gChatMessages.get(position));
        }else {
            ((Group_chatAdapter.ReceivedGroupMessageViewHolder) holder).setData(gChatMessages.get(position), pImage_url);
        }
    }

    @Override
    public int getItemCount() {
        return gChatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(gChatMessages.get(position).senderID.equals(senderID)){
            return VIEW_TYPE_SENT;
        }else {
            return  VIEW_TYPE_RECEIVED;
        }
    }

    static  class SentGroupMessageViewHolder extends RecyclerView.ViewHolder{
        private final ContainerGroupSentMessageBinding binding;

        SentGroupMessageViewHolder(ContainerGroupSentMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Group_chat_messageModel gChatMessage){
            binding.textMessage.setText(gChatMessage.message);
            binding.date.setText(gChatMessage.dateTime);
            binding.username.setText(username);
        }
    }

    static  class ReceivedGroupMessageViewHolder extends RecyclerView.ViewHolder{
        private final ContainerGroupReceivedMessageBinding binding;

        ReceivedGroupMessageViewHolder(ContainerGroupReceivedMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Group_chat_messageModel gChatMessage, String pImage_url){
            binding.textMessage.setText(gChatMessage.message);
            binding.date.setText(gChatMessage.dateTime);
            binding.username.setText(gChatMessage.username);
            System.out.println("thitrd checking: " + pImage_url);
            Picasso.get().load(Uri.parse(gChatMessage.image))
                    .resize(200, 200)  // Target dimensions
                    .centerCrop()  // Maintain aspect ratio
                    .placeholder(R.drawable.image_placeholder)  // Placeholder while loading
                    .error(R.drawable.ic_baseline_image_not_supported_24)  // Image for loading errors
                    .config(Bitmap.Config.RGB_565)  // Reduce memory usage (optional)
                    .into(binding.pImage);
            binding.pImage.setOnClickListener(v ->{
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("id", gChatMessage.senderID);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
