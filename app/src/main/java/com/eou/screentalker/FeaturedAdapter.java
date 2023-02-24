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
