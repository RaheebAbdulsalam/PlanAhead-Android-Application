package uk.ac.aston.cs3mdd.planaheadmobileapplication.splash_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.MainActivity;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the app bar on splash screen
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);
    }
}