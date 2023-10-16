package com.eou.screentalker.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eou.screentalker.Activities.ChatActivity;
import com.eou.screentalker.Adapters.FriendsAdapter;
import com.eou.screentalker.Adapters.UsersAdapter;
import com.eou.screentalker.Listeners.FriendListener;
import com.eou.screentalker.Listeners.UserListener;
import com.eou.screentalker.Models.FriendModel;
import com.eou.screentalker.Models.UserModel;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.FragmentMessagesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment implements FriendListener {
    private FragmentMessagesBinding binding;
    private PreferenceManager preferenceManager;
    private CollectionReference documentReference;
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseFirestore fStore;


    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(requireActivity());
        mAuth=FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("friends");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentMessagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUsers();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUsers();
    }

    public void getUsers(){
        loading(true);
        documentReference.get().addOnCompleteListener(querySnapshotTask -> {
            loading(false);
            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
            if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
                List<FriendModel> friends = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
                    if (currentUserId.equals(queryDocumentSnapshot.getString("person_id"))) {
                        FriendModel friend = new FriendModel();
                        friend.friend_username= queryDocumentSnapshot.getString("friend_username");
                        friend.friend_pImage= queryDocumentSnapshot.getString("friend_pImage");
//                    friend.setFcmToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
//                    friend.setEmail(queryDocumentSnapshot.getString(Constants.KEY_EMAIL));
                        friend.friend_id= queryDocumentSnapshot.getString("friend_id");
                        friends.add(friend);
//                        continue;
                    }
                    if (currentUserId.equals(queryDocumentSnapshot.getString("friend_id"))) {
                        FriendModel friend = new FriendModel();
                        friend.friend_username= queryDocumentSnapshot.getString("person_username");
                        friend.friend_pImage= queryDocumentSnapshot.getString("person_pImage");
//                    friend.setFcmToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
//                    friend.setEmail(queryDocumentSnapshot.getString(Constants.KEY_EMAIL));
                        friend.friend_id= queryDocumentSnapshot.getString("person_id");
                        friends.add(friend);
//                        continue;
                    }

                }
                if(friends.size() > 0){
                    FriendsAdapter friendsAdapter = new FriendsAdapter(friends, (FriendListener) this);
                    binding.usersRecyclerView.setAdapter(friendsAdapter);
                    binding.usersRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    showErrorMessage();
                }
            }else{
                showErrorMessage();
            }
        });
    }

    public void showErrorMessage(){
        binding.errorMsg.setText(String.format("%s", "No user available"));
        binding.errorMsg.setVisibility(View.VISIBLE);
    }

    public void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onFriendClicked(FriendModel friend) {
        Intent intent = new Intent(requireActivity(), ChatActivity.class);
        intent.putExtra("friend", friend);
        startActivity(intent);
    }
}


































//public class MessagesFragment extends Fragment implements UserListener {
//    private FragmentMessagesBinding binding;
//    private PreferenceManager preferenceManager;
//    private CollectionReference documentReference;
//    private CollectionReference filter;
//    private FirebaseAuth mAuth;
//    private String userID;
//    private FirebaseFirestore fStore;
//
//
//    public MessagesFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        preferenceManager = new PreferenceManager(requireActivity());
//        mAuth=FirebaseAuth.getInstance();
//        fStore = FirebaseFirestore.getInstance();
//        userID = mAuth.getCurrentUser().getUid();
//        documentReference = fStore.collection("users");
//        filter = fStore.collection("friends");
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        binding= FragmentMessagesBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        getUsers();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getUsers();
//    }
//
//    public void getUsers(){
//        loading(true);
//        documentReference.get().addOnCompleteListener(querySnapshotTask -> {
//            loading(false);
//            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
//            if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
//                List<UserModel> users = new ArrayList<>();
//                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
//                    if (!currentUserId.equals(queryDocumentSnapshot.getId())) {
//                        continue;
//                    }
//                    UserModel user = new UserModel();
//                    user.setUsername(queryDocumentSnapshot.getString(Constants.KEY_NAME));
//                    user.setpImage_url(queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
//                    user.setFcmToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
//                    user.setEmail(queryDocumentSnapshot.getString(Constants.KEY_EMAIL));
//                    user.setId(queryDocumentSnapshot.getId());
//                    users.add(user);
//                }
//                if(users.size() > 0){
//                    UsersAdapter usersAdapter = new UsersAdapter(users, this);
//                    binding.usersRecyclerView.setAdapter(usersAdapter);
//                    binding.usersRecyclerView.setVisibility(View.VISIBLE);
//                }else{
//                    showErrorMessage();
//                }
//            }else{
//                showErrorMessage();
//            }
//        });
//    }
//
//    public void showErrorMessage(){
//        binding.errorMsg.setText(String.format("%s", "No user available"));
//        binding.errorMsg.setVisibility(View.VISIBLE);
//    }
//
//    public void loading(Boolean isLoading){
//        if(isLoading){
//            binding.progressBar.setVisibility(View.VISIBLE);
//        }else{
//            binding.progressBar.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    @Override
//    public void onUserClicked(UserModel user) {
//        Intent intent = new Intent(requireActivity(), ChatActivity.class);
//        intent.putExtra(Constants.KEY_USER, user);
//        startActivity(intent);
//    }
//
//    public void filtering(){
//        loading(true);
//        filter.get().addOnCompleteListener(querySnapshotTask -> {
//            loading(false);
//            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
//            if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
//                List<UserModel> users = new ArrayList<>();
//                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
//                    if (currentUserId.equals(queryDocumentSnapshot.getString("person_id"))) {
//                        continue;
//                    }
//                    UserModel user = new UserModel();
//                    user.setUsername(queryDocumentSnapshot.getString(Constants.KEY_NAME));
//                    user.setpImage_url(queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
//                    user.setFcmToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
//                    user.setEmail(queryDocumentSnapshot.getString(Constants.KEY_EMAIL));
//                    user.setId(queryDocumentSnapshot.getId());
//                    users.add(user);
//                }
//                if(users.size() > 0){
//                    UsersAdapter usersAdapter = new UsersAdapter(users, this);
//                    binding.usersRecyclerView.setAdapter(usersAdapter);
//                    binding.usersRecyclerView.setVisibility(View.VISIBLE);
//                }else{
//                    showErrorMessage();
//                }
//            }else{
//                showErrorMessage();
//            }
//        });
//    }
//}