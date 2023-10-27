package com.eou.screentalker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.eou.screentalker.Adapters.ChatAdapter;
import com.eou.screentalker.Adapters.Group_chatAdapter;
import com.eou.screentalker.Models.Chat_messageModel;
import com.eou.screentalker.Models.CommunityModel;
import com.eou.screentalker.Models.Group_chat_messageModel;
import com.eou.screentalker.Models.UserModel;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.ActivityGroupchatBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class GroupchatActivity extends AppCompatActivity {
    private ActivityGroupchatBinding binding;
    private UserModel receiverUser;
    private CommunityModel communityModel;
    private List<Group_chat_messageModel> gChatMessages;
    private Group_chatAdapter gChatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore fStore;
    private String pImage;
    private String username;
    private String userID;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupchatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        fStore = FirebaseFirestore.getInstance();

        loadGroupDetails();
        listenMesssages();
        init();
        binding.imageBack.setOnClickListener(v-> onBackPressed());
        binding.layoutSend.setOnClickListener(v-> sendMessage());
    }

    private  void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        gChatMessages = new ArrayList<>();
        System.out.println("Top checking: " + pImage);
        gChatAdapter = new Group_chatAdapter(gChatMessages,preferenceManager.getString(Constants.KEY_USER_ID));
        binding.chatRecyclerView.setAdapter(gChatAdapter);
        fStore = FirebaseFirestore.getInstance();
    }

    private void  loadGroupDetails(){
        communityModel = (CommunityModel) getIntent().getSerializableExtra("community");
        binding.textName.setText(communityModel.getName());
        System.out.println(communityModel.getMembers());
    }

    private  void sendMessage(){
        HashMap<String, Object> message = new HashMap<>();
        message.put("community_id", communityModel.getId());
        message.put("pImage", preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
        message.put("username", preferenceManager.getString(Constants.KEY_NAME));
        System.out.println(preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        fStore.collection("Group chat").add(message);
        binding.inputMessage.setText(null);
    }

    public void listenMesssages(){
        System.out.println(communityModel.getId());
        fStore.collection("Group chat")
//                //get where the sender id is the id of current user
                .whereEqualTo("community_id", communityModel.getId())
//                //where the receiver id is same as the id of receiver or user currently talking to
//                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.getId())
                .addSnapshotListener(eventListener);
//        fStore.collection(Constants.KEY_COLLECTION_CHAT)
//                //get where the sender id is the id of receiver user
//                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.getId())
//                //where the receiver id is same as the id of current user
//                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
//                .addSnapshotListener(eventListener);
    }

    private String getReadableDataTime(Date date){
        return  new SimpleDateFormat("mmmm dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return;
        }
        if (value != null){
            int count = gChatMessages.size();
            for(DocumentChange documentChange: value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    Group_chat_messageModel gChatMessage = new Group_chat_messageModel();
                    gChatMessage.senderID = documentChange.getDocument().getString("senderID");
                    gChatMessage.communityID = documentChange.getDocument().getString("community_id");
                    gChatMessage.message = documentChange.getDocument().getString("message");
                    gChatMessage.dateTime = getReadableDataTime(documentChange.getDocument().getDate("timestamp"));
                    gChatMessage.dateObject = documentChange.getDocument().getDate("timestamp");
//                    System.out.println("Down checking: " + documentChange.getDocument().getString("pImage"));
                    gChatMessage.image = documentChange.getDocument().getString("pImage");
                    gChatMessage.username = documentChange.getDocument().getString("username");
                    gChatMessages.add(gChatMessage);
                }
            }
            Collections.sort(gChatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0){
                gChatAdapter.notifyDataSetChanged();
            }else{
                gChatAdapter.notifyItemRangeInserted(gChatMessages.size(), gChatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(gChatMessages.size() -1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
    };
}