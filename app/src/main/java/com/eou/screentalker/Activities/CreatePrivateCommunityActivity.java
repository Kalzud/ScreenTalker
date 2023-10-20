package com.eou.screentalker.Activities;

import android.os.Bundle;
import com.eou.screentalker.Utilities.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import com.eou.screentalker.databinding.ActivityCreatePrivateCommunityBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreatePrivateCommunityActivity extends AppCompatActivity {

    private ActivityCreatePrivateCommunityBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePrivateCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        fStore = FirebaseFirestore.getInstance();
    }

}