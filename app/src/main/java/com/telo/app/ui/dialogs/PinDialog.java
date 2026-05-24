package com.telo.app.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.telo.app.R;
import com.telo.app.util.AnimationHelper;

public class PinDialog extends DialogFragment {

    public interface PinCallback {
        void onPinEntered(String pin);
        void onCancelled();
    }

    private PinCallback   callback;
    private StringBuilder pinInput = new StringBuilder();
    private TextView      tvPinDisplay;
    private static final int PIN_LENGTH = 4;

    public void setCallback(PinCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_pin, null);

        tvPinDisplay = view.findViewById(R.id.tv_pin_display);
        setupNumpad(view);

        return new AlertDialog.Builder(requireContext())
            .setTitle("Enter PIN")
            .setView(view)
            .setNegativeButton("Cancel", (d, w) -> {
                if (callback != null) callback.onCancelled();
            })
            .create();
    }

    private void setupNumpad(View view) {
        int[] btnIds = {
            R.id.btn_pin_0, R.id.btn_pin_1,
            R.id.btn_pin_2, R.id.btn_pin_3,
            R.id.btn_pin_4, R.id.btn_pin_5,
            R.id.btn_pin_6, R.id.btn_pin_7,
            R.id.btn_pin_8, R.id.btn_pin_9
        };

        for (int i = 0; i < btnIds.length; i++) {
            final String digit = String.valueOf(i);
            Button btn = view.findViewById(btnIds[i]);
            if (btn != null) {
                btn.setOnClickListener(v -> {
                    if (pinInput.length() < PIN_LENGTH) {
                        pinInput.append(digit);
                        updateDisplay();
                        if (pinInput.length() == PIN_LENGTH) {
                            if (callback != null) {
                                callback.onPinEntered(
                                    pinInput.toString()
                                );
                            }
                            dismiss();
                        }
                    }
                });
            }
        }

        Button btnDel = view.findViewById(R.id.btn_pin_delete);
        if (btnDel != null) {
            btnDel.setOnClickListener(v -> {
                if (pinInput.length() > 0) {
                    pinInput.deleteCharAt(pinInput.length() - 1);
                    updateDisplay();
                }
            });
        }
    }

    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PIN_LENGTH; i++) {
            sb.append(i < pinInput.length() ? "●" : "○");
            if (i < PIN_LENGTH - 1) sb.append("  ");
        }
        if (tvPinDisplay != null) tvPinDisplay.setText(sb.toString());
    }
}