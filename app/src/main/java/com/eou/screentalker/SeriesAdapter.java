package com.eou.screentalker;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
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

        //linking to the detail activity for the series
        holder.itemView.setOnClickListener(v -> {
            // to send user to details activity when they click on movie card using intent
            Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(),DetailsActivity.class);
            sendDataToDetailsActivity.putExtra("title", seriesModels.get(position).getStitle());
            sendDataToDetailsActivity.putExtra("link", seriesModels.get(position).getSlink());
            sendDataToDetailsActivity.putExtra("cover", seriesModels.get(position).getScover());
            sendDataToDetailsActivity.putExtra("thumb", seriesModels.get(position).getSthumb());
            sendDataToDetailsActivity.putExtra("des", seriesModels.get(position).getSdes());
            sendDataToDetailsActivity.putExtra("cast", seriesModels.get(position).getScast());
            sendDataToDetailsActivity.putExtra("t_link", seriesModels.get(position).getTlink());

            //creating transition animation
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (Activity) holder.itemView.getContext(),holder.imageView,"ImageMain");
            //sharedElementsName is same as xml file
            holder.itemView.getContext().startActivity(sendDataToDetailsActivity,optionsCompat.toBundle());
        });
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
