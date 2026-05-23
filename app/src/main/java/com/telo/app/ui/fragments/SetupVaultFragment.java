package com.telo.app.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.telo.app.R;
import com.telo.app.passwords.PasswordStrengthChecker;
import com.telo.app.ui.HomeActivity;
import com.telo.app.util.PreferenceHelper;
import com.telo.app.vault.VaultManager;

public class SetupVaultFragment extends Fragment {

    private EditText    etPassword;
    private EditText    etConfirmPassword;
    private Button      btnCreate;
    private ProgressBar progressStrength;
    private TextView    tvStrengthLabel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_setup_vault, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPassword        = view.findViewById(R.id.et_setup_password);
        etConfirmPassword = view.findViewById(R.id.et_setup_confirm);
        btnCreate         = view.findViewById(R.id.btn_setup_create);
        progressStrength  = view.findViewById(R.id.progress_setup_strength);
        tvStrengthLabel   = view.findViewById(R.id.tv_setup_strength);

        etPassword.addTextChangedListener(
            new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s,
                        int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s,
                        int start, int before, int count) {}
                @Override
                public void afterTextChanged(android.text.Editable s) {
                    checkStrength(s.toString());
                }
            }
        );

        btnCreate.setOnClickListener(v -> createVault());
    }

    private void checkStrength(String password) {
        PasswordStrengthChecker.Result result =
            PasswordStrengthChecker.check(password);
        progressStrength.setProgress(result.score);
        tvStrengthLabel.setText(result.feedback);
    }

    private void createVault() {
        String password = etPassword.getText().toString();
        String confirm  = etConfirmPassword.getText().toString();

        if (password.isEmpty()) {
            etPassword.setError("Password required");
            return;
        }
        if (!password.equals(confirm)) {
            etConfirmPassword.setError("Passwords don't match");
            return;
        }
        if (password.length() < 8) {
            etPassword.setError("Minimum 8 characters");
            return;
        }

        com.telo.app.db.AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                VaultManager.getInstance(requireContext())
                    .createVault(password.toCharArray());
                PreferenceHelper.setVaultSetup(true);

                requireActivity().runOnUiThread(() -> {
                    startActivity(new Intent(
                        requireContext(), HomeActivity.class
                    ));
                    requireActivity().finish();
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                    etPassword.setError(
                        "Setup failed: " + e.getMessage()
                    )
                );
            }
        });
    }
}