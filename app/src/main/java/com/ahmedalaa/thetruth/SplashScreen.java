package com.ahmedalaa.thetruth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.ahmedalaa.thetruth.Util.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        sessionManager = new SessionManager(getPreferences(MODE_PRIVATE));

        Runnable a = () -> {
            Intent i = new Intent(getApplicationContext(), SliderActivity.class);
            startActivity(i);
            finish();

        };
        if (sessionManager.isFirstTime()) {
            sessionManager.visited();

            new Handler().postDelayed(a, 2000);

        } else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }
}