package com.eou.screentalker.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eou.screentalker.Adapters.PartAdapter;
import com.eou.screentalker.Models.DataModel;
import com.eou.screentalker.Adapters.FeaturedAdapter;
import com.eou.screentalker.Models.FeaturedModel;
import com.eou.screentalker.Models.PartModel;
import com.eou.screentalker.R;
import com.eou.screentalker.Adapters.SeriesAdapter;
import com.eou.screentalker.Models.SeriesModel;
import com.eou.screentalker.Adapters.SliderAdapter;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

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
    private RecyclerView recommendedRecyclerView;
    private PreferenceManager preferenceManager;
    private CollectionReference documentReference;
    private CollectionReference partRef;
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

        featuredRecyclerView = view.findViewById(R.id.privateRecyclerView);
        seriesRecyclerView = view.findViewById(R.id.seriesRecyclerView);
        recommendedRecyclerView = view.findViewById(R.id.recommendedRecyclerView);


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
        recommendMovies();
//        getRecommendations();

    }

    @Override
    public void onResume() {
        recommendMovies();
        super.onResume();
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

    public void recommendMovies(){
        String currentUserid = preferenceManager.getString(Constants.KEY_USER_ID);
        partRef = fStore.collection("watched").document(currentUserid).collection("parts");
        // Get documents asynchronously using a listener
        partRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Process retrieved documents and group them by type (explained below)
                     List<DocumentSnapshot> documents = task.getResult().getDocuments();
                     groupDocumentsByType(documents);
                } else {
                     // Handle errors
                    Log.w("Firestore", "Error getting documents:", task.getException());
                }
            }
        });
    }

    private void groupDocumentsByType(List<DocumentSnapshot> documents) {
         // Use a HashMap to store type as key and document count as value
         HashMap<String, Integer> typeCountMap = new HashMap<>();

        for (DocumentSnapshot doc : documents) {
            String type = doc.getString("type");
            if (type != null) {
                // Get existing count for this type, or initialize to 0
                int count = typeCountMap.getOrDefault(type, 0);
                count++;
                typeCountMap.put(type, count);
             }
        }
        // Map where each key (type) has the number of documents with that type
        findMostFrequentType(typeCountMap);
    }

    private void findMostFrequentType(HashMap<String, Integer> typeCountMap) {
         String mostFrequentType = null;
        int maxCount = 0;

         for (Map.Entry<String, Integer> entry : typeCountMap.entrySet()) {
            String type = entry.getKey();
             int count = entry.getValue();

             if (count > maxCount) {
                 mostFrequentType = type;
                 maxCount = count;
             }
        }
        Log.d("Result", "Most frequent type: " + mostFrequentType + ", Count: " + maxCount);
         getRecommendations(mostFrequentType);
    }





    private void getRecommendations(String genre) {
        // Get a reference to the "links" collection
        DatabaseReference linksRef = FirebaseDatabase.getInstance().getReference("link");

        // Create empty ArrayLists to store recommendations
        final ArrayList<PartModel> possibleRecommendations = new ArrayList<>();
        final ArrayList<PartModel> recommendations = new ArrayList<>();

        // Set up RecyclerView layout manager (replace with your desired layout)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recommendedRecyclerView.setLayoutManager(layoutManager);

        // Create and set adapter
        PartAdapter partAdapter = new PartAdapter(recommendations, requireActivity());
        recommendedRecyclerView.setAdapter(partAdapter);

        // Attach a value event listener to the reference
        linksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                possibleRecommendations.clear(); // Clear previous data

                for (DataSnapshot contentSnapshot : snapshot.getChildren()) {
                    String flinkId = contentSnapshot.getKey();
                    Log.d("flinkid", flinkId);
                    for (DataSnapshot childSnapshot : contentSnapshot.getChildren()) { // Loop through all child nodes of the current "flink"
                        String childKey = childSnapshot.getKey(); // Get the child node key (e.g., "0", "1", "2")
                        Log.d("childkey", childKey);

                        if (childSnapshot.exists()) {
                            String type = childSnapshot.child("type").getValue(String.class); // Assuming "type" is present in all child nodes

                            if (type != null && type.equals(genre)) {
                                PartModel model = childSnapshot.getValue(PartModel.class);
                                possibleRecommendations.add(model);
                                Log.d("possiblerecommendations", String.valueOf(possibleRecommendations.size()));
                            }
                        }
                    }
                }

                // Select and display a random recommendation (if any)
                if (possibleRecommendations.size() > 0) {
                    int randomIndex = new Random().nextInt(possibleRecommendations.size());
                    PartModel recommendedMovie = possibleRecommendations.get(randomIndex);
                    recommendations.clear(); // Clear existing recommendations
                    recommendations.add(recommendedMovie);
                    partAdapter.notifyDataSetChanged();
                } else {
                    // Handle the case where there are no comedy movies (optional)
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Log.w("Firebase", "Error getting data:", error.toException());
            }
        });
    }



}