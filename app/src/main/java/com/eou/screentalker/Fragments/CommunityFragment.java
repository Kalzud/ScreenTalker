package com.eou.screentalker.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eou.screentalker.Activities.CreatePrivateCommunityActivity;
import com.eou.screentalker.Activities.CreatePublicCommunityActivity;
import com.eou.screentalker.Activities.GroupchatActivity;
import com.eou.screentalker.Adapters.Community_cardAdapter;
import com.eou.screentalker.Listeners.CommunityListener;
import com.eou.screentalker.Models.Chat_messageModel;
import com.eou.screentalker.Models.CommunityModel;
import com.eou.screentalker.Models.MemberModel;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.FragmentCommunityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<MemberModel> members;
//    private  CollectionReference privateGroupCollection = fStore.collection("communities").document(communityId).collection("members")

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
        members = new ArrayList<>();
//        privateGroupCollection = fStore.collection("communities").document(communityId).collection("members")

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
        getPrivateCommunities();
        binding.btnPublic.setOnClickListener(v-> {
            Intent intent = new Intent(v.getContext(), CreatePublicCommunityActivity.class);
            startActivity(intent);
        });
        binding.btnPrivate.setOnClickListener(v-> {
            Intent intent = new Intent(v.getContext(), CreatePrivateCommunityActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getPublicCommunities();
        getPrivateCommunities();
    }

//    public void searchPublicCommunities(String searchString) {
////        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
//
//
//        communityReference.whereEqualTo("is_public", true).addSnapshotListener((value, error) -> {
//            if (error != null) {
//                // Handle error
//                showErrorMessage();
//                return;
//            }
//
//            if (value != null) {
//                List<CommunityModel> communities = new ArrayList<>();
//
//                for (QueryDocumentSnapshot documentSnapshot : value) {
//                    String communityName = documentSnapshot.getString("name");
//
//                    // Check if the community name matches the search pattern using regex
//                    if (communityName != null && communityName.toLowerCase().matches(".*\\b" + escapeRegex(searchString) + "\\b.*")) {
//                        MemberModel member = new MemberModel();
////                                member = null;
//                        members.add(member);
//                        CommunityModel community = new CommunityModel();
//                        community.setName(communityName);
//                        community.setDp_url(documentSnapshot.getString("dp_url"));
//                        community.setIs_public(Boolean.TRUE.equals(documentSnapshot.getBoolean("is_public")));
//                        community.setMembers(members);
//                        community.setId(documentSnapshot.getId());
//                        communities.add(community);
//                    }
//                }
//
//                if (communities.size() > 0) {
//                    Community_cardAdapter community_cardAdapter = new Community_cardAdapter(communities, this);
//                    GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false);
//                    layoutManager.setReverseLayout(true);
//                    binding.searchPublicCommunityRecyclerView.setLayoutManager(layoutManager);
//                    binding.searchPublicCommunityRecyclerView.setAdapter(community_cardAdapter);
//                    binding.searchPublicCommunityRecyclerView.setVisibility(View.VISIBLE);
//                    binding.spacing2.setVisibility(View.VISIBLE);
//                } else {
//                    showErrorMessage();
//                }
//            }
//        });
//    }

    // Helper method to escape special characters in the search string for regex
    private String escapeRegex(String input) {
        return input.replaceAll("[.*+?^${}()|[\\]\\\\]]", "\\\\$0");
    }


    public void getPublicCommunities(){
        communityReference.whereEqualTo("is_public", true).get().addOnCompleteListener(v-> {
            if (v.isSuccessful() && v.getResult() != null) {
                List<CommunityModel> communitys = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot: v.getResult()){
                                MemberModel member = new MemberModel();
//                                member = null;
                                members.add(member);
                                System.out.println("ok show the public com");
                                CommunityModel community = new CommunityModel();
                                community.setName(queryDocumentSnapshot.getString("name"));
                                community.setDp_url(queryDocumentSnapshot.getString("dp_url"));
                                community.setMembers(members);
                                community.setIs_public(Boolean.TRUE.equals(queryDocumentSnapshot.getBoolean("is_public")));
                                community.setId(queryDocumentSnapshot.getId());
                                community.setAdmin(null);
                                communitys.add(community);
                                System.out.println(communitys);
                                System.out.println(communitys.size());
//                            }

                            System.out.println(communitys.size());
                            if(communitys.size() > 0){
                                System.out.println("Community not null");
                                Community_cardAdapter community_cardAdapter = new Community_cardAdapter(communitys, this);
                                GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false);
                                //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
                                layoutManager.setReverseLayout(true);
                                binding.publicCommunityRecyclerView.setLayoutManager(layoutManager);
                                binding.publicCommunityRecyclerView.setAdapter(community_cardAdapter);
                                binding.publicCommunityRecyclerView.setVisibility(View.VISIBLE);

                            }else{
                                showErrorMessage("no public communities");
                            }
//                        }
//                    }
//                    );
                }
            }else{
                showErrorMessage("no public communities");
            }
        });
    }

    public void getPrivateCommunities(){
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        communityReference.whereEqualTo("is_public", false).get().addOnCompleteListener(v-> {
            if (v.isSuccessful() && v.getResult() != null) {
                List<CommunityModel> communitys = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot: v.getResult()){
                    communityReference.document(queryDocumentSnapshot.getId()).collection("members").get().addOnCompleteListener(v1 -> {
                        for (QueryDocumentSnapshot queryDocumentSnapshot1: v1.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot1.getId())){
                                MemberModel member = new MemberModel();
                                member.id = queryDocumentSnapshot1.getId();
                                member.dp = queryDocumentSnapshot1.getString("dp");
                                member.name = queryDocumentSnapshot1.getString("name");
                                member.canSendText = Boolean.TRUE.equals(queryDocumentSnapshot1.getBoolean("canSendText"));
                                member.admin = Boolean.TRUE.equals(queryDocumentSnapshot1.getBoolean("admin"));
                                members.add(member);


                                System.out.println("ok show the prive com");
                                CommunityModel community = new CommunityModel();
                                community.setName(queryDocumentSnapshot.getString("name"));
                                community.setDp_url(queryDocumentSnapshot.getString("dp_url"));
                                community.setMembers(members);
                                community.setIs_public(Boolean.TRUE.equals(queryDocumentSnapshot.getBoolean("is_public")));
                                community.setId(queryDocumentSnapshot.getId());
                                community.setAdmin(queryDocumentSnapshot.getString("admin"));
                                communitys.add(community);
                                System.out.println(communitys);
                                System.out.println(communitys.size());
                            }

                            System.out.println(communitys.size());
                            if(communitys.size() > 0){
                                System.out.println("Community not null");
                                Community_cardAdapter community_cardAdapter = new Community_cardAdapter(communitys, this);
                                GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                                //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
                                layoutManager.setReverseLayout(true);
                                binding.privateRecyclerView.setLayoutManager(layoutManager);
                                binding.privateRecyclerView.setAdapter(community_cardAdapter);
                                binding.privateRecyclerView.setVisibility(View.VISIBLE);

                            }else{
                                showErrorMessage("no private communities");
                            }
                        }
                    });
                }
            }else{
                showErrorMessage("no private communities");
            }
        });
    }



    public void showErrorMessage(String text){
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }

//    public void getModels(){}

    @Override
    public void onCommunityClicked(CommunityModel community) {
        Intent intent = new Intent(requireActivity(), GroupchatActivity.class);
        intent.putExtra("community", community);
        startActivity(intent);
    }
}


