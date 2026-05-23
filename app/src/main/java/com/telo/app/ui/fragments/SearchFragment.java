package com.telo.app.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.adapters.OTPAdapter;
import com.telo.app.adapters.PasswordAdapter;
import com.telo.app.db.OTPEntryEntity;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.viewmodels.OTPViewModel;
import com.telo.app.viewmodels.PasswordViewModel;

public class SearchFragment extends Fragment {

    private OTPViewModel      otpViewModel;
    private PasswordViewModel passwordViewModel;
    private EditText          etSearch;
    private RecyclerView      rvOTPResults;
    private RecyclerView      rvPasswordResults;
    private TextView          tvOTPHeader;
    private TextView          tvPasswordHeader;
    private TextView          tvNoResults;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_search, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        otpViewModel      = new ViewModelProvider(requireActivity())
            .get(OTPViewModel.class);
        passwordViewModel = new ViewModelProvider(requireActivity())
            .get(PasswordViewModel.class);

        etSearch          = view.findViewById(R.id.et_search);
        rvOTPResults      = view.findViewById(R.id.rv_search_otp);
        rvPasswordResults = view.findViewById(R.id.rv_search_password);
        tvOTPHeader       = view.findViewById(R.id.tv_search_otp_header);
        tvPasswordHeader  = view.findViewById(R.id.tv_search_pass_header);
        tvNoResults       = view.findViewById(R.id.tv_search_empty);

        setupRecyclerViews();
        setupSearch();

        // Auto focus
        etSearch.requestFocus();
    }

    private void setupRecyclerViews() {
        rvOTPResults.setLayoutManager(
            new LinearLayoutManager(requireContext())
        );
        rvPasswordResults.setLayoutManager(
            new LinearLayoutManager(requireContext())
        );
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                } else {
                    clearResults();
                }
            }
        });
    }

    private void performSearch(String query) {
        otpViewModel.search(query).observe(
                getViewLifecycleOwner(), entries -> {
            tvOTPHeader.setVisibility(
                entries != null && !entries.isEmpty()
                    ? View.VISIBLE : View.GONE
            );
        });

        passwordViewModel.search(query).observe(
                getViewLifecycleOwner(), entries -> {
            tvPasswordHeader.setVisibility(
                entries != null && !entries.isEmpty()
                    ? View.VISIBLE : View.GONE
            );
        });
    }

    private void clearResults() {
        tvOTPHeader.setVisibility(View.GONE);
        tvPasswordHeader.setVisibility(View.GONE);
    }
}