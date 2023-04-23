package com.eou.screentalker.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Models.CommunityModel;
import com.eou.screentalker.Models.RequestModel;
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
        }
    }

    private  void add(RequestModel request){
        HashMap<String, Object> friend = new HashMap<>();
        friend.put("friend_id", request.sender_id);
        System.out.println(request.sender_id);
        friend.put("friend_pImage", request.sender_pImage);
        friend.put("friend_username", request.sender_username);
        friend.put("person_id", preferenceManager.getString(Constants.KEY_USER_ID));
        fStore.collection("friends").add(friend);

    }
}
