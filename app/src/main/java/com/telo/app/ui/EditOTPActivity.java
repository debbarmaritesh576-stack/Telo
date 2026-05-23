package com.telo.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.telo.app.R;
import com.telo.app.otp.OTPAlgorithm;
import com.telo.app.otp.OTPType;
import com.telo.app.viewmodels.EditOTPViewModel;

public class EditOTPActivity extends AppCompatActivity {

    private EditOTPViewModel viewModel;

    private EditText etName;
    private EditText etIssuer;
    private EditText etSecret;
    private EditText etPeriod;
    private EditText etDigits;
    private Spinner  spinnerType;
    private Spinner  spinnerAlgorithm;
    private TextView tvPreviewCode;
    private Button   btnSave;
    private Button   btnScanQR;

    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_otp);

        viewModel = new ViewModelProvider(this)
            .get(EditOTPViewModel.class);

        setupToolbar();
        initViews();
        setupSpinners();
        setupButtons();
        observeViewModel();

        // Edit mode
        String entryId = getIntent().getStringExtra("entry_id");
        if (entryId != null) {
            isEditMode = true;
            viewModel.loadEntry(entryId);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(
                isEditMode ? "Edit Account" : "Add Account"
            );
        }
    }

    private void initViews() {
        etName          = findViewById(R.id.et_otp_name);
        etIssuer        = findViewById(R.id.et_otp_issuer);
        etSecret        = findViewById(R.id.et_otp_secret);
        etPeriod        = findViewById(R.id.et_otp_period);
        etDigits        = findViewById(R.id.et_otp_digits);
        spinnerType     = findViewById(R.id.spinner_otp_type);
        spinnerAlgorithm = findViewById(R.id.spinner_otp_algorithm);
        tvPreviewCode   = findViewById(R.id.tv_otp_preview);
        btnSave         = findViewById(R.id.btn_otp_save);
        btnScanQR       = findViewById(R.id.btn_scan_qr);
    }

    private void setupSpinners() {
        // Type spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item,
            new String[]{"TOTP", "HOTP", "Steam"}
        );
        typeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );
        spinnerType.setAdapter(typeAdapter);

        // Algorithm spinner
        ArrayAdapter<String> algoAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item,
            new String[]{"SHA1", "SHA256", "SHA512"}
        );
        algoAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );
        spinnerAlgorithm.setAdapter(algoAdapter);
    }

    private void setupButtons() {
        btnSave.setOnClickListener(v -> saveEntry());

        btnScanQR.setOnClickListener(v -> {
            startActivity(new Intent(this, ScanQRActivity.class));
        });

        // Preview code on secret change
        etSecret.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                viewModel.setSecret(etSecret.getText().toString());
                viewModel.generatePreviewCode();
            }
        });
    }

    private void saveEntry() {
        viewModel.setName(etName.getText().toString());
        viewModel.setIssuer(etIssuer.getText().toString());
        viewModel.setSecret(etSecret.getText().toString());

        String periodStr = etPeriod.getText().toString();
        if (!periodStr.isEmpty()) {
            viewModel.setPeriod(Long.parseLong(periodStr));
        }

        String digitsStr = etDigits.getText().toString();
        if (!digitsStr.isEmpty()) {
            viewModel.setDigits(Integer.parseInt(digitsStr));
        }

        viewModel.saveEntry();
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, error -> {
            if (error != null) {
                etName.setError(error);
            }
        });

        viewModel.getCode().observe(this, code -> {
            tvPreviewCode.setText(code);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}