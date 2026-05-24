package com.telo.app.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.telo.app.R;

public class BackupPasswordDialog extends DialogFragment {

    public interface BackupPasswordCallback {
        void onPasswordSet(char[] password);
        void onCancelled();
    }

    private BackupPasswordCallback callback;

    public void setCallback(BackupPasswordCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState) {

        View     view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_backup_password, null);
        EditText et   = view.findViewById(
            R.id.et_backup_password
        );
        EditText etConfirm = view.findViewById(
            R.id.et_backup_confirm
        );

        return new AlertDialog.Builder(requireContext())
            .setTitle("Backup Password")
            .setMessage(
                "Set a strong password to protect your backup"
            )
            .setView(view)
            .setPositiveButton("Set Password", (d, w) -> {
                String pass    = et.getText().toString();
                String confirm = etConfirm.getText().toString();

                if (!pass.equals(confirm)) {
                    et.setError("Passwords don't match");
                    return;
                }
                if (pass.length() < 8) {
                    et.setError("Minimum 8 characters");
                    return;
                }
                if (callback != null) {
                    callback.onPasswordSet(pass.toCharArray());
                }
            })
            .setNegativeButton("Cancel", (d, w) -> {
                if (callback != null) callback.onCancelled();
            })
            .create();
    }
}