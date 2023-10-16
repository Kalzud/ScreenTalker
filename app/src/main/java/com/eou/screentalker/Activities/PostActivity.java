package com.eou.screentalker.Activities;

import static androidx.constraintlayout.widget.Constraints.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.eou.screentalker.R;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {
    //items on xml
    private RoundedImageView postImg;
    private Button post, btnChange_username, btnChange_bio;
    private EditText tag, inputNew_bio;


    //database variables
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private FirebaseUser user;
    private StorageReference storageReference;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private CollectionReference collectionReference;

    //variables used for stuff in code
    private Uri imageUri;
    private String userID;

    //user details for updates
    private String user_username = "";
    private String user_email = "";
    private String user_pImage_url = "";
    private String user_bio = "";
    private String user_dob = "";

    private PreferenceManager preferenceManager;
    private EditprofileActivity.OnRefreshListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        preferenceManager = new PreferenceManager(PostActivity.this);
        //initialising database fields
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        collectionReference = fStore.collection("posts");

        //initialising the items on the xml
        postImg = findViewById(R.id.post_img);
        post = findViewById(R.id.post);
        tag = findViewById(R.id.tag);
//        btnChange_username = findViewById(R.id.btnChange_username);
//        btnChange_bio = findViewById(R.id.btnChange_bio);
//        inputNew_bio = findViewById(R.id.inputNew_bio);


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

//        collectionReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                //setting this to show the current data in database on xml
//                assert value != null;
//                Picasso.get().load(Uri.parse(value.getString("pImage_url"))).into(postImg);
//                tag.setHint(value.getString("username"));
//                inputNew_bio.setHint(value.getString("bio"));
//
//                //setting this variables to content of data in database for updating purposes
//                user_username = value.getString("username");
//                user_email = value.getString("email");
//                user_dob = value.getString("dob");
//                user_bio = value.getString("bio");
//                user_pImage_url = value.getString("pImage_url");
//            }
//        });

        //functions for buttons on xml
        //get picture from gallery
        post.setOnClickListener(v -> {
            openSomeActivityForResult(v);
            if(mListener != null){
                mListener.onRefresh();
            }
        });
//        btnChange_username.setOnClickListener(v -> {
//            if(tag != null){
//                updateUsername();}
//            else {Toast.makeText(PostActivity.this, "You have to put a username", Toast.LENGTH_SHORT).show();}
//        });
//        btnChange_bio.setOnClickListener(v -> {
//            if(inputNew_bio != null)
//                updateBio();
//            else Toast.makeText(PostActivity.this, "You have to put a bio", Toast.LENGTH_SHORT).show();
//        });


        //toolbar settings
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
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
        StorageReference fileRef = storageReference.child("posts/" + ranKey + ".png");

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                postPic(uri);
                Picasso.get().load(uri).into(postImg);
            });
            Toast.makeText(PostActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(PostActivity.this, "Image not uploaded", Toast.LENGTH_SHORT).show());
    }

    //when back button is pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void postPic(Uri uri){
        String tag = this.tag.getText().toString();
        Map<String, Object> post = new HashMap<>();
        post.put("post", uri);
        post.put("tag", tag);
        post.put("id", preferenceManager.getString(Constants.KEY_USER_ID));
        collectionReference.add(post).addOnSuccessListener(v-> Log.d(TAG, "onsuccess: picture updated for" + userID));
    }
//    public void updateUsername(){
//        String tag = this.tag.getText().toString();
//        preferenceManager.putString(Constants.KEY_NAME, tag);
//        System.out.println(Constants.KEY_NAME);
//        Map<String, Object> user = new HashMap<>();
//        user.put("username", tag);
//        collectionReference.update(user).addOnSuccessListener(v-> {
//            Log.d(TAG, "onsuccess: username updated for" + userID);
//            Toast.makeText(PostActivity.this, "Username updated", Toast.LENGTH_SHORT).show();
//        });
//    }
//    public void updateBio(){
//        String new_bio = inputNew_bio.getText().toString();
//        preferenceManager.putString(Constants.KEY_BIO, new_bio);
//        System.out.println(Constants.KEY_BIO);
//        Map<String, Object> user = new HashMap<>();
//        user.put("bio", new_bio);
//        collectionReference.update(user).addOnSuccessListener(v-> {
//            Log.d(TAG, "onsuccess: bio updated for" + userID);
//            Toast.makeText(PostActivity.this, "Bio updated", Toast.LENGTH_SHORT).show();
//        });
//    }
}