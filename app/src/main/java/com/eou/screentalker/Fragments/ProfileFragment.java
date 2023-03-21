package com.eou.screentalker.Fragments;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eou.screentalker.Models.Group_chat_messageModel;
import com.eou.screentalker.R;
import com.eou.screentalker.databinding.FragmentPersonalProfileBinding;
import com.eou.screentalker.databinding.FragmentProfileBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private FirebaseFirestore fStore;
    private  String id;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        id = getIntent().getStringExtra("id");
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void listenMesssages(){
        fStore.collection("users")
//                //get where the sender id is the id of current user
//                .whereEqualTo("community_id", communityModel.getId())
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

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return;
        }
        if (value != null){
            for(DocumentChange documentChange: value.getDocumentChanges()){
//                    gChatMessage.senderID = documentChange.getDocument().getString("senderID");
//                    gChatMessage.communityID = documentChange.getDocument().getString("community_id");
//                    gChatMessage.message = documentChange.getDocument().getString("message");
//                    gChatMessage.dateTime = getReadableDataTime(documentChange.getDocument().getDate("timestamp"));
//                    gChatMessage.dateObject = documentChange.getDocument().getDate("timestamp");
////                    System.out.println("Down checking: " + documentChange.getDocument().getString("pImage"));
//                    gChatMessage.image = documentChange.getDocument().getString("pImage");
                    binding.username.setText( documentChange.getDocument().getString("username"));
//                    gChatMessage.username = documentChange.getDocument().getString("username");
//                    gChatMessages.add(gChatMessage);
            }
        }
    };
}