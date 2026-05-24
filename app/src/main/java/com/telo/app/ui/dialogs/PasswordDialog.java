package com.telo.app.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.telo.app.R;

public class PasswordDialog extends DialogFragment {

    public interface PasswordCallback {
        void onPasswordEntered(char[] password);
        void onCancelled();
    }

    private static final String ARG_TITLE   = "title";
    private static final String ARG_MESSAGE = "message";

    private PasswordCallback callback;

    public static PasswordDialog newInstance(
            String title, String message) {
        PasswordDialog dialog = new PasswordDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE,   title);
        args.putString(ARG_MESSAGE, message);
        dialog.setArguments(args);
        return dialog;
    }

    public void setCallback(PasswordCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_password, null);

        EditText etPassword = view.findViewById(R.id.et_dialog_password);
        EditText etConfirm  = view.findViewById(R.id.et_dialog_confirm);

        String title   = getArguments() != null
            ? getArguments().getString(ARG_TITLE)   : "Password";
        String message = getArguments() != null
            ? getArguments().getString(ARG_MESSAGE) : "";

        return new AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setView(view)
            .setPositiveButton("Confirm", (d, w) -> {
                if (callback != null) {
                    callback.onPasswordEntered(
                        etPassword.getText().toString().toCharArray()
                    );
                }
            })
            .setNegativeButton("Cancel", (d, w) -> {
                if (callback != null) callback.onCancelled();
            })
            .create();
    }
}