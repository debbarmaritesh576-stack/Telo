package com.telo.app.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.telo.app.R;
import com.telo.app.passwords.PasswordGenerator;
import com.telo.app.passwords.PasswordStrengthChecker;

public class PasswordGeneratorDialog extends DialogFragment {

    public interface GeneratorCallback {
        void onPasswordGenerated(String password);
    }

    private GeneratorCallback callback;
    private TextView          tvGenerated;
    private String            currentPassword = "";

    public void setCallback(GeneratorCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_generator, null);

        tvGenerated        = view.findViewById(R.id.tv_gen_password);
        SeekBar seekBar    = view.findViewById(R.id.seekbar_gen_length);
        TextView tvLength  = view.findViewById(R.id.tv_gen_length);
        TextView tvStrength = view.findViewById(R.id.tv_gen_strength);
        CheckBox cbUpper   = view.findViewById(R.id.cb_gen_upper);
        CheckBox cbDigits  = view.findViewById(R.id.cb_gen_digits);
        CheckBox cbSymbols = view.findViewById(R.id.cb_gen_symbols);
        Button   btnGen    = view.findViewById(R.id.btn_gen_generate);

        seekBar.setMax(48);
        seekBar.setProgress(16);
        tvLength.setText("16");

        seekBar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(
                        SeekBar sb, int progress, boolean fromUser) {
                    tvLength.setText(
                        String.valueOf(Math.max(8, progress))
                    );
                }
                @Override
                public void onStartTrackingTouch(SeekBar sb) {}
                @Override
                public void onStopTrackingTouch(SeekBar sb) {}
            }
        );

        btnGen.setOnClickListener(v -> {
            int length = Math.max(8, seekBar.getProgress());
            currentPassword = new PasswordGenerator()
                .length(length)
                .useUppercase(cbUpper.isChecked())
                .useDigits(cbDigits.isChecked())
                .useSymbols(cbSymbols.isChecked())
                .generate();
            tvGenerated.setText(currentPassword);

            PasswordStrengthChecker.Result result =
                PasswordStrengthChecker.check(currentPassword);
            tvStrength.setText(result.feedback);
        });

        // Generate on open
        btnGen.performClick();

        return new AlertDialog.Builder(requireContext())
            .setTitle("Password Generator")
            .setView(view)
            .setPositiveButton("Use This Password", (d, w) -> {
                if (callback != null && !currentPassword.isEmpty()) {
                    callback.onPasswordGenerated(currentPassword);
                }
            })
            .setNegativeButton("Cancel", null)
            .create();
    }
}