package com.telo.app.ui;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.telo.app.R;
import com.telo.app.security.AutoLockManager;
import com.telo.app.viewmodels.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {

    private SettingsViewModel viewModel;

    private Switch   switchBiometric;
    private Switch   switchScreenshot;
    private Switch   switchTapReveal;
    private Switch   switchAutoBackup;
    private TextView tvLockTimeout;
    private TextView tvTheme;
    private TextView tvPinStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        viewModel = new ViewModelProvider(this)
            .get(SettingsViewModel.class);

        setupToolbar();
        initViews();
        observeViewModel();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
    }

    private void initViews() {
        switchBiometric  = findViewById(R.id.switch_biometric);
        switchScreenshot = findViewById(R.id.switch_screenshot);
        switchTapReveal  = findViewById(R.id.switch_tap_reveal);
        switchAutoBackup = findViewById(R.id.switch_auto_backup);
        tvLockTimeout    = findViewById(R.id.tv_lock_timeout);
        tvTheme          = findViewById(R.id.tv_theme);
        tvPinStatus      = findViewById(R.id.tv_pin_status);
    }

    private void observeViewModel() {
        viewModel.getBiometricEnabled().observe(this, enabled ->
            switchBiometric.setChecked(enabled)
        );
        viewModel.getScreenshotBlocked().observe(this, blocked ->
            switchScreenshot.setChecked(blocked)
        );
        viewModel.getTapToReveal().observe(this, enabled ->
            switchTapReveal.setChecked(enabled)
        );
        viewModel.getAutoBackup().observe(this, enabled ->
            switchAutoBackup.setChecked(enabled)
        );
        viewModel.getLockTimeout().observe(this, timeout ->
            tvLockTimeout.setText(timeout.name())
        );
        viewModel.getTheme().observe(this, theme ->
            tvTheme.setText(theme)
        );
        viewModel.getSuccess().observe(this, msg -> {
            if (msg != null) showSnackbar(msg);
        });
        viewModel.getError().observe(this, error -> {
            if (error != null) showSnackbar(error);
        });

        tvPinStatus.setText(
            viewModel.isPinEnabled() ? "PIN: Enabled" : "PIN: Disabled"
        );
    }

    private void setupListeners() {
        switchBiometric.setOnCheckedChangeListener((v, checked) ->
            viewModel.setBiometricEnabled(checked)
        );
        switchScreenshot.setOnCheckedChangeListener((v, checked) ->
            viewModel.setScreenshotBlocked(checked)
        );
        switchTapReveal.setOnCheckedChangeListener((v, checked) ->
            viewModel.setTapToReveal(checked)
        );
        switchAutoBackup.setOnCheckedChangeListener((v, checked) ->
            viewModel.setAutoBackup(checked)
        );

        tvLockTimeout.setOnClickListener(v -> showTimeoutPicker());
        tvTheme.setOnClickListener(v -> showThemePicker());
        tvPinStatus.setOnClickListener(v -> showPinDialog());
    }

    private void showTimeoutPicker() {
        String[] options = {"Immediately", "30s", "1 min",
                            "5 min", "15 min", "Never"};
        AutoLockManager.LockTimeout[] timeouts =
            AutoLockManager.LockTimeout.values();

        new android.app.AlertDialog.Builder(this)
            .setTitle("Auto-lock after")
            .setItems(options, (d, which) ->
                viewModel.setLockTimeout(timeouts[which])
            )
            .show();
    }

    private void showThemePicker() {
        String[] themes = {"Light", "Dark", "AMOLED", "System"};
        new android.app.AlertDialog.Builder(this)
            .setTitle("Theme")
            .setItems(themes, (d, which) ->
                viewModel.setTheme(themes[which].toLowerCase())
            )
            .show();
    }

    private void showPinDialog() {
        startActivity(new android.content.Intent(
            this, UnlockActivity.class
        ));
    }

    private void showSnackbar(String msg) {
        com.google.android.material.snackbar.Snackbar
            .make(switchBiometric, msg,
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}