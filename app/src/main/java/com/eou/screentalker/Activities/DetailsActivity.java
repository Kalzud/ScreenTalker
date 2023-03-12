package com.eou.screentalker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eou.screentalker.Adapters.CastAdapter;
import com.eou.screentalker.Adapters.PartAdapter;
import com.eou.screentalker.Models.CastModel;
import com.eou.screentalker.Models.PartModel;
import com.eou.screentalker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private List<CastModel> castModels;
    private List<PartModel> partModels;
    private CastAdapter castAdapter;
    private PartAdapter partAdapter;

    private RecyclerView part_recyclerView, cast_recyclerView;

    private ImageView thumb, cover;
    private TextView title, des;
    private FloatingActionButton actionButton;
    private String title_movie;
    private String des_movie;
    private String thumb_movie;
    private String link_movie;
    private String cover_movie;
    private String cast_movie;
    private String trailer_movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //initialise
        thumb = findViewById(R.id.thumb);
        cover = findViewById(R.id.cover);
        title = findViewById(R.id.title_details);
        des = findViewById(R.id.des);
        actionButton = findViewById(R.id.floatingActionButton2);

        //getting the data through intent
        title_movie = getIntent().getStringExtra("title");
        des_movie = getIntent().getStringExtra("des");
        thumb_movie = getIntent().getStringExtra("thumb");
        link_movie = getIntent().getStringExtra("link");
        cover_movie = getIntent().getStringExtra("cover");
        cast_movie = getIntent().getStringExtra("cast");
        trailer_movie = getIntent().getStringExtra("t_link");

        cast_recyclerView = findViewById(R.id.recyclerView_cast);
        part_recyclerView = findViewById(R.id.recyclerView_parts);

        //toolbar settings
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title_movie);

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//      getSupportActionBar().setIcon(R.drawable.ic_baseline_arrow_back_ios_24);

        Glide.with(this).load(thumb_movie).into(thumb);
        Glide.with(this).load(cover_movie).into(cover);
        thumb.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));
        cover.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));

        title.setText(title_movie);
        des.setText(des_movie);

        actionButton.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("link").child(trailer_movie).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String vidUrl = snapshot.getValue(String.class);
                    Intent intent = new Intent(DetailsActivity.this,PlayActivity.class);
                    intent.putExtra("vid", vidUrl);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(DetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        loadCast();
        loadPart();
    }

    private void loadPart() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference partRef = database.getReference();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);
        part_recyclerView.setLayoutManager(layoutManager);

        partModels = new ArrayList<>();
//        System.out.println("This is Part model: " + partModels);
        partAdapter = new PartAdapter(partModels);
        part_recyclerView.setAdapter( partAdapter);
        partRef.child("link").child(link_movie).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot contentSnapShot:snapshot.getChildren()){
                    PartModel partModel = contentSnapShot.getValue(PartModel.class);
                    partModels.add(partModel);
                }
                partAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                error code
            }
        });
    }

    private void loadCast() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference castRef = database.getReference();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        //to reverse layout cause I want to display from the first position so I need the reverse of 3 2 1 0
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        cast_recyclerView.setLayoutManager(layoutManager);

        castModels = new ArrayList<>();

        castAdapter = new CastAdapter(castModels);
        cast_recyclerView.setAdapter(castAdapter);
        castRef.child("cast").child(cast_movie).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot contentSnapShot:snapshot.getChildren()){
                    CastModel castModel = contentSnapShot.getValue(CastModel.class);
                    castModels.add(castModel);
//                    System.out.println("This is cast model: " + castModels);
                }
                castAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                error code
            }
        });
    }

    //when back button is pressed

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}