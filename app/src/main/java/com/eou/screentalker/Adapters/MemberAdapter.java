package com.eou.screentalker.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eou.screentalker.Listeners.MemberListener;
import com.eou.screentalker.Models.FriendModel;
import com.eou.screentalker.Models.MemberModel;
import com.eou.screentalker.databinding.ContainerUserBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder>{
    private final List<MemberModel> members;
    private final MemberListener memberListener;

    public MemberAdapter(List<MemberModel> members, MemberListener memberListener) {
        this.members = members;
        this.memberListener = memberListener;
    }

    @NonNull
    @Override
    public MemberAdapter.MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContainerUserBinding containerUserBinding = ContainerUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MemberAdapter.MemberViewHolder(containerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.MemberViewHolder holder, int position) {
        holder.setMemberData(members.get(position));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder{
        ContainerUserBinding binding;
        MemberViewHolder(ContainerUserBinding containerUserBinding){
            super(containerUserBinding.getRoot());
            binding = containerUserBinding;
        }

        void setMemberData(MemberModel member){
            binding.textName.setText(member.name);
            //            binding.pImage.setImageURI(Uri.parse(friend.getpImage_url()));
            Picasso.get().load(Uri.parse(member.dp)).into(binding.pImage);
            binding.getRoot().setOnClickListener(v-> memberListener.onMemberClicked(member));
        }
    }
}
