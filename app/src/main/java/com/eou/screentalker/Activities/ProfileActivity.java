package com.eou.screentalker.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eou.screentalker.Adapters.PartAdapter;
import com.eou.screentalker.Adapters.PostAdapter;
import com.eou.screentalker.Models.CommunityModel;
import com.eou.screentalker.Models.Group_chat_messageModel;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private FirebaseFirestore fStore;
    private  String id;
    private  String source;
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private CollectionReference collectionReference1;
    private PreferenceManager preferenceManager;
    private CollectionReference postReference;
    private CollectionReference viewedReference;
    private boolean isFriend;
    private boolean isRequested;
    private boolean isRequestedYou;
    private String currentUserid;
    private MemberModel memberModel;


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
        }
//        else if("friends".equals(source)){
//            id = fri
//        }
        else {
            id = getIntent().getStringExtra("id");
        }
        documentReference = fStore.collection("users").document(id);
        collectionReference = fStore.collection("requests");
        collectionReference1 = fStore.collection("friends");
        postReference = fStore.collection("posts");
        viewedReference = fStore.collection("watched").document(id).collection("parts");
        currentUserid = preferenceManager.getString(Constants.KEY_USER_ID);
        isFriend = false;
        isRequested = false;
        isRequestedYou = false;

        getPosts();

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
                if(currentUserid.equals(queryDocumentSnapshot.getString("sender_id")) && id.equals(queryDocumentSnapshot.getString("receiver_id"))){
//                    binding.add.setVisibility(View.GONE);
//                    binding.requested.setVisibility(View.VISIBLE);
                    System.out.println("reached here5");
                    isRequested = true;
                    isRequestedYou = false;
                }else if (currentUserid.equals(queryDocumentSnapshot.getString("receiver_id")) && id.equals(queryDocumentSnapshot.getString("sender_id"))){
                    isRequested = false;
                    isRequestedYou = true;
                    System.out.println("reached here5.1");
                }else{
                    isRequestedYou = false;
                    isRequested = false;
                }
            }
        });

        //deciding if to show add, requested or nothing
        collectionReference1.get().addOnCompleteListener(v1 -> {
            String currentUserid = preferenceManager.getString(Constants.KEY_USER_ID);
            for(QueryDocumentSnapshot queryDocumentSnapshot1: v1.getResult()){
                if(currentUserid.equals(queryDocumentSnapshot1.getString("person_id")) && id.equals(queryDocumentSnapshot1.getString("friend_id"))){
//                    binding.add.setVisibility(View.GONE);
//                    binding.requested.setVisibility(View.GONE);
                    System.out.println("reached here1");
                    isFriend = true;
                }else if(currentUserid.equals(queryDocumentSnapshot1.getString("friend_id")) && id.equals(queryDocumentSnapshot1.getString("person_id"))) {
//                    binding.add.setVisibility(View.GONE);
//                    binding.requested.setVisibility(View.GONE);
                    System.out.println("reached here2");
                    isFriend = true;
                }else{
//                    binding.add.setVisibility(View.VISIBLE);
                    System.out.println("reached here3");
                    isFriend = false;
                }
            }
            if (!isRequested && !isFriend && !isRequestedYou){
                binding.add.setVisibility(View.VISIBLE);
                System.out.println("reached here6");
//            binding.add.setVisibility(View.GONE);
            }else if (isFriend){
                binding.requested.setVisibility(View.GONE);
                binding.add.setVisibility(View.GONE);
                binding.remove.setVisibility(View.VISIBLE);
                binding.viewed.setVisibility(View.VISIBLE);
                getViewedMovies();
                System.out.println("reached here7");
            }else if (isRequested){
                binding.requested.setVisibility(View.VISIBLE);
                System.out.println("reached here8");
            }else if(isRequestedYou){
                binding.requested.setText("Requested You Already");
                binding.requested.setVisibility(View.VISIBLE);
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
                                            }
                                        } else {
                                            showErrorMessage();
                                        }
                                    }
                                } else {
                                    showErrorMessage();
                                }
                            }
                        });
    }

    public void getPosts(){
//        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
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
                            showErrorMessage();
                        }
                    }else{
                        showErrorMessage();
                    }
                });
    }

    public void showErrorMessage(){
        Toast.makeText(this, "No posts available", Toast.LENGTH_SHORT).show();
    }

    public void getViewedMovies(){
        if(isFriend){
            List<PartModel> parts = new ArrayList<>();
            viewedReference
                    .get().addOnCompleteListener(querySnapshotTask -> {
                        if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
                            List<PostModel> posts = new ArrayList<>();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
//                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
//                                continue;
//                            }
                                PartModel part = new PartModel();
                                part.setPart(queryDocumentSnapshot.getString("name"));
//                    System.out.println(queryDocumentSnapshot.getString("name"));
                                part.setThumburl(queryDocumentSnapshot.getString("thumbnail"));
//                    System.out.println(queryDocumentSnapshot.getString("Dp_url"));
                                parts.add(part);
                            }
                            if(parts.size() > 0){
                                PartAdapter partAdapter = new PartAdapter(parts, this);
                                GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
                                //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
                                layoutManager.setReverseLayout(true);
                                binding.viewedRecyclerView.setLayoutManager(layoutManager);
                                binding.viewedRecyclerView.setAdapter(partAdapter);
                                binding.viewedRecyclerView.setVisibility(View.VISIBLE);
                            }else{
                                showErrorMessage();
                            }
                        }else{
                            showErrorMessage();
                        }
                    });
        }
    }
}