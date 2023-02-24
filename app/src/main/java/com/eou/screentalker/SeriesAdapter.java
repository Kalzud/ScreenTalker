package com.eou.screentalker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MyViewHolder> {
    private List<SeriesModel> seriesModels;


    public SeriesAdapter(List<SeriesModel> seriesModels) {
        this.seriesModels = seriesModels;
    }

    @NonNull
    @Override
    public SeriesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(seriesModels.get(position).getStitle());
        // here we load thumbnails
        Glide.with(holder.itemView.getContext()).load(seriesModels.get(position).getSthumb()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return seriesModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.movie_title);
        }
    }
}
