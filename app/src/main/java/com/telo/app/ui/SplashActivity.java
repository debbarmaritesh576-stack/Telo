package com.telo.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.telo.app.R;
import com.telo.app.util.PreferenceHelper;
import com.telo.app.vault.VaultManager;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(
            this::navigate, SPLASH_DELAY
        );
    }

    private void navigate() {
        Intent intent;

        if (PreferenceHelper.isFirstLaunch()) {
            // First time — show intro
            intent = new Intent(this, IntroActivity.class);
        } else if (!VaultManager.getInstance(this).vaultExists()) {
            // Vault not created — setup
            intent = new Intent(this, IntroActivity.class);
        } else {
            // Vault exists — unlock
            intent = new Intent(this, UnlockActivity.class);
        }

        startActivity(intent);
        finish();
    }
}