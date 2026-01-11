package com.example.dailydosepersonalmedicinecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dailydosepersonalmedicinecompanion.activity.LoginActivity;

/**
 * MainActivity - Splash screen
 * Redirects to LoginActivity after initialization
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Redirect to login after 2 seconds
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }, 2000);
    }
}