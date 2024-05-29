package com.eou.screentalker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.eou.screentalker.R;

public class PickActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_action);

        // Handle the pick action (e.g., initiate a call)
        // You may want to add your custom logic here
        // For this example, we'll just show a toast message
        showToast("Call Picked!");
        finish(); // Finish the activity
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}