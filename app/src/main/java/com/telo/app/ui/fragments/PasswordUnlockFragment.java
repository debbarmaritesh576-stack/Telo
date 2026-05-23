package com.telo.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.telo.app.R;
import com.telo.app.util.AnimationHelper;

public class PasswordUnlockFragment extends UnlockFragment {

    private EditText etPassword;
    private Button   btnUnlock;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_password_unlock, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPassword = view.findViewById(R.id.et_unlock_password);
        btnUnlock  = view.findViewById(R.id.btn_unlock_password);

        btnUnlock.setOnClickListener(v -> {
            String password = etPassword.getText().toString();
            if (password.isEmpty()) {
                etPassword.setError("Enter password");
                return;
            }
            viewModel.unlockWithPassword(password.toCharArray());
        });

        viewModel.getState().observe(
                getViewLifecycleOwner(), state -> {
            if (state == com.telo.app.viewmodels
                    .UnlockViewModel.UnlockState.FAILED) {
                AnimationHelper.shake(etPassword);
                etPassword.setText("");
            }
        });
    }
}