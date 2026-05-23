package com.telo.app.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.telo.app.R;
import com.telo.app.crypto.BiometricCipher;
import com.telo.app.util.PreferenceHelper;
import com.telo.app.util.ScreenshotBlocker;
import com.telo.app.vault.VaultManager;
import com.telo.app.viewmodels.UnlockViewModel;

public class UnlockActivity extends AppCompatActivity {

    private UnlockViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        ScreenshotBlocker.apply(
            this, PreferenceHelper.isScreenshotBlocked()
        );

        viewModel = new ViewModelProvider(this)
            .get(UnlockViewModel.class);

        // If vault not setup yet
        if (!VaultManager.getInstance(this).vaultExists()) {
            showSetupFragment();
            return;
        }

        observeViewModel();

        // Auto biometric if enabled
        if (PreferenceHelper.isBiometricEnabled() &&
            BiometricCipher.isBiometricAvailable(this)) {
            showBiometricUnlock();
        } else if (viewModel.isPinEnabled()) {
            showPinUnlock();
        } else {
            showPasswordUnlock();
        }
    }

    private void observeViewModel() {
        viewModel.getState().observe(this, state -> {
            switch (state) {
                case SUCCESS:
                    goToHome();
                    break;
                case LOCKED_OUT:
                    showLockedOut();
                    break;
                default:
                    break;
            }
        });
    }

    private void showBiometricUnlock() {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container,
                new BiometricUnlockFragment())
            .commit();
    }

    private void showPinUnlock() {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container,
                new PinUnlockFragment())
            .commit();
    }

    private void showPasswordUnlock() {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container,
                new PasswordUnlockFragment())
            .commit();
    }

    private void showSetupFragment() {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container,
                new SetupVaultFragment())
            .commit();
    }

    private void showLockedOut() {
        // Show lockout UI
    }

    private void goToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        // Prevent going back from unlock screen
        finishAffinity();
    }
}