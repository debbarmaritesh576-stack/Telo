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
import com.telo.app.adapters.OTPAdapter;
import com.telo.app.adapters.OTPTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.telo.app.db.OTPEntryEntity;
import com.telo.app.viewmodels.HomeViewModel;
import com.telo.app.viewmodels.OTPViewModel;

public class OTPListFragment extends Fragment {

    private OTPViewModel viewModel;
    private OTPAdapter   adapter;
    private RecyclerView recyclerView;
    private TextView     tvEmpty;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_otp_list, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel    = new ViewModelProvider(requireActivity())
            .get(OTPViewModel.class);
        recyclerView = view.findViewById(R.id.rv_otp_list);
        tvEmpty      = view.findViewById(R.id.tv_otp_empty);

        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        adapter = new OTPAdapter(
            requireContext(),
            new OTPAdapter.OnItemClickListener() {
                @Override
                public void onCopy(OTPEntryEntity entry, String code) {
                    showSnackbar("Copied — clears in 30s");
                }
                @Override
                public void onFavorite(OTPEntryEntity entry) {
                    viewModel.toggleFavorite(
                        entry.id, !entry.isFavorite
                    );
                }
                @Override
                public void onEdit(OTPEntryEntity entry) {
                    // Navigate to edit
                }
                @Override
                public void onDelete(OTPEntryEntity entry) {
                    showDeleteConfirm(entry);
                }
                @Override
                public void onLongPress(OTPEntryEntity entry) {
                    showOptions(entry);
                }
            }
        );

        recyclerView.setLayoutManager(
            new LinearLayoutManager(requireContext())
        );
        recyclerView.setAdapter(adapter);

        // Swipe to delete
        OTPTouchHelper touchHelper = new OTPTouchHelper(
            new OTPTouchHelper.TouchHelperListener() {
                @Override
                public void onSwipeLeft(int position) {
                    OTPEntryEntity entry = adapter.getCurrentList()
                        .get(position);
                    showDeleteConfirm(entry);
                }
                @Override
                public void onSwipeRight(int position) {
                    OTPEntryEntity entry = adapter.getCurrentList()
                        .get(position);
                    viewModel.toggleFavorite(
                        entry.id, !entry.isFavorite
                    );
                }
                @Override
                public void onMoved(int from, int to) {}
            }
        );
        new ItemTouchHelper(touchHelper).attachToRecyclerView(
            recyclerView
        );
    }

    private void observeViewModel() {
        viewModel.getAllEntries().observe(
                getViewLifecycleOwner(), entries -> {
            adapter.submitList(entries);
            tvEmpty.setVisibility(
                entries == null || entries.isEmpty()
                    ? View.VISIBLE : View.GONE
            );
        });
    }

    private void showDeleteConfirm(OTPEntryEntity entry) {
        new android.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete " + entry.name + "?")
            .setMessage("This cannot be undone")
            .setPositiveButton("Delete", (d, w) ->
                viewModel.deleteEntry(entry.id)
            )
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showOptions(OTPEntryEntity entry) {
        // Show bottom sheet
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