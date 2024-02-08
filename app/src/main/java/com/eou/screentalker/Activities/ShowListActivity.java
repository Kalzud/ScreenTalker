package com.eou.screentalker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eou.screentalker.Adapters.FriendsAdapter;
import com.eou.screentalker.Adapters.MemberAdapter;
import com.eou.screentalker.Listeners.FriendListener;
import com.eou.screentalker.Listeners.MemberListener;
import com.eou.screentalker.Models.FriendModel;
import com.eou.screentalker.Models.MemberModel;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.ActivityShowListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowListActivity extends AppCompatActivity implements MemberListener, FriendListener {
    private ActivityShowListBinding binding;
    private PreferenceManager preferenceManager;
    private CollectionReference collectionReference;
    private FirebaseAuth mAuth;
    private String userID;
    private String communityID;
    private FirebaseFirestore fStore;
    private  String source;
    private int select = 1;
    private final List<FriendModel> newMembers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        mAuth=FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        source = getIntent().getStringExtra("source");
        if("members".equals(source)){
            showMembers();
        }else if("friends".equals(source)){
            showFriends();
        }

        binding.image.setOnClickListener(v -> onBackPressed());
    }

    public void getMembers(){
        loading(true);
        collectionReference.get().addOnCompleteListener(querySnapshotTask -> {
            loading(false);
            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
            if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
                List<MemberModel> members = new ArrayList<>();;
                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
//                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
//                        continue;
//                    }

                    MemberModel member = new MemberModel();
                    member.id = queryDocumentSnapshot.getId();
                    member.dp = queryDocumentSnapshot.getString("dp");
                    member.name = queryDocumentSnapshot.getString("name");
                    member.canSendText = Boolean.FALSE.equals(queryDocumentSnapshot.getBoolean("canSendText"));
                    member.admin = Boolean.FALSE.equals(queryDocumentSnapshot.getBoolean("admin"));
                    members.add(member);
                    System.out.println(members);
                }
                if (members.size() > 0) {
                    MemberAdapter memberAdapter = new MemberAdapter(members, this);
                    binding.usersRecyclerView.setAdapter(memberAdapter);
                    binding.usersRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    showErrorMessage("No members Available");
                }
            }else{
                showErrorMessage("No members Available");
            }
        });
    }

    public void getFriends(){
        loading(true);
        collectionReference.get().addOnCompleteListener(querySnapshotTask -> {
            loading(false);
            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
            if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
                List<FriendModel> friends = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
                    if (currentUserId.equals(queryDocumentSnapshot.getString("person_id"))) {
                        FriendModel friend = new FriendModel();
                        friend.friend_username= queryDocumentSnapshot.getString("friend_username");
                        friend.friend_pImage= queryDocumentSnapshot.getString("friend_pImage");
                        friend.canSendText = true;
                        friend.admin = true;
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
                        friend.canSendText = true;
                        friend.admin = true;
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
                    showErrorMessage("No friends available");
                }
            }else{
                showErrorMessage("No friends available");
            }
        });
    }

    public void showErrorMessage(String text){
        binding.errorMsg.setText(String.format("%s", text));
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
    public void onMemberClicked(MemberModel member) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("source", "members");
        intent.putExtra("member", member);
        startActivity(intent);

    }

    public void showMembers(){
        binding.leave.setVisibility(View.VISIBLE);
        binding.title.setText("Group members");
        communityID = getIntent().getStringExtra("communityId");
        System.out.println(communityID);
        collectionReference = fStore.collection("communities").document(communityID).collection("members");
        getMembers();
        binding.leave.setOnClickListener(v -> leaveGroup(collectionReference));
    }

    public void showFriends(){
        binding.add.setVisibility(View.VISIBLE);
        binding.add.setOnClickListener(v -> turnFriendToMember());
        binding.title.setText("Add friends to group");
        collectionReference = fStore.collection("friends");
        getFriends();
    }

    public void turnFriendToMember(){
        communityID = getIntent().getStringExtra("communityId");
        System.out.println(communityID);
//        CollectionReference newMembersCollectionReference = fStore.collection("communities").document(communityID).collection("members");
        collectionReference = fStore.collection("communities").document(communityID).collection("members");
        for(FriendModel friendModel: newMembers){
            Map<String, Object> memberDetails = new HashMap<>();
            memberDetails.put("name", friendModel.friend_username);
            memberDetails.put("dp", friendModel.friend_pImage);
            memberDetails.put("canSendText", true);
            memberDetails.put("admin", false);
            collectionReference.document(friendModel.friend_id).set(memberDetails).addOnSuccessListener(v1 -> System.out.println("done adding"));
        }
    }

    public void leaveGroup(CollectionReference collectionReference){
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        DocumentReference userDoc = collectionReference.document(currentUserId);
        userDoc.delete().addOnCompleteListener(v -> System.out.println("deleted"));
    }


    @Override
    public void onFriendClicked(FriendModel friend) {
        System.out.println("here");

        if(select == 1){
            select = 2;
            if (!newMembers.contains(friend)) {
                // The friend is not already in the list
                newMembers.add(friend);
            }
            System.out.println(newMembers);
        }else{
            select = 1;

            newMembers.remove(friend);
            System.out.println(newMembers);
        }
//        newMembers.add(friend);
//        System.out.println(newMembers);
    }
}