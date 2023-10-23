package com.eou.screentalker.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eou.screentalker.Activities.ChatActivity;
import com.eou.screentalker.Activities.CreatePublicCommunityActivity;
import com.eou.screentalker.Activities.EditprofileActivity;
import com.eou.screentalker.Activities.GroupchatActivity;
import com.eou.screentalker.Adapters.Community_cardAdapter;
import com.eou.screentalker.Listeners.CommunityListener;
import com.eou.screentalker.Listeners.UserListener;
import com.eou.screentalker.Models.CommunityModel;
import com.eou.screentalker.Models.UserModel;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.FragmentCommunityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment implements CommunityListener {
    private PreferenceManager preferenceManager;
    private CollectionReference communityReference;
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseFirestore fStore;
    private FragmentCommunityBinding binding;


    public CommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(requireActivity());
        mAuth=FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        communityReference = fStore.collection("communities");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentCommunityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        public_communityRecyclerView = view.findViewById(R.id.public_communityRecyclerView);
        getPublicCommunities();
        binding.btnPublic.setOnClickListener(v-> {
            Intent intent = new Intent(v.getContext(), CreatePublicCommunityActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getPublicCommunities();
    }

    public void getPublicCommunities(){
        communityReference
                .whereEqualTo("is_public", true)
                .get().addOnCompleteListener(querySnapshotTask -> {
            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
            if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
                List<CommunityModel> communitys = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    CommunityModel community = new CommunityModel();
                    community.setName(queryDocumentSnapshot.getString("name"));
//                    System.out.println(queryDocumentSnapshot.getString("name"));
                    community.setDp_url(queryDocumentSnapshot.getString("dp_url"));
//                    System.out.println(queryDocumentSnapshot.getString("Dp_url"));
                    community.setMembers(queryDocumentSnapshot.getString("members"));
                    community.setIs_public(Boolean.TRUE.equals(queryDocumentSnapshot.getBoolean("is_public")));
                    community.setId(queryDocumentSnapshot.getId());
                    communitys.add(community);
                }
                if(communitys.size() > 0){
                    Community_cardAdapter community_cardAdapter = new Community_cardAdapter(communitys, this);
                    GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false);
                    //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
                    layoutManager.setReverseLayout(true);
                    binding.publicCommunityRecyclerView.setLayoutManager(layoutManager);
                    binding.publicCommunityRecyclerView.setAdapter(community_cardAdapter);
                    binding.publicCommunityRecyclerView.setVisibility(View.VISIBLE);
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

    @Override
    public void onCommunityClicked(CommunityModel community) {
        Intent intent = new Intent(requireActivity(), GroupchatActivity.class);
        intent.putExtra("community", community);
        startActivity(intent);
    }
}