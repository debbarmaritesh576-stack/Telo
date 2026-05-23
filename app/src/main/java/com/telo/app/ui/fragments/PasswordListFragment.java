package com.telo.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.adapters.PasswordAdapter;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.util.ClipboardHelper;
import com.telo.app.viewmodels.PasswordViewModel;

public class PasswordListFragment extends Fragment {

    private PasswordViewModel viewModel;
    private PasswordAdapter   adapter;
    private RecyclerView      recyclerView;
    private TextView          tvEmpty;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_password_list, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel    = new ViewModelProvider(requireActivity())
            .get(PasswordViewModel.class);
        recyclerView = view.findViewById(R.id.rv_password_list);
        tvEmpty      = view.findViewById(R.id.tv_password_empty);

        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        adapter = new PasswordAdapter(
            requireContext(),
            new PasswordAdapter.OnItemClickListener() {
                @Override
                public void onClick(PasswordEntry entry) {
                    // Navigate to edit
                }
                @Override
                public void onCopyPassword(PasswordEntry entry) {
                    ClipboardHelper.copyPassword(
                        requireContext(), entry.getPassword()
                    );
                    showSnackbar("Password copied — clears in 30s");
                }
                @Override
                public void onCopyUsername(PasswordEntry entry) {
                    ClipboardHelper.copyUsername(
                        requireContext(), entry.getUsername()
                    );
                    showSnackbar("Username copied");
                }
                @Override
                public void onFavorite(PasswordEntry entry) {
                    viewModel.toggleFavorite(entry);
                }
                @Override
                public void onDelete(PasswordEntry entry) {
                    showDeleteConfirm(entry);
                }
                @Override
                public void onLongPress(PasswordEntry entry) {
                    showOptions(entry);
                }
            }
        );

        recyclerView.setLayoutManager(
            new LinearLayoutManager(requireContext())
        );
        recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getAllPasswords().observe(
                getViewLifecycleOwner(), entries -> {
            adapter.submitList(entries);
            tvEmpty.setVisibility(
                entries == null || entries.isEmpty()
                    ? View.VISIBLE : View.GONE
            );
        });

        viewModel.getSelectedCategory().observe(
                getViewLifecycleOwner(), categoryId -> {
            if (categoryId != null && !categoryId.equals("all")) {
                viewModel.getByCategory(categoryId).observe(
                    getViewLifecycleOwner(),
                    entries -> adapter.submitList(entries)
                );
            }
        });
    }

    private void showDeleteConfirm(PasswordEntry entry) {
        new android.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete " + entry.getTitle() + "?")
            .setMessage("This cannot be undone")
            .setPositiveButton("Delete", (d, w) ->
                viewModel.deletePassword(entry.getId())
            )
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showOptions(PasswordEntry entry) {
        // Show bottom sheet options
    }

    private void showSnackbar(String msg) {
        if (getView() != null) {
            com.google.android.material.snackbar.Snackbar
                .make(getView(), msg,
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                .show();
        }
    }
}