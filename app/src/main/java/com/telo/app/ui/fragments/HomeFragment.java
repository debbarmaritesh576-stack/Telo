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
import com.telo.app.R;
import com.telo.app.viewmodels.HomeViewModel;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private TextView      tvOTPCount;
    private TextView      tvPasswordCount;
    private TextView      tvGreeting;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_home, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity())
            .get(HomeViewModel.class);

        tvOTPCount      = view.findViewById(R.id.tv_otp_count);
        tvPasswordCount = view.findViewById(R.id.tv_password_count);
        tvGreeting      = view.findViewById(R.id.tv_greeting);

        setGreeting();
        observeViewModel();
    }

    private void setGreeting() {
        int hour = java.util.Calendar.getInstance()
            .get(java.util.Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour < 12)      greeting = "Good morning";
        else if (hour < 17) greeting = "Good afternoon";
        else                greeting = "Good evening";
        tvGreeting.setText(greeting);
    }

    private void observeViewModel() {
        viewModel.getAllOTP().observe(getViewLifecycleOwner(), entries -> {
            if (entries != null) {
                tvOTPCount.setText(
                    entries.size() + " 2FA accounts"
                );
            }
        });

        viewModel.getAllPasswords().observe(
                getViewLifecycleOwner(), entries -> {
            if (entries != null) {
                tvPasswordCount.setText(
                    entries.size() + " passwords"
                );
            }
        });
    }
}