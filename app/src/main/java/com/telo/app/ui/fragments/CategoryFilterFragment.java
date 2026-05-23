package com.telo.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.adapters.CategoryChipAdapter;
import com.telo.app.viewmodels.HomeViewModel;

public class CategoryFilterFragment extends Fragment {

    private HomeViewModel       viewModel;
    private CategoryChipAdapter adapter;
    private RecyclerView        rvCategories;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_category_filter, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel    = new ViewModelProvider(requireActivity())
            .get(HomeViewModel.class);
        rvCategories = view.findViewById(R.id.rv_filter_categories);

        adapter = new CategoryChipAdapter(
            requireContext(),
            category -> viewModel.setSelectedCategory(category.id)
        );

        rvCategories.setLayoutManager(
            new LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        );
        rvCategories.setAdapter(adapter);

        viewModel.getAllCategories().observe(
                getViewLifecycleOwner(),
                categories -> adapter.setCategories(categories)
        );

        viewModel.getSelectedCategory().observe(
                getViewLifecycleOwner(),
                categoryId -> adapter.setSelected(categoryId)
        );
    }
}