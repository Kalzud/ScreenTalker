package com.eou.screentalker.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Models.CommunityModel;
import com.eou.screentalker.Models.RequestModel;
import com.eou.screentalker.R;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.InviteCardBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder>{
    private final List<RequestModel> requestModels;
    private FirebaseFirestore fStore;
    private PreferenceManager preferenceManager;
    private Context context;

    public RequestAdapter(List<RequestModel> requestModels, Context context) {
        this. requestModels =  requestModels;
        this.context = context;
        fStore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(context);
    }

    @NonNull
    @Override
    public RequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InviteCardBinding inviteCardBinding = InviteCardBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new RequestAdapter.MyViewHolder(inviteCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.MyViewHolder holder, int position) {
        holder.setRequestData(requestModels.get(position));
    }

    @Override
    public int getItemCount() {
        return requestModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        InviteCardBinding  binding;
        MyViewHolder(InviteCardBinding  inviteCardBinding) {
            super(inviteCardBinding.getRoot());
            binding = inviteCardBinding;
        }

        void setRequestData(RequestModel request){
            binding.name.setText(request.sender_username);
            Picasso.get().load(Uri.parse(request.sender_pImage)).into(binding.dp);
            binding.accept.setOnClickListener(v-> add(request));
            binding.reject.setOnClickListener(v-> remove(request));
        }

        private  void add(RequestModel request){
            HashMap<String, Object> friend = new HashMap<>();
            friend.put("friend_id", request.sender_id);
            System.out.println(request.sender_id);
            friend.put("friend_pImage", request.sender_pImage);
            friend.put("friend_username", request.sender_username);
            friend.put("person_id", preferenceManager.getString(Constants.KEY_USER_ID));
            friend.put("person_pImage", preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
            friend.put("person_username", preferenceManager.getString(Constants.KEY_NAME));
            fStore.collection("friends").add(friend);
            fStore.collection("requests").document(request.id).delete();
            binding.accept.setVisibility(View.GONE);
        }

        private void remove(RequestModel request){
            fStore.collection("requests").document(request.id).delete();
            binding.accept.setVisibility(View.GONE);
            binding.reject.setVisibility(View.GONE);
        }
    }



//    // Set the click listener for the reload button
//        reloadButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            // Create a new instance of the fragment
//            MyFragment newFragment = new MyFragment();
//
//            // Get the fragment manager
//            FragmentManager fragmentManager = getFragmentManager();
//
//            // Start a new fragment transaction
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//            // Replace the existing fragment with the new instance
//            fragmentTransaction.replace(R.id.fragment_container, newFragment);
//
//            // Commit the transaction
//            fragmentTransaction.commit();
//        }
//    });

//    // Initialize Firestore instance
//    Firestore firestore = FirestoreClient.getFirestore();
//
//    // Specify the document reference
//    DocumentReference docRef = firestore.collection("myCollection").document("myDocument");
//
//    // Update the document
//    ApiFuture<WriteResult> future = docRef.update("myField", FieldValue.delete());
//
//// Wait for the update to complete
//try {
//        future.get();
//        System.out.println("Field successfully deleted.");
//    } catch (InterruptedException | ExecutionException e) {
//        System.out.println("Error deleting field: " + e.getMessage());
//    }

}
