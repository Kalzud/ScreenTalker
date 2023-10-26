package com.eou.screentalker.Activities;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eou.screentalker.Fragments.CommunityFragment;
import com.eou.screentalker.R;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.eou.screentalker.databinding.ActivityCreatePublicCommunityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreatePublicCommunityActivity extends AppCompatActivity {

    private ActivityCreatePublicCommunityBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;



    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private CollectionReference collectionReference;

    //variables used for stuff in code
    private Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePublicCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        collectionReference = fStore.collection("communities");




        //register for activity we would use in openSomeActivityForResult
        someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK){
                //no request codes
                Intent data = result.getData();
                assert data != null;
                imageUri = data.getData();
//                pImage.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        });

        //get picture from gallery
        binding.createCommunity.setOnClickListener(this::openSomeActivityForResult);

        //toolbar settings
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Create Public Community");
        //go back
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void openSomeActivityForResult (View v){
        //open gallery and create intent
        //First part is for picking the image and second part is to get the image uri from gallery
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //launch activity to get result
        someActivityResultLauncher.launch(openGalleryIntent);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //creating the collection

        final String ranKey = UUID.randomUUID().toString();
        //upload to firebase storage
        StorageReference fileRef = storageReference.child("communityDp/" + ranKey + ".png");

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                createCommunity(uri);
                Picasso.get().load(uri).into(binding.communityImg);
            });
            Toast.makeText(CreatePublicCommunityActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(CreatePublicCommunityActivity.this, "Image not uploaded", Toast.LENGTH_SHORT).show());
    }

    public void createCommunity(Uri uri){
        Map<String, Object> communities = new HashMap<>();
        communities.put("dp_url", uri);
        communities.put("is_public", true);
        communities.put("name", binding.communityName.getText().toString());
        collectionReference.add(communities).addOnSuccessListener(v-> Log.d(TAG, "onsuccess: community updated for"));
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