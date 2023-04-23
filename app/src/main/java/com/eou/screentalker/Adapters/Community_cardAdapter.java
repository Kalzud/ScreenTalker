package com.eou.screentalker.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eou.screentalker.Listeners.CommunityListener;
import com.eou.screentalker.Models.CommunityModel;
import com.eou.screentalker.Models.RequestModel;
import com.eou.screentalker.databinding.CommunityCardBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Community_cardAdapter extends RecyclerView.Adapter<Community_cardAdapter.MyViewHolder>{
        private final List<CommunityModel> communityModels;
        private  final CommunityListener communityListener;

    public Community_cardAdapter(List<CommunityModel> communityModels, CommunityListener communityListener) {
            this.communityModels = communityModels;
            this.communityListener = communityListener;
            }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommunityCardBinding communityCardBinding = CommunityCardBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(communityCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
          holder.setCommunityData(communityModels.get(position));
    }

    @Override
    public int getItemCount() {
            return communityModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CommunityCardBinding binding;
        MyViewHolder(CommunityCardBinding communityCardBinding) {
            super(communityCardBinding.getRoot());
            binding = communityCardBinding;
        }

        void setCommunityData(CommunityModel community){
            binding.groupTitle.setText(community.getName());
//            binding.pImage.setImageURI(Uri.parse(user.getpImage_url()));
//            System.out.println(community.getDp_url());
//            Glide.with(itemView).load(Uri.parse(community.getDp_url())).into(binding.groupDp);
            Picasso.get().load(Uri.parse(community.getDp_url())).into(binding.groupDp);
            binding.getRoot().setOnClickListener(v-> communityListener.onCommunityClicked(community));
        }

    }
}
