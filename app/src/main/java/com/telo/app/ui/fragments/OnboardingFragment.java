package com.telo.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.telo.app.R;

public class OnboardingFragment extends Fragment {

    private static final String ARG_TITLE    = "title";
    private static final String ARG_DESC     = "desc";
    private static final String ARG_IMAGE    = "image";

    public static OnboardingFragment newInstance(
            String title, String desc, int imageRes) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC,  desc);
        args.putInt(ARG_IMAGE,    imageRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_onboarding, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView  tvTitle = view.findViewById(R.id.tv_onboarding_title);
        TextView  tvDesc  = view.findViewById(R.id.tv_onboarding_desc);
        ImageView ivImage = view.findViewById(R.id.iv_onboarding_image);

        if (getArguments() != null) {
            tvTitle.setText(getArguments().getString(ARG_TITLE));
            tvDesc.setText(getArguments().getString(ARG_DESC));
            ivImage.setImageResource(getArguments().getInt(ARG_IMAGE));
        }
    }
}