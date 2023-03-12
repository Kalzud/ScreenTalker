package com.eou.screentalker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eou.screentalker.Models.CastModel;
import com.eou.screentalker.R;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder>{
    List<CastModel> castModels;

    public CastAdapter(List<CastModel> castModels) {
        this.castModels = castModels;
    }

    @NonNull
    @Override
    public CastAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cast_name.setText(castModels.get(position).getCname());
//        System.out.println("This is cast model: " + castModels);
        Glide.with(holder.itemView).load(castModels.get(position).getCimageurl()).into(holder.cast_image);
//        Glide.with(holder.itemView.getContext()).load(featuredModels.get(position).getFthumb()).into(holder.imageView)
    }

    @Override
    public int getItemCount() {
        return castModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView cast_image;
        private TextView cast_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cast_image = itemView.findViewById(R.id.cast_image);
            cast_name = itemView.findViewById(R.id.cast_name);
        }
    }
}
