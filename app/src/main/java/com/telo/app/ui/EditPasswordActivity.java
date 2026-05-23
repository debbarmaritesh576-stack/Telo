package com.telo.app.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputLayout;
import com.telo.app.R;
import com.telo.app.passwords.PasswordStrengthChecker;
import com.telo.app.viewmodels.EditPasswordViewModel;

public class EditPasswordActivity extends AppCompatActivity {

    private EditPasswordViewModel viewModel;

    private EditText    etTitle;
    private EditText    etUsername;
    private EditText    etEmail;
    private EditText    etPassword;
    private EditText    etUrl;
    private EditText    etNotes;
    private ProgressBar progressStrength;
    private TextView    tvStrengthLabel;
    private TextView    tvStrengthFeedback;
    private Button      btnSave;
    private Button      btnGenerate;
    private Button      btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        viewModel = new ViewModelProvider(this)
            .get(EditPasswordViewModel.class);

        setupToolbar();
        initViews();
        setupListeners();
        observeViewModel();

        String entryId = getIntent().getStringExtra("entry_id");
        if (entryId != null) {
            viewModel.loadEntry(entryId);
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        etTitle           = findViewById(R.id.et_pass_title);
        etUsername        = findViewById(R.id.et_pass_username);
        etEmail           = findViewById(R.id.et_pass_email);
        etPassword        = findViewById(R.id.et_pass_password);
        etUrl             = findViewById(R.id.et_pass_url);
        etNotes           = findViewById(R.id.et_pass_notes);
        progressStrength  = findViewById(R.id.progress_strength);
        tvStrengthLabel   = findViewById(R.id.tv_strength_label);
        tvStrengthFeedback = findViewById(R.id.tv_strength_feedback);
        btnSave           = findViewById(R.id.btn_pass_save);
        btnGenerate       = findViewById(R.id.btn_pass_generate);
        btnDelete         = findViewById(R.id.btn_pass_delete);
        btnDelete.setVisibility(View.GONE);
    }

    private void setupListeners() {
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                    int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                    int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setPassword(s.toString());
            }
        });

        btnSave.setOnClickListener(v -> {
            viewModel.setTitle(etTitle.getText().toString());
            viewModel.setUsername(etUsername.getText().toString());
            viewModel.setEmail(etEmail.getText().toString());
            viewModel.setUrl(etUrl.getText().toString());
            viewModel.setNotes(etNotes.getText().toString());
            viewModel.saveEntry();
            finish();
        });

        btnGenerate.setOnClickListener(v -> {
            String generated = viewModel.generatePassword(
                16, true, true, true
            );
            etPassword.setText(generated);
        });

        btnDelete.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                .setTitle("Delete password?")
                .setMessage("This cannot be undone")
                .setPositiveButton("Delete", (d, w) -> {
                    viewModel.deleteEntry();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
    }

    private void observeViewModel() {
        viewModel.getStrength().observe(this, result -> {
            if (result == null) return;
            progressStrength.setProgress(result.score);
            tvStrengthLabel.setText(result.strength.name());
            tvStrengthFeedback.setText(result.feedback);

            int color = getStrengthColor(result.strength);
            progressStrength.setProgressTintList(
                android.content.res.ColorStateList.valueOf(color)
            );
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                etTitle.setError(error);
            }
        });
    }

    private int getStrengthColor(PasswordStrengthChecker.Strength s) {
        switch (s) {
            case VERY_WEAK:   return getColor(R.color.red);
            case WEAK:        return getColor(R.color.orange);
            case FAIR:        return getColor(R.color.yellow);
            case STRONG:      return getColor(R.color.green);
            case VERY_STRONG: return getColor(R.color.blue);
            default:          return getColor(R.color.gray);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}