package com.telo.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.telo.app.R;
import com.telo.app.viewmodels.UnlockViewModel;

public class UnlockFragment extends Fragment {

    protected UnlockViewModel viewModel;
    protected TextView        tvError;
    protected TextView        tvAttempts;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_unlock, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel  = new ViewModelProvider(requireActivity())
            .get(UnlockViewModel.class);
        tvError    = view.findViewById(R.id.tv_unlock_error);
        tvAttempts = view.findViewById(R.id.tv_unlock_attempts);

        observeViewModel();
    }

    protected void observeViewModel() {
        viewModel.getError().observe(
                getViewLifecycleOwner(), error -> {
            if (error != null) {
                tvError.setText(error);
                tvError.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getAttempts().observe(
                getViewLifecycleOwner(), attempts -> {
            if (attempts != null && attempts > 0) {
                tvAttempts.setText(
                    attempts + " failed attempt(s)"
                );
                tvAttempts.setVisibility(View.VISIBLE);
            }
        });
    }
}