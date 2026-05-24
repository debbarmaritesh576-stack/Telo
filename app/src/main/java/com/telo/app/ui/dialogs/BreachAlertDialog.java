package com.telo.app.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.telo.app.R;

public class BreachAlertDialog extends DialogFragment {

    public interface BreachCallback {
        void onChangePassword();
        void onDismiss();
    }

    private static final String ARG_TITLE = "title";
    private static final String ARG_COUNT = "count";

    private BreachCallback callback;

    public static BreachAlertDialog newInstance(
            String title, int breachCount) {
        BreachAlertDialog dialog = new BreachAlertDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_COUNT,    breachCount);
        dialog.setArguments(args);
        return dialog;
    }

    public void setCallback(BreachCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState) {

        String title = getArguments() != null
            ? getArguments().getString(ARG_TITLE) : "";
        int count = getArguments() != null
            ? getArguments().getInt(ARG_COUNT) : 0;

        return new AlertDialog.Builder(requireContext())
            .setTitle("⚠️ Password Breached!")
            .setMessage(
                "Your password for \"" + title + "\" was found " +
                "in " + count + " data breach(es).\n\n" +
                "Change it immediately to protect your account."
            )
            .setIcon(R.drawable.ic_breach)
            .setPositiveButton("Change Password", (d, w) -> {
                if (callback != null) callback.onChangePassword();
            })
            .setNegativeButton("Later", (d, w) -> {
                if (callback != null) callback.onDismiss();
            })
            .setCancelable(false)
            .create();
    }
}