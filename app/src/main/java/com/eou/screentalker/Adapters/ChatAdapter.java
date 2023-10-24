package com.eou.screentalker.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Activities.ProfileActivity;
import com.eou.screentalker.Models.Chat_messageModel;
import com.eou.screentalker.databinding.ItemContainerReceivedMessageBinding;
import com.eou.screentalker.databinding.ItemContainerSentMessageBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<Chat_messageModel> chatMessages;
    private static String pImage_url = null;
    private final String senderID;

    public static  final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<Chat_messageModel> chatMessages, String pImage_url, String senderID) {
        this.chatMessages = chatMessages;
        this.pImage_url = pImage_url;
        this.senderID = senderID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT){
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,false
                    )
            );
        }else{
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        }else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), pImage_url);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderID.equals(senderID)){
            return VIEW_TYPE_SENT;
        }else {
            return  VIEW_TYPE_RECEIVED;
        }
    }

    static  class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Chat_messageModel chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.date.setText(chatMessage.dateTime);
        }
    }

    static  class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Chat_messageModel chatMessage, String pImage_url){
            binding.textMessage.setText(chatMessage.message);
            binding.date.setText(chatMessage.dateTime);
            Picasso.get().load(Uri.parse(pImage_url)).into(binding.pImage);
            binding.pImage.setOnClickListener(v ->{
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("id", chatMessage.senderID);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
