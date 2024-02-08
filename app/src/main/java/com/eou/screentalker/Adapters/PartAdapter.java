package com.eou.screentalker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eou.screentalker.Models.PartModel;
import com.eou.screentalker.Activities.PlayActivity;
import com.eou.screentalker.R;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PartAdapter extends RecyclerView.Adapter<PartAdapter.MyViewHolder> {
    List<PartModel> partModels;
    private Context context;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore fStore;
    private CollectionReference collectionReference;

    public PartAdapter(List<PartModel> partModels, Context context) {
        this.partModels = partModels;
        this.context = context;
        preferenceManager = new PreferenceManager(context);
        fStore = FirebaseFirestore.getInstance();
        String currentUserid = preferenceManager.getString(Constants.KEY_USER_ID);
        collectionReference = fStore.collection("watched").document(currentUserid).collection("parts");
    }

    // Helper method to check if the film is already in the watched section
    private void checkIfFilmAlreadyWatched(String filmName, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        // Perform a query to check if the film with the same name already exists
        // Adjust the query based on your Firestore document structure
        Query query = collectionReference.whereEqualTo("name", filmName);

        // Execute the query asynchronously
        query.get().addOnCompleteListener(onCompleteListener);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.part_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.part_name.setText(partModels.get(position).getPart());
        Glide.with(holder.itemView).load(partModels.get(position).getThumburl()).into(holder.part_image);
//        Intent intent = new Intent(v.getContext(), PlayActivity.class);
//        intent.putExtra("title", partModels.get(position).getPart());
//        intent.putExtra("vid", partModels.get(position).getVidurl());
//        holder.itemView.getContext().startActivity(intent);
        //to play movie parts
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlayActivity.class);
            intent.putExtra("title", partModels.get(position).getPart());
            intent.putExtra("vid", partModels.get(position).getVidurl());

            // Check if the film is already in the watched section
            String filmName = partModels.get(position).getPart();
            checkIfFilmAlreadyWatched(filmName, task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Film already watched
                        Log.d("FilmWatched", "Film already in watched section: " + filmName);
                    } else {
                        // If not already watched, add it to Firestore and start the PlayActivity
                        Map<String, Object> filmDetails = new HashMap<>();
                        filmDetails.put("name", filmName);
                        filmDetails.put("thumbnail", partModels.get(position).getThumburl());
                        collectionReference.add(filmDetails);
                    }
                } else {
                    // Handle errors here
                }
            });
            // Start PlayActivity
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return partModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView part_image;
        private TextView part_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            part_image = itemView.findViewById(R.id.part_image);
            part_name = itemView.findViewById(R.id.part_name);
        }
    }


}
