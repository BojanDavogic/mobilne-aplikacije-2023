package com.example.slagalica.aktivnosti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slagalica.MainActivity;
import com.example.slagalica.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toast.makeText(this, "Aplikacija se pokreće", Toast.LENGTH_SHORT).show();
        int SPLASH_TIME_OUT = 3000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, Login.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
