package com.eou.screentalker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;

public class PlayActivity extends AppCompatActivity {

    private StyledPlayerView styledPlayerView;
    private ExoPlayer exoPlayer;
    private String VIDEO_URL;
    private String VIDEO_TITLE;
    private VideoView videoView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        VIDEO_URL = getIntent().getStringExtra("vid");
        VIDEO_TITLE = getIntent().getStringExtra("title");
        System.out.println("got here");
        System.out.println(VIDEO_URL);
        System.out.println(VIDEO_TITLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(VIDEO_TITLE);

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        TextView internetStatus = (TextView) findViewById(R.id.internet_status);
//        if (!isOnline(this)) {
//            System.out.println("No internet connection.");
//            internetStatus.setText("No internet connection.");
//        } else {
//            System.out.println("Connected to internet.");
//            internetStatus.setText("Connected to internet.");
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        progressBar = findViewById(R.id.progressBar);


        videoView = findViewById(R.id.video_view);



//    private void playVideo() {
        Uri videoUri = Uri.parse(VIDEO_URL);
        videoView.setVideoURI(videoUri);
        videoView.setVisibility(View.VISIBLE);
//        videoView.getBufferPercentage();
//        videoView.getDuration();
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
//        progressBar.setVisibility(View.VISIBLE);
//        videoView.setOnPreparedListener(mp -> progressBar.setVisibility(View.GONE));
        videoView.setOnPreparedListener(mp -> mp.setVolume(1,1));


//        videoView = findViewById(R.id.video_view);
//        Uri videoUri = Uri.parse(VIDEO_URL);
//        videoView.setVideoURI(videoUri);
//        videoView.setVisibility(View.VISIBLE);
////        System.out.println("got here");
//        System.out.println(VIDEO_URL);
//        System.out.println("please work");
//        videoView.start();










//        styledPlayerView = findViewById(R.id.video_player);
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(getApplicationContext()).build();
//        RenderersFactory renderersFactory = new DefaultRenderersFactory(this);
//        TrackSelector trackSelector = new DefaultTrackSelector(this);
//        exoPlayer = new ExoPlayer.Builder(this).setRenderersFactory(renderersFactory).setTrackSelector(trackSelector).setBandwidthMeter(bandwidthMeter).build();
//        styledPlayerView.setPlayer(exoPlayer);
//
////        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)));
//         String userAgent = Util.getUserAgent(this, getString(R.string.app_name));
//        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)));;
////        String appName = getString(R.string.app_name);
////        dataSourceFactory.getDefaultRequestProperties().set("App Name", appName);
//        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4")));
//        if(VIDEO_URL == null){
//            System.out.println("why me abeg");
//        }
//        System.out.println(VIDEO_URL);
//        exoPlayer.prepare();
//        exoPlayer.setPlayWhenReady(true);


//        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory());
//        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
//        TrackSelector trackSelector =
//                new DefaultTrackSelector(videoTrackSelectionFactory);
//
//        av = new AdaptiveTrackSelection.Factory();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //when activity is destroied realease the player
//        finish();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //back on button click
        if(item.getItemId() == android.R.id.home){
            super.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}