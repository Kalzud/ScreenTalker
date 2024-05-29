package com.eou.screentalker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eou.screentalker.Adapters.UsersAdapter;
import com.eou.screentalker.Listeners.UserListener;
import com.eou.screentalker.Models.UserModel;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.ActivityUsersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListener {
    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;
    private CollectionReference documentReference;
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        mAuth=FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("users");
        getUsers();
        binding.image.setOnClickListener(v -> onBackPressed());
    }

    public void getUsers(){
        loading(true);
        documentReference.get().addOnCompleteListener(querySnapshotTask -> {
            loading(false);
            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
            if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
                List<UserModel> users = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    UserModel user = new UserModel();
                    user.setUsername(queryDocumentSnapshot.getString(Constants.KEY_NAME));
                    user.setpImage_url(queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
                    user.setFcmToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
                    user.setEmail(queryDocumentSnapshot.getString(Constants.KEY_EMAIL));
                    user.setId(queryDocumentSnapshot.getId());
                    users.add(user);
                }
                if(users.size() > 0){
                    UsersAdapter usersAdapter = new UsersAdapter(users, this);
                    binding.usersRecyclerView.setAdapter(usersAdapter);
                    binding.usersRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    showErrorMessage("No user available");
                }
            }else{
                showErrorMessage("No user available");
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
    public void onUserClicked(UserModel user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}