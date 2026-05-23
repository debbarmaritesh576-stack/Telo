package com.telo.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.telo.app.R;
import com.telo.app.crypto.BiometricCipher;
import com.telo.app.util.AnimationHelper;

public class BiometricUnlockFragment extends UnlockFragment {

    private ImageView ivFingerprint;
    private TextView  tvBiometricStatus;
    private Button    btnUsePinInstead;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_biometric_unlock, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivFingerprint    = view.findViewById(R.id.iv_fingerprint);
        tvBiometricStatus = view.findViewById(R.id.tv_biometric_status);
        btnUsePinInstead = view.findViewById(R.id.btn_use_pin);

        btnUsePinInstead.setOnClickListener(v ->
            requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                    new PinUnlockFragment())
                .commit()
        );

        showBiometricPrompt();
    }

    private void showBiometricPrompt() {
        AnimationHelper.pulse(ivFingerprint);
        tvBiometricStatus.setText("Touch fingerprint sensor");

        BiometricCipher.authenticateForEncrypt(
            requireActivity(),
            "Unlock Telo",
            "Use biometric to unlock",
            new BiometricCipher.BiometricCallback() {
                @Override
                public void onSuccess(javax.crypto.Cipher cipher) {
                    viewModel.onBiometricSuccess();
                }
                @Override
                public void onError(String error) {
                    tvBiometricStatus.setText(error);
                }
                @Override
                public void onFailed() {
                    tvBiometricStatus.setText("Not recognized. Try again");
                    AnimationHelper.shake(ivFingerprint);
                    viewModel.onBiometricFailed();
                }
            }
        );
    }
}