package com.telo.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.telo.app.R;
import com.telo.app.passwords.PasswordGenerator;
import com.telo.app.passwords.PasswordStrengthChecker;
import com.telo.app.util.ClipboardHelper;

public class GeneratorFragment extends Fragment {

    private TextView  tvGeneratedPassword;
    private SeekBar   seekBarLength;
    private TextView  tvLength;
    private CheckBox  cbUppercase;
    private CheckBox  cbDigits;
    private CheckBox  cbSymbols;
    private CheckBox  cbExcludeAmbiguous;
    private Button    btnGenerate;
    private Button    btnCopy;
    private TextView  tvStrength;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_generator, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvGeneratedPassword = view.findViewById(R.id.tv_generated);
        seekBarLength       = view.findViewById(R.id.seekbar_length);
        tvLength            = view.findViewById(R.id.tv_length);
        cbUppercase         = view.findViewById(R.id.cb_uppercase);
        cbDigits            = view.findViewById(R.id.cb_digits);
        cbSymbols           = view.findViewById(R.id.cb_symbols);
        cbExcludeAmbiguous  = view.findViewById(R.id.cb_exclude_ambiguous);
        btnGenerate         = view.findViewById(R.id.btn_generate);
        btnCopy             = view.findViewById(R.id.btn_copy_generated);
        tvStrength          = view.findViewById(R.id.tv_gen_strength);

        seekBarLength.setMax(48);
        seekBarLength.setProgress(16);
        tvLength.setText("16");

        seekBarLength.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(
                        SeekBar seekBar, int progress, boolean fromUser) {
                    int length = Math.max(8, progress);
                    tvLength.setText(String.valueOf(length));
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            }
        );

        btnGenerate.setOnClickListener(v -> generatePassword());

        btnCopy.setOnClickListener(v -> {
            String password = tvGeneratedPassword.getText().toString();
            if (!password.isEmpty()) {
                ClipboardHelper.copyPassword(requireContext(), password);
                showSnackbar("Password copied — clears in 30s");
            }
        });

        generatePassword();
    }

    private void generatePassword() {
        int length = Math.max(8, seekBarLength.getProgress());

        String password = new PasswordGenerator()
            .length(length)
            .useUppercase(cbUppercase.isChecked())
            .useDigits(cbDigits.isChecked())
            .useSymbols(cbSymbols.isChecked())
            .excludeAmbiguous(cbExcludeAmbiguous.isChecked())
            .generate();

        tvGeneratedPassword.setText(password);

        PasswordStrengthChecker.Result result =
            PasswordStrengthChecker.check(password);
        tvStrength.setText(result.feedback);
    }

    private void showSnackbar(String msg) {
        if (getView() != null) {
            com.google.android.material.snackbar.Snackbar
                .make(getView(), msg,
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                .show();
        }
    }
}