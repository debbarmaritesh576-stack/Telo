package com.telo.app.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.telo.app.R;
import com.telo.app.crypto.EncryptedPrefs;
import com.telo.app.crypto.MasterKeyManager;
import com.telo.app.vault.VaultManager;

public class PanicWipeDialog extends DialogFragment {

    public interface PanicCallback {
        void onWipeConfirmed();
        void onCancelled();
    }

    private PanicCallback callback;

    public void setCallback(PanicCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState) {

        return new AlertDialog.Builder(requireContext())
            .setTitle("⚠️ Emergency Wipe")
            .setMessage(
                "This will permanently delete ALL your data:\n\n" +
                "• All 2FA accounts\n" +
                "• All passwords\n" +
                "• All notes & cards\n" +
                "• All settings\n\n" +
                "This CANNOT be undone. Are you absolutely sure?"
            )
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("WIPE EVERYTHING", (d, w) -> {
                performPanicWipe();
            })
            .setNegativeButton("Cancel", (d, w) -> {
                if (callback != null) callback.onCancelled();
            })
            .setCancelable(false)
            .create();
    }

    private void performPanicWipe() {
        com.telo.app.db.AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                // Wipe vault
                VaultManager.getInstance(requireContext())
                    .panicWipe();

                // Wipe keystore key
                MasterKeyManager.deleteMasterKey();

                // Wipe encrypted prefs
                new EncryptedPrefs(requireContext()).clearAll();

                // Wipe database
                com.telo.app.db.AppDatabase
                    .getInstance(requireContext())
                    .clearAllTables();

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(
                        requireContext(),
                        "All data wiped",
                        Toast.LENGTH_LONG
                    ).show();

                    if (callback != null) {
                        callback.onWipeConfirmed();
                    }

                    // Restart app
                    requireActivity().finishAffinity();
                });

            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                    Toast.makeText(
                        requireContext(),
                        "Wipe failed: " + e.getMessage(),
                        Toast.LENGTH_SHORT
                    ).show()
                );
            }
        });
    }
}