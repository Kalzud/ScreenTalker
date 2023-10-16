package com.eou.screentalker.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eou.screentalker.Activities.EditprofileActivity;
import com.eou.screentalker.Activities.PostActivity;
import com.eou.screentalker.Adapters.PostAdapter;
import com.eou.screentalker.Adapters.RequestAdapter;
import com.eou.screentalker.Models.PostModel;
import com.eou.screentalker.Models.RequestModel;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.FragmentPersonalProfileBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class Personal_profileFragment extends Fragment {
    private FragmentPersonalProfileBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore fStore;
    private CollectionReference requestReference;
    private CollectionReference postReference;
    private Context context;



    public Personal_profileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity();
        preferenceManager = new PreferenceManager(requireActivity());
        fStore = FirebaseFirestore.getInstance();
        requestReference = fStore.collection("requests");
        postReference = fStore.collection("posts");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentPersonalProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserDetails();
        getInvities();
        getPosts();
        binding.editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditprofileActivity.class);
            startActivity(intent);
        });
        binding.posts.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), PostActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserDetails();
        getInvities();
        getPosts();
    }

    private void loadUserDetails(){
        binding.username.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.bio.setText(preferenceManager.getString(Constants.KEY_BIO));
        Picasso.get().load(Uri.parse(preferenceManager.getString(Constants.KEY_PROFILE_IMAGE))).into(binding.imageProfile);
        System.out.println("Fourth check: " + preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
    }

    public void getInvities(){
        requestReference
                .whereEqualTo("receiver_id", preferenceManager.getString(Constants.KEY_USER_ID))
                .get().addOnCompleteListener(querySnapshotTask -> {
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
                        List<RequestModel> requests = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
                            RequestModel request= new RequestModel();
                            request.receiver_id = queryDocumentSnapshot.getString("receiver_id");
//                    System.out.println(queryDocumentSnapshot.getString("name"));
                            request.sender_id = queryDocumentSnapshot.getString("sender_id");
//                    System.out.println(queryDocumentSnapshot.getString("Dp_url"));
                            request.sender_pImage = queryDocumentSnapshot.getString("sender_pImage");
                            request.sender_username = queryDocumentSnapshot.getString("sender_username");
                            request.id = queryDocumentSnapshot.getId();
                            requests.add(request);
                        }
                        if(requests.size() > 0){
                            RequestAdapter requestAdapter = new RequestAdapter(requests, context);
                            GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                            //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
                            layoutManager.setReverseLayout(true);
                            binding.recyclerView.setLayoutManager(layoutManager);
                            binding.recyclerView.setAdapter(requestAdapter);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                        }else{
                            showErrorMessage();
                        }
                    }else{
                        showErrorMessage();
                    }
                });
    }

    public void showErrorMessage(){
        Toast.makeText(requireContext(), "No users available", Toast.LENGTH_SHORT).show();
    }

    public void getPosts(){
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        postReference
                .whereEqualTo("id", currentUserId)
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
                            GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false);
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
}