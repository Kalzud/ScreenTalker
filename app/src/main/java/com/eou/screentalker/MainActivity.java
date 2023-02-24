package com.eou.screentalker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.google.firebase.FirebaseApp;

/**
 * Author: Emmanuel O. Uduma
 *
 * This class and would host the main activities all the
 * other fragment would be hosted here
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}