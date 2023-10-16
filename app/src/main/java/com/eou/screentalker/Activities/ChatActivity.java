package com.eou.screentalker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.eou.screentalker.Adapters.ChatAdapter;
import com.eou.screentalker.Models.Chat_messageModel;
import com.eou.screentalker.Models.FriendModel;
import com.eou.screentalker.Models.UserModel;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.ActivityChatBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private FriendModel receiverFriend;
    private List<Chat_messageModel> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadReceiverDetails();
        init();
        listenMesssages();
        binding.imageBack.setOnClickListener(v-> onBackPressed());
        binding.layoutSend.setOnClickListener(v-> sendMessage());
    }

    private  void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages, receiverFriend.friend_pImage, preferenceManager.getString(Constants.KEY_USER_ID));
        binding.chatRecyclerView.setAdapter(chatAdapter);
        fStore = FirebaseFirestore.getInstance();
    }

    private void  loadReceiverDetails(){
        receiverFriend = (FriendModel) getIntent().getSerializableExtra("friend");
        binding.textName.setText(receiverFriend.friend_username);
//        System.out.println(receiverFriend.getUsername());
    }

    private  void sendMessage(){
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverFriend.friend_id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        fStore.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        binding.inputMessage.setText(null);
    }

    public void listenMesssages(){
        fStore.collection(Constants.KEY_COLLECTION_CHAT)
                //get where the sender id is the id of current user
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                //where the receiver id is same as the id of receiver or user currently talking to
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverFriend.friend_id)
                .addSnapshotListener(eventListener);
        fStore.collection(Constants.KEY_COLLECTION_CHAT)
                //get where the sender id is the id of receiver user
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverFriend.friend_id)
                //where the receiver id is same as the id of current user
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private String getReadableDataTime(Date date){
        return  new SimpleDateFormat("mmmm dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return;
        }
        if (value != null){
            int count = chatMessages.size();
            for(DocumentChange documentChange: value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    Chat_messageModel chatMessage = new Chat_messageModel();
                    chatMessage.senderID = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverID = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDataTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0){
                chatAdapter.notifyDataSetChanged();
            }else{
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() -1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
    };
}

















































//public class ChatActivity extends AppCompatActivity {
//    private ActivityChatBinding binding;
//    private UserModel receiverFriend;
//    private List<Chat_messageModel> chatMessages;
//    private ChatAdapter chatAdapter;
//    private PreferenceManager preferenceManager;
//    private FirebaseFirestore fStore;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityChatBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        loadReceiverDetails();
//        init();
//        listenMesssages();
//        binding.imageBack.setOnClickListener(v-> onBackPressed());
//        binding.layoutSend.setOnClickListener(v-> sendMessage());
//    }
//
//    private  void init(){
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        chatMessages = new ArrayList<>();
//        chatAdapter = new ChatAdapter(chatMessages, receiverFriend.getpImage_url(), preferenceManager.getString(Constants.KEY_USER_ID));
//        binding.chatRecyclerView.setAdapter(chatAdapter);
//        fStore = FirebaseFirestore.getInstance();
//    }
//
//    private void  loadReceiverDetails(){
//        receiverFriend = (UserModel) getIntent().getSerializableExtra(Constants.KEY_USER);
//        binding.textName.setText(receiverFriend.getUsername());
////        System.out.println(receiverFriend.getUsername());
//    }
//
//    private  void sendMessage(){
//        HashMap<String, Object> message = new HashMap<>();
//        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
//        message.put(Constants.KEY_RECEIVER_ID, receiverFriend.getId());
//        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
//        message.put(Constants.KEY_TIMESTAMP, new Date());
//        fStore.collection(Constants.KEY_COLLECTION_CHAT).add(message);
//        binding.inputMessage.setText(null);
//    }
//
//    public void listenMesssages(){
//        fStore.collection(Constants.KEY_COLLECTION_CHAT)
//                //get where the sender id is the id of current user
//                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
//                //where the receiver id is same as the id of receiver or user currently talking to
//                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverFriend.getId())
//                .addSnapshotListener(eventListener);
//        fStore.collection(Constants.KEY_COLLECTION_CHAT)
//                //get where the sender id is the id of receiver user
//                .whereEqualTo(Constants.KEY_SENDER_ID, receiverFriend.getId())
//                //where the receiver id is same as the id of current user
//                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
//                .addSnapshotListener(eventListener);
//    }
//
//    private String getReadableDataTime(Date date){
//        return  new SimpleDateFormat("mmmm dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
//    }
//
//    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
//        if(error != null){
//            return;
//        }
//        if (value != null){
//            int count = chatMessages.size();
//            for(DocumentChange documentChange: value.getDocumentChanges()){
//                if(documentChange.getType() == DocumentChange.Type.ADDED){
//                    Chat_messageModel chatMessage = new Chat_messageModel();
//                    chatMessage.senderID = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
//                    chatMessage.receiverID = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
//                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
//                    chatMessage.dateTime = getReadableDataTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
//                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
//                    chatMessages.add(chatMessage);
//                }
//            }
//            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
//            if (count == 0){
//                chatAdapter.notifyDataSetChanged();
//            }else{
//                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
//                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() -1);
//            }
//            binding.chatRecyclerView.setVisibility(View.VISIBLE);
//        }
//        binding.progressBar.setVisibility(View.GONE);
//    };
//}