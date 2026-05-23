package com.telo.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.telo.app.R;
import com.telo.app.util.TimeHelper;
import com.telo.app.viewmodels.BackupViewModel;

public class BackupActivity extends AppCompatActivity {

    private BackupViewModel viewModel;

    private Button      btnExport;
    private Button      btnImport;
    private Button      btnExportPlain;
    private TextView    tvLastBackup;
    private ProgressBar progressBar;

    private ActivityResultLauncher<String> exportLauncher;
    private ActivityResultLauncher<String[]> importLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        viewModel = new ViewModelProvider(this)
            .get(BackupViewModel.class);

        setupToolbar();
        initViews();
        setupLaunchers();
        observeViewModel();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Backup & Restore");
        }
    }

    private void initViews() {
        btnExport      = findViewById(R.id.btn_export);
        btnImport      = findViewById(R.id.btn_import);
        btnExportPlain = findViewById(R.id.btn_export_plain);
        tvLastBackup   = findViewById(R.id.tv_last_backup);
        progressBar    = findViewById(R.id.progress_backup);
        progressBar.setVisibility(View.GONE);
    }

    private void setupLaunchers() {
        exportLauncher = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("*/*"),
            uri -> {
                if (uri != null) {
                    showPasswordDialog(uri);
                }
            }
        );

        importLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null) {
                    viewModel.importFromAegis(uri);
                }
            }
        );
    }

    private void observeViewModel() {
        viewModel.getState().observe(this, state -> {
            switch (state) {
                case EXPORTING:
                case IMPORTING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                case FAILED:
                    progressBar.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        });

        viewModel.getMessage().observe(this, msg -> {
            if (msg != null) {
                showSnackbar(msg);
            }
        });

        viewModel.getLastBackup().observe(this, time -> {
            if (time != null && time > 0) {
                tvLastBackup.setText(
                    "Last backup: " + TimeHelper.formatRelative(time)
                );
            } else {
                tvLastBackup.setText("No backup yet");
            }
        });
    }

    private void setupListeners() {
        btnExport.setOnClickListener(v ->
            exportLauncher.launch("telo_backup.telo")
        );

        btnImport.setOnClickListener(v ->
            importLauncher.launch(new String[]{"*/*"})
        );

        btnExportPlain.setOnClickListener(v ->
            exportLauncher.launch("telo_backup.json")
        );
    }

    private void showPasswordDialog(Uri uri) {
        android.widget.EditText input =
            new android.widget.EditText(this);
        input.setInputType(
            android.text.InputType.TYPE_CLASS_TEXT |
            android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        );
        input.setHint("Backup password");

        new android.app.AlertDialog.Builder(this)
            .setTitle("Encrypt Backup")
            .setMessage("Set a password to protect your backup")
            .setView(input)
            .setPositiveButton("Export", (d, w) -> {
                String pass = input.getText().toString();
                if (!pass.isEmpty()) {
                    viewModel.exportEncrypted(uri, pass.toCharArray());
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showSnackbar(String msg) {
        com.google.android.material.snackbar.Snackbar
            .make(btnExport, msg,
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}