package com.eou.screentalker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.eou.screentalker.Adapters.CommentAdapter;
import com.eou.screentalker.Adapters.Group_chatAdapter;
import com.eou.screentalker.Fragments.CommentFragment;
import com.eou.screentalker.Models.CommentModel;
import com.eou.screentalker.Models.Group_chat_messageModel;
import com.eou.screentalker.R;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.ActivityGroupchatBinding;
import com.eou.screentalker.databinding.ActivityPlayBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class PlayActivity extends AppCompatActivity {

//    public static final String VIDEO_TITLE;
    private String VIDEO_URL;
    ProgressDialog progressDialog;
    private ActivityPlayBinding binding;
    String VIDEO_TITLE;
    private PreferenceManager preferenceManager;

//    public void setupToolbar(String title) {
//        // Set toolbar title and other properties
//        getSupportActionBar().setTitle(title);
//        // ... (rest of toolbar setup)
//    }
//
//    public void loadAndPlayVideo(String videoUrl) {
//        // Get video view and progress dialog references
//        VideoView videoView = findViewById(R.id.video_view);
//        ProgressDialog progressDialog = new ProgressDialog(this);
//
//        // Set video URI and visibility
//        videoView.setVideoURI(Uri.parse(videoUrl));
//        videoView.setVisibility(View.VISIBLE);
//
//        // ... (rest of video loading and playback logic)
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);

        FrameLayout commentContainer = findViewById(R.id.comment_container);
        CommentFragment commentFragment = new CommentFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(commentContainer.getId(), commentFragment);
        transaction.commit();


//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        progressDialog = new ProgressDialog(PlayActivity.this);


        VIDEO_URL = getIntent().getStringExtra("vid");
        VIDEO_TITLE = getIntent().getStringExtra("title");
        commentFragment.title = VIDEO_TITLE;
        preferenceManager.putString(Constants.KEY_TITLE, VIDEO_TITLE);
        System.out.println("got here");
        System.out.println(VIDEO_URL);
        System.out.println(VIDEO_TITLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView imageback = findViewById(R.id.imageBack);
        imageback.setOnClickListener(v->onBackPressed());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(VIDEO_TITLE);

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_baseline_arrow_back_ios_24);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading.....");
        progressDialog.setTitle("Loading");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        VideoView videoView = findViewById(R.id.video_view);
        Uri videoUri = Uri.parse(VIDEO_URL);
        videoView.setVideoURI(videoUri);
        videoView.setVisibility(View.VISIBLE);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
//        mediaController.set
        videoView.setOnPreparedListener(mp -> progressDialog.dismiss());
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        //back on button click
//        if(item.getItemId() == android.R.id.home){
//            super.finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }


}