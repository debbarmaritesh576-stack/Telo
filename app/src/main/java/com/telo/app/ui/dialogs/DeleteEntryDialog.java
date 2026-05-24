package com.telo.app.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.telo.app.R;

public class DeleteEntryDialog extends DialogFragment {

    public interface DeleteCallback {
        void onDeleteConfirmed(String entryId);
        void onCancelled();
    }

    private static final String ARG_ID   = "entry_id";
    private static final String ARG_NAME = "entry_name";

    private DeleteCallback callback;

    public static DeleteEntryDialog newInstance(
            String entryId, String entryName) {
        DeleteEntryDialog dialog = new DeleteEntryDialog();
        Bundle args = new Bundle();
        args.putString(ARG_ID,   entryId);
        args.putString(ARG_NAME, entryName);
        dialog.setArguments(args);
        return dialog;
    }

    public void setCallback(DeleteCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState) {

        String id   = getArguments() != null
            ? getArguments().getString(ARG_ID)   : "";
        String name = getArguments() != null
            ? getArguments().getString(ARG_NAME) : "this entry";

        return new AlertDialog.Builder(requireContext())
            .setTitle("Delete " + name + "?")
            .setMessage("This action cannot be undone.")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Delete", (d, w) -> {
                if (callback != null) {
                    callback.onDeleteConfirmed(id);
                }
            })
            .setNegativeButton("Cancel", (d, w) -> {
                if (callback != null) callback.onCancelled();
            })
            .create();
    }
}