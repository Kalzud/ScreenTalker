package com.eou.screentalker.Adapters;

import android.content.Intent;
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

import java.util.List;

public class PartAdapter extends RecyclerView.Adapter<PartAdapter.MyViewHolder> {
    List<PartModel> partModels;

    public PartAdapter(List<PartModel> partModels) {
        this.partModels = partModels;
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
        //to play movie parts
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlayActivity.class);
            intent.putExtra("title", partModels.get(position).getPart());
            intent.putExtra("vid", partModels.get(position).getVidurl());
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
