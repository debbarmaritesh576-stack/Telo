package com.telo.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.telo.app.R;
import com.telo.app.util.AnimationHelper;

public class PinUnlockFragment extends UnlockFragment {

    private TextView tvPinDisplay;
    private StringBuilder pinInput = new StringBuilder();
    private static final int PIN_LENGTH = 4;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_pin_unlock, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvPinDisplay = view.findViewById(R.id.tv_pin_display);

        setupNumpad(view);

        viewModel.getState().observe(
                getViewLifecycleOwner(), state -> {
            if (state == com.telo.app.viewmodels
                    .UnlockViewModel.UnlockState.FAILED) {
                AnimationHelper.shake(tvPinDisplay);
                pinInput.setLength(0);
                updateDisplay();
            }
        });
    }

    private void setupNumpad(View view) {
        int[] numpadIds = {
            R.id.btn_pin_0, R.id.btn_pin_1, R.id.btn_pin_2,
            R.id.btn_pin_3, R.id.btn_pin_4, R.id.btn_pin_5,
            R.id.btn_pin_6, R.id.btn_pin_7, R.id.btn_pin_8,
            R.id.btn_pin_9
        };

        for (int i = 0; i < numpadIds.length; i++) {
            final String digit = String.valueOf(i);
            Button btn = view.findViewById(numpadIds[i]);
            if (btn != null) {
                btn.setOnClickListener(v -> {
                    if (pinInput.length() < PIN_LENGTH) {
                        pinInput.append(digit);
                        updateDisplay();
                        if (pinInput.length() == PIN_LENGTH) {
                            viewModel.unlockWithPin(
                                pinInput.toString()
                            );
                        }
                    }
                });
            }
        }

        Button btnDelete = view.findViewById(R.id.btn_pin_delete);
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                if (pinInput.length() > 0) {
                    pinInput.deleteCharAt(pinInput.length() - 1);
                    updateDisplay();
                }
            });
        }
    }

    private void updateDisplay() {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < PIN_LENGTH; i++) {
            display.append(i < pinInput.length() ? "●" : "○");
            if (i < PIN_LENGTH - 1) display.append("  ");
        }
        tvPinDisplay.setText(display.toString());
    }
}