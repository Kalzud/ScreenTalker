package com.eou.screentalker;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgsLazyKt;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.eou.screentalker.databinding.FragmentLoginPageBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLoginPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLoginPage extends Fragment {
    private Button playButton;
    private VideoView videoView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentLoginPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLoginPage.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLoginPage newInstance(String param1, String param2) {
        FragmentLoginPage fragment = new FragmentLoginPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_login_page, container, false);
        //set the view
        View view = inflater.inflate(R.layout.fragment_login_page, container, false);
//        create the stream variable of type button and assign to the stream button id
        Button stream = view.findViewById(R.id.stream);
//        assign activity to happen on button click
        stream.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_fragmentLoginPage_to_fragmentMoviePage));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        playButton = view.findViewById(R.id.play_button);
//        videoView = view.findViewById(R.id.video_view);
//
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                playVideo();
//            }
//        });
//    }
//
//    private void playVideo() {
//        Uri videoUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/screentalker-fe07d.appspot.com/o/Videos%2Fscream-6-big-game-spot_h1080p.mov?alt=media&token=ef54ab34-6f16-4b27-b58e-bacce865b2a9");
//        videoView.setVideoURI(videoUri);
//        videoView.setVisibility(View.VISIBLE);
//        videoView.start();
    }

}