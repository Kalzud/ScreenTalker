package com.eou.screentalker.Adapters;

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
import com.eou.screentalker.Activities.DetailsActivity;
import com.eou.screentalker.Models.FeaturedModel;
import com.eou.screentalker.R;

import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.MyViewHolder> {
        private List<FeaturedModel> featuredModels;

        public FeaturedAdapter(List<FeaturedModel>featuredModels) {
                this.featuredModels = featuredModels;
        }

        @NonNull
        @Override
        public FeaturedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
                return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FeaturedAdapter.MyViewHolder holder, int position) {
                holder.textView.setText(featuredModels.get(position).getFtitle());
                // here we load thumbnails
                Glide.with(holder.itemView.getContext()).load(featuredModels.get(position).getFthumb()).into(holder.imageView);

                //linking to the detail activity for the movie
                holder.itemView.setOnClickListener(v -> {
                        // to send user to details activity when they click on movie card using intent
                        Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                        sendDataToDetailsActivity.putExtra("title", featuredModels.get(position).getFtitle());
                        sendDataToDetailsActivity.putExtra("link", featuredModels.get(position).getFlink());
                        sendDataToDetailsActivity.putExtra("cover", featuredModels.get(position).getFcover());
                        sendDataToDetailsActivity.putExtra("thumb", featuredModels.get(position).getFthumb());
                        sendDataToDetailsActivity.putExtra("des", featuredModels.get(position).getFdes());
                        sendDataToDetailsActivity.putExtra("cast", featuredModels.get(position).getFcast());
                        sendDataToDetailsActivity.putExtra("t_link", featuredModels.get(position).getTlink());

                        //creating transition animation
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                (Activity) holder.itemView.getContext(),holder.imageView,"ImageMain");
                        //sharedElementsName is same as xml file
                        holder.itemView.getContext().startActivity(sendDataToDetailsActivity,optionsCompat.toBundle());
                });
        }

        @Override
        public int getItemCount() {
                return featuredModels.size();
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
