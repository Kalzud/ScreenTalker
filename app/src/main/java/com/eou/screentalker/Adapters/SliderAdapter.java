package com.eou.screentalker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eou.screentalker.Models.DataModel;
import com.eou.screentalker.Activities.PlayActivity;
import com.eou.screentalker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

/**
 *  Author: Emmanuel O. Uduma
 *
 * This class would act as a bridge between
 * the slide and data to be shown on the alide.
 * With the viewHolder here we could get access to the
 * findViewById of elements and then bind and show them.
 * WE could get the data of the DataModel objects and also sho its position
 */
public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MyViewHolder> {

    private Context context;
    private List<DataModel> dataModels;

    public SliderAdapter(Context context){
        this.context = context;
    }
    public SliderAdapter(Context context, List<DataModel> dataModels){
        this.context = context;
        this.dataModels = dataModels;
    }


    //Renew data showing on slider
    public void renewItems(List<DataModel> dataModels){
        setDataModels(dataModels);
//        this.dataModels = dataModels;
        notifyDataSetChanged();
    }



    public List<DataModel> getDataModels() {
        return dataModels;
    }


    private void setDataModels(List<DataModel> dataModels) {
        this.dataModels = dataModels;
    }

    //remove current data on slider
    public void deleteItems(int position){
       remove(position);
        notifyDataSetChanged();
    }

    private void remove(int position){
        this.dataModels.remove(position);
    }

    //show the slider layout
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
//        to inflate layout for slide items here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new MyViewHolder(view);
    }

    //set text and thumbnail based on DataModel object
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {

        viewHolder.trailer_title.setText(dataModels.get(position).getTtitle());
        Glide.with(viewHolder.itemView).load(dataModels.get(position).getTurl()).into(viewHolder.slider_image);
        //play video
        viewHolder.play_button.setOnClickListener(v -> {
            Intent trailer_video = new Intent(context, PlayActivity.class);
            //get title and vid
            trailer_video.putExtra("vid",dataModels.get(position).getTvid());
            trailer_video.putExtra("title",dataModels.get(position).getTtitle());
            System.out.println("in here");
            System.out.println(context);
            //play vid?
            v.getContext().startActivity(trailer_video);
        });
    }

    @Override
    public int getCount() {
        return dataModels.size();
    }

    //get the ids and assign them so they can be used for binding
    public class MyViewHolder extends SliderViewAdapter.ViewHolder{
        ImageView slider_image;
        TextView trailer_title;
        FloatingActionButton play_button;

        public MyViewHolder(View itemView) {
            super(itemView);
            slider_image = itemView.findViewById(R.id.image_thumbnail);
            trailer_title = itemView.findViewById(R.id.trailer_title);
            play_button = itemView.findViewById(R.id.floatingActionButton);
        }
    }

}
