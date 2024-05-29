package com.eou.screentalker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Activities.ProfileActivity;
import com.eou.screentalker.Listeners.RequestActionListener;
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
    private RequestActionListener requestActionListener;

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
            Picasso.get().load(Uri.parse(request.sender_pImage))
                    .resize(200, 200)  // Target dimensions
                    .centerCrop()  // Maintain aspect ratio
                    .placeholder(R.drawable.image_placeholder)  // Placeholder while loading
                    .error(R.drawable.ic_baseline_image_not_supported_24)  // Image for loading errors
                    .config(Bitmap.Config.RGB_565)  // Reduce memory usage (optional)
                    .into(binding.dp);
            binding.accept.setOnClickListener(v-> add(request));
            binding.reject.setOnClickListener(v-> remove(request));
            binding.dp.setOnClickListener(v ->{
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("id", request.sender_id);
                itemView.getContext().startActivity(intent);
            });
        }

        private  void add(RequestModel request){
            HashMap<String, Object> friend = new HashMap<>();
            friend.put("friend_id", request.sender_id);
            System.out.println(request.sender_id);
            friend.put("friend_pImage", request.sender_pImage);
            friend.put("friend_username", request.sender_username);
            friend.put("friend_token", request.sender_token);
            friend.put("person_id", preferenceManager.getString(Constants.KEY_USER_ID));
            friend.put("person_pImage", preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
            friend.put("person_username", preferenceManager.getString(Constants.KEY_NAME));
            friend.put("person_token", preferenceManager.getString(Constants.KEY_FCM_TOKEN));
            fStore.collection("friends").add(friend);
            fStore.collection("requests").document(request.id).delete();
            binding.accept.setVisibility(View.GONE);
            binding.reject.setVisibility(View.GONE);
        }

        private void remove(RequestModel request){
            fStore.collection("requests").document(request.id).delete();
            binding.accept.setVisibility(View.GONE);
            binding.reject.setVisibility(View.GONE);
        }
    }
}
