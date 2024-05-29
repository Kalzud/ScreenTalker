package com.eou.screentalker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eou.screentalker.Adapters.PartAdapter;
import com.eou.screentalker.Adapters.PostAdapter;
import com.eou.screentalker.Models.MemberModel;
import com.eou.screentalker.Models.PartModel;
import com.eou.screentalker.Models.PostModel;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
//import com.eou.screentalker.databinding.ActivityProfileB;
import com.eou.screentalker.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    public ActivityProfileBinding binding;
    private FirebaseFirestore fStore;
    private  String id;
    private  String source;
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private CollectionReference collectionReference1;
    private PreferenceManager preferenceManager;
    private CollectionReference postReference;
    private CollectionReference viewedReference;
    public boolean isFriend;
    public boolean isRequested;
    public boolean isRequestedYou;
    private String currentUserid;
    private MemberModel memberModel;

    public boolean areFriendChecksDone = false;
    public boolean areRequestChecksDone = false;
    public int foundFriend = 0;
    public int foundRequest = 0;
    public int foundRequestedYou = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        fStore = FirebaseFirestore.getInstance();
        memberModel = (MemberModel) getIntent().getSerializableExtra("member");
        source = getIntent().getStringExtra("source");
        if("members".equals(source)){
            id = memberModel.id;
        } else {
            id = getIntent().getStringExtra("id");
        }
        documentReference = fStore.collection("users").document(id);
        collectionReference = fStore.collection("requests");
        collectionReference1 = fStore.collection("friends");
        postReference = fStore.collection("posts");
        viewedReference = fStore.collection("watched").document(id).collection("parts");
        currentUserid = preferenceManager.getString(Constants.KEY_USER_ID);
        assignStatus();
        getPosts();
//        getViewedMovies();
        documentReference.addSnapshotListener((value, error) -> {
            //setting this to show the current data in database on xml
            Picasso.get().load(Uri.parse(value.getString("pImage_url"))).into(binding.imageProfile);
            binding.username.setText(value.getString("username"));
            binding.bio.setText(value.getString("bio"));

        });
        binding.imageBack.setOnClickListener(v-> {
            super.onBackPressed();
            finish();
        });

        collectionReference.get().addOnCompleteListener(v -> {
            System.out.println("reached here4");
            for(QueryDocumentSnapshot queryDocumentSnapshot: v.getResult()){
                System.out.println("reached here4.3");
                String senderID = queryDocumentSnapshot.getString("sender_id");
                String receiverID = queryDocumentSnapshot.getString("receiver_id");
                checkRequestStatus(senderID, receiverID, currentUserid, id);
                assignStatus();
//                checkStatus();
            }
        });

        //deciding if to show add, requested or nothing
        collectionReference1.get().addOnCompleteListener(v1 -> {
            System.out.println("reached here4.1");
            for(QueryDocumentSnapshot queryDocumentSnapshot1: v1.getResult()){
                System.out.println("reached here4.2");
                String personID = queryDocumentSnapshot1.getString("person_id");
                Log.d("personId", personID);
                Log.d("userid", currentUserid);
                String friendID = queryDocumentSnapshot1.getString("friend_id");
                Log.d("friendId", friendID);
                Log.d("otherid", id);
                checkFriendStatus(personID, friendID, currentUserid, id);
                assignStatus();
//                checkStatus();
            }
        });


        binding.add.setOnClickListener(v -> add());
        binding.remove.setOnClickListener(v -> remove());
    }

    private  void add(){
        HashMap<String, Object> request = new HashMap<>();
        request.put("sender_id", preferenceManager.getString(Constants.KEY_USER_ID));
        request.put("sender_pImage", preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
        request.put("sender_username", preferenceManager.getString(Constants.KEY_NAME));
        request.put("sender_token", preferenceManager.getString(Constants.KEY_FCM_TOKEN));
        System.out.println(preferenceManager.getString(Constants.KEY_FCM_TOKEN));
        request.put("receiver_id", id);
        fStore.collection("requests").add(request);
        binding.add.setVisibility(View.GONE);
        binding.requested.setVisibility(View.VISIBLE);
    }

    private void remove(){
            // Query where "friend_username" is equal to 'id' and "person_username" is equal to 'currentUserId'
            Task<QuerySnapshot> firstQuery = collectionReference1
                    .whereEqualTo("friend_id", id)
                    .whereEqualTo("person_id", currentUserid)
                    .get();

        // Query where "friend_username" is equal to 'currentUserId' and "person_username" is equal to 'id'
                Task<QuerySnapshot> secondQuery = collectionReference1
                        .whereEqualTo("friend_id", currentUserid)
                        .whereEqualTo("person_id", id)
                        .get();

        // Combine the results of both queries
                Tasks.whenAllComplete(firstQuery, secondQuery)
                        .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                            @Override
                            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                                if (task.isSuccessful()) {
                                    for (Task<?> queryTask : task.getResult()) {
                                        if (queryTask.isSuccessful()) {
                                            QuerySnapshot result = (QuerySnapshot) queryTask.getResult();
                                            for (QueryDocumentSnapshot document : result) {
                                                System.out.println(document.getId());
                                                collectionReference1.document(document.getId()).delete();
                                                binding.add.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            showErrorMessage("Unable to remove requests");
                                        }
                                    }
                                } else {
                                    showErrorMessage("Unable to remove requests");
                                }
                            }
                        });

    }

    public void getPosts(){
        postReference
                .whereEqualTo("id", id)
                .get().addOnCompleteListener(querySnapshotTask -> {
                    if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
                        List<PostModel> posts = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
//                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
//                                continue;
//                            }
                            PostModel post = new PostModel();
                            post.id = queryDocumentSnapshot.getString("id");
//                    System.out.println(queryDocumentSnapshot.getString("name"));
                            post.post = queryDocumentSnapshot.getString("post");
//                    System.out.println(queryDocumentSnapshot.getString("Dp_url"));
                            post.tag = queryDocumentSnapshot.getString("tag");
                            posts.add(post);
                        }
                        if(posts.size() > 0){
                            PostAdapter postAdapter = new PostAdapter(posts);
                            GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
                            //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
                            layoutManager.setReverseLayout(true);
                            binding.postRecyclerView.setLayoutManager(layoutManager);
                            binding.postRecyclerView.setAdapter(postAdapter);
                            binding.postRecyclerView.setVisibility(View.VISIBLE);
                        }else{
                            showErrorMessage("No post available");
                        }
                    }else{
                        showErrorMessage("No post available");
                    }
                });
    }

    public void showErrorMessage(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void getViewedMovies(){
            List<PartModel> parts = new ArrayList<>();
            viewedReference
                    .get().addOnCompleteListener(querySnapshotTask -> {
                        if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
                            List<PostModel> posts = new ArrayList<>();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
                                PartModel part = new PartModel();
                                part.setPart(queryDocumentSnapshot.getString("name"));
//                    System.out.println(queryDocumentSnapshot.getString("name"));
                                part.setThumburl(queryDocumentSnapshot.getString("thumbnail"));
//                    System.out.println(queryDocumentSnapshot.getString("Dp_url"));
                                part.setVidurl(queryDocumentSnapshot.getString("vid"));
                                parts.add(part);
                            }
                            if(parts.size() > 0){
                                PartAdapter partAdapter = new PartAdapter(parts, this);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                                layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
                                layoutManager.setReverseLayout(true);
                                layoutManager.setStackFromEnd(true);
                                binding.viewedRecyclerView.setLayoutManager(layoutManager);
                                binding.viewedRecyclerView.setAdapter(partAdapter);
                                binding.viewedRecyclerView.setVisibility(View.VISIBLE);
                            }else{
                                showErrorMessage("No viewed movies available");
                            }
                        }else{
                            showErrorMessage("No viewed movies available");
                        }
                    });
    }



    public void checkFriendStatus(String personID, String friendID, String userId, String profileOwnerId) {
        //check if user is friend with profile owner
        if(userId.equals(personID) && profileOwnerId.equals(friendID)){
            System.out.println("reached here1");
            foundFriend++;
            System.out.println(foundFriend);
        }else
        if(userId.equals(friendID) && profileOwnerId.equals(personID)) {
            System.out.println("reached here2");
            foundFriend++;
            System.out.println(foundFriend);
        }
//        System.out.println(isFriend);
        areFriendChecksDone = true;
    }

    public void checkRequestStatus(String senderID, String receiverID, String userId, String profileOwnerId){
        //check if user received request
        if(userId.equals(senderID) && profileOwnerId.equals(receiverID)){
            System.out.println("reached here5");
            foundRequest++;
            System.out.println(foundRequest);
            //check if user sent request
        }else if (userId.equals(receiverID) && profileOwnerId.equals(senderID)){
            foundRequestedYou++;
            System.out.println(foundRequestedYou);
            System.out.println("reached here5.1");
        }
        areRequestChecksDone = true;
    }

    public void assignStatus(){
            if(foundFriend > 0){
                isFriend = true;
            }else{
                isFriend = false;
            }
            if(foundRequest > 0){
                isRequested = true;
            }else{
                isRequested = false;
            }
            if(foundRequestedYou > 0){
                isRequestedYou = true;
            }else{
                isRequestedYou = false;
            }
            setXmlBasedOnStatus();
    }

    //this method was previously checkStatus
    public void setXmlBasedOnStatus() {
            if (!isRequested && !isFriend && !isRequestedYou) {
                if(binding != null){
                    binding.add.setVisibility(View.VISIBLE);
                    binding.requested.setVisibility(View.GONE);
                    binding.remove.setVisibility(View.GONE);
                }
                Log.d("No friend or request", "Add button should show");
                Log.d("No friend", String.valueOf(isFriend));
                Log.d("requested", String.valueOf(isRequested));
                Log.d("requested you", String.valueOf(isRequestedYou));
                System.out.println("reached here6");
            } else if (isFriend) {
                if(binding != null){
                    binding.requested.setVisibility(View.GONE);
                    binding.add.setVisibility(View.GONE);
                    binding.remove.setVisibility(View.VISIBLE);
                    binding.viewed.setVisibility(View.VISIBLE);
                    getViewedMovies();
                }
                Log.d("friend", "Remove button should show");
                System.out.println("reached here7");
            } else if (isRequested) {
                if(binding != null){
                    binding.requested.setVisibility(View.VISIBLE);
                    binding.add.setVisibility(View.GONE);
                    binding.remove.setVisibility(View.GONE);
                }
                Log.d("you requested", "you requested");
                System.out.println("reached here8");
            } else {
                if(binding != null){
                    binding.requested.setText("Requested You Already");
                    binding.requested.setVisibility(View.VISIBLE);
                    binding.add.setVisibility(View.GONE);
                    binding.remove.setVisibility(View.GONE);
                }
                Log.d("requested you", "person requested you");
            }
    }

}