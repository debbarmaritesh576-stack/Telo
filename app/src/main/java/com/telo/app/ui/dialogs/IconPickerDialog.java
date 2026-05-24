package com.telo.app.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.adapters.IconPackAdapter;

public class IconPickerDialog extends DialogFragment {

    public interface IconCallback {
        void onIconSelected(String iconRes);
    }

    private IconCallback callback;
    private String       currentIconRes;

    public static IconPickerDialog newInstance(String currentIcon) {
        IconPickerDialog dialog = new IconPickerDialog();
        Bundle args = new Bundle();
        args.putString("current_icon", currentIcon);
        dialog.setArguments(args);
        return dialog;
    }

    public void setCallback(IconCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState) {

        currentIconRes = getArguments() != null
            ? getArguments().getString("current_icon") : null;

        View view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_icon_picker, null);

        RecyclerView rv = view.findViewById(
            R.id.rv_icon_picker
        );
        rv.setLayoutManager(
            new GridLayoutManager(requireContext(), 4)
        );

        IconPackAdapter adapter = new IconPackAdapter(
            requireContext(),
            iconRes -> {
                if (callback != null) {
                    callback.onIconSelected(iconRes);
                }
                dismiss();
            }
        );

        if (currentIconRes != null) {
            adapter.setSelected(currentIconRes);
        }

        rv.setAdapter(adapter);

        return new AlertDialog.Builder(requireContext())
            .setTitle("Choose Icon")
            .setView(view)
            .setNegativeButton("Cancel", null)
            .create();
    }
}