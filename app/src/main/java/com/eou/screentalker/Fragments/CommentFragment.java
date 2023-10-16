package com.eou.screentalker.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eou.screentalker.Adapters.ChatAdapter;
import com.eou.screentalker.Adapters.CommentAdapter;
import com.eou.screentalker.Adapters.Community_cardAdapter;
import com.eou.screentalker.Adapters.UsersAdapter;
import com.eou.screentalker.Models.Chat_messageModel;
import com.eou.screentalker.Models.CommentModel;
import com.eou.screentalker.Models.CommunityModel;
import com.eou.screentalker.Models.UserModel;
import com.eou.screentalker.R;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.FragmentCommentBinding;
import com.eou.screentalker.databinding.FragmentMessagesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class CommentFragment extends Fragment {
    private FragmentCommentBinding binding;
    private PreferenceManager preferenceManager;
    private List<CommentModel> comments;
    private CommentAdapter commentAdapter;
    private CollectionReference documentReference;
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseFirestore fStore;
    public String title;


    public CommentFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentCommentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        listenComments();
        binding.layoutSend.setOnClickListener(v-> sendComment());
    }

    private  void init(){
        preferenceManager = new PreferenceManager(requireActivity());
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments,preferenceManager.getString(Constants.KEY_USER_ID));
        binding.chatRecyclerView.setAdapter(commentAdapter);
        fStore = FirebaseFirestore.getInstance();
    }

        private  void sendComment(){
        HashMap<String, Object> comment = new HashMap<>();
        comment.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        comment.put("pImage", preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
        comment.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        comment.put(Constants.KEY_TIMESTAMP, new Date());
        comment.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
        comment.put("title", title);
        fStore.collection("comments").add(comment);
        binding.inputMessage.setText(null);
    }

    public void listenComments(){
        fStore.collection("comments")
                //get where the sender id is the id of current user
                .addSnapshotListener(eventListener);
        System.out.println(title);
        System.out.println( preferenceManager.getString(Constants.KEY_TITLE));

    }

    private String getReadableDataTime(Date date){
        return  new SimpleDateFormat("mmmm dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return;
        }
        if (value != null){
            int count = comments.size();

            for(DocumentChange documentChange: value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    if(Objects.equals(documentChange.getDocument().getString("title"), title)){
                        CommentModel comment = new CommentModel();
                        comment.username = documentChange.getDocument().getString("username");
                        comment.message = documentChange.getDocument().getString("message");
                        comment.image= documentChange.getDocument().getString("pImage");
                        comment.dateTime = getReadableDataTime(documentChange.getDocument().getDate("timestamp"));
                        comment.dateObject = documentChange.getDocument().getDate("timestamp");
                        comment.senderID = documentChange.getDocument().getString("senderID");
//                        title = documentChange.getDocument().getString("title");
                        comments.add(comment);
                    }
                }
            }
            Collections.sort(comments, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0){
                commentAdapter.notifyDataSetChanged();
            }else{
                commentAdapter.notifyItemRangeInserted(comments.size(), comments.size());
                binding.chatRecyclerView.smoothScrollToPosition(comments.size() -1);
                System.out.println(count);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
    };
}