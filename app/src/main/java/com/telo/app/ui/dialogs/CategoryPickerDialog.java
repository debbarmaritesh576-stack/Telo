package com.telo.app.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.adapters.CategoryChipAdapter;
import com.telo.app.db.AppDatabase;
import com.telo.app.db.CategoryEntity;
import java.util.List;

public class CategoryPickerDialog extends DialogFragment {

    public interface CategoryCallback {
        void onCategorySelected(CategoryEntity category);
    }

    private CategoryCallback callback;

    public void setCallback(CategoryCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_category_picker, null);

        RecyclerView rv = view.findViewById(
            R.id.rv_category_picker
        );
        rv.setLayoutManager(
            new LinearLayoutManager(requireContext())
        );

        CategoryChipAdapter adapter = new CategoryChipAdapter(
            requireContext(),
            category -> {
                if (callback != null) {
                    callback.onCategorySelected(category);
                }
                dismiss();
            }
        );
        rv.setAdapter(adapter);

        AppDatabase.getInstance(requireContext())
            .categoryDao()
            .getAll()
            .observe(this, adapter::setCategories);

        return new AlertDialog.Builder(requireContext())
            .setTitle("Select Category")
            .setView(view)
            .setNegativeButton("Cancel", null)
            .create();
    }
}