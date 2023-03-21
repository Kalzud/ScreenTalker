package com.eou.screentalker.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eou.screentalker.Adapters.UsersAdapter;
import com.eou.screentalker.Models.DataModel;
import com.eou.screentalker.Adapters.FeaturedAdapter;
import com.eou.screentalker.Models.FeaturedModel;
import com.eou.screentalker.Models.UserModel;
import com.eou.screentalker.R;
import com.eou.screentalker.Adapters.SeriesAdapter;
import com.eou.screentalker.Models.SeriesModel;
import com.eou.screentalker.Adapters.SliderAdapter;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *  Author: Emmanuel O. Uduma
 *
 * THis class would be the controller of the stream page
 * and would include code to enable various functions on the page
 *
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class StreamFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private List<DataModel> dataModels;
    private List<FeaturedModel> featuredModels;
    private List<SeriesModel> seriesModels;
    private SliderAdapter sliderAdapter;
    private FeaturedAdapter featuredAdapter;
    private SeriesAdapter seriesAdapter;
    private RecyclerView featuredRecyclerView;
    private RecyclerView seriesRecyclerView;
    private PreferenceManager preferenceManager;
    private CollectionReference documentReference;
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseFirestore fStore;



    public StreamFragment() {
        // Required empty public constructor
    }



    //would run first
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(requireActivity());
        mAuth=FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("users");
    }

    //would run second
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stream, container, false);
    }


    private void loadFirebaseForSlider() {

        myRef.child("trailer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot contextSlider: snapshot.getChildren()){
                    DataModel sliderItem = contextSlider.getValue(DataModel.class);
                    dataModels.add(sliderItem);
                }
                sliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });
    }
//renew items on slider view after swipe with the help of the slider adapter renewIte,s and deleteItems methods
    public void renewItems(View view) {
        dataModels = new ArrayList<>();
        DataModel dataItems = new DataModel();
        dataModels.add(dataItems);
        sliderAdapter.renewItems(dataModels);
        sliderAdapter.deleteItems(0);
    }
//would run third
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        featuredRecyclerView = view.findViewById(R.id.recyclerView);
        seriesRecyclerView = view.findViewById(R.id.seriesRecyclerView);


// get the toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Screen Talker");

        //initialise app with firebase to grant us access to the information on it
        FirebaseApp.initializeApp(requireActivity());

 //get the slider view from xml
        SliderView sliderView = view.findViewById(R.id.sliderView);
        sliderAdapter = new SliderAdapter(requireActivity());
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(6);
        renewItems(sliderView);

//        to load data from firebase
        loadFirebaseForSlider();
        loadFeaturedData();

    }

    private void loadFeaturedData() {
        //load data from firebase here
        DatabaseReference Fref = database.getReference("featured");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        featuredRecyclerView.setLayoutManager(layoutManager);

        featuredModels = new ArrayList<>();
        featuredAdapter = new FeaturedAdapter(featuredModels);
        featuredRecyclerView.setAdapter(featuredAdapter);


        Fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot contentSnapShot:snapshot.getChildren()){
                    FeaturedModel featuredModel = contentSnapShot.getValue(FeaturedModel.class);
                    featuredModels.add(featuredModel);
                }
                featuredAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                 // for error code
            }
        });

        loadSeriesData();
    }

    private void loadSeriesData() {
        //load data from firebase here
        DatabaseReference Sref = database.getReference("series");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        seriesRecyclerView.setLayoutManager(layoutManager);

        seriesModels = new ArrayList<>();
        seriesAdapter = new SeriesAdapter(seriesModels);
        seriesRecyclerView.setAdapter(seriesAdapter);


        Sref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot contentSnapShot:snapshot.getChildren()){
                    SeriesModel seriesModel = contentSnapShot.getValue(SeriesModel.class);
                    seriesModels.add(seriesModel);
                }
                seriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // for error code
            }
        });

    }

//    public void getUsers(){
//        documentReference.get().addOnCompleteListener(querySnapshotTask -> {
//            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
//            if (querySnapshotTask.isSuccessful() && querySnapshotTask.getResult() != null) {
//                List<UserModel> users = new ArrayList<>();
//                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshotTask.getResult()) {
//                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
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
//            }
//        });
//    }


}