package com.telo.app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.adapters.GroupAdapter;
import com.telo.app.db.GroupEntity;
import com.telo.app.passwords.CategoryManager;

public class CategoryManagerActivity extends AppCompatActivity {

    private CategoryManager categoryManager;
    private RecyclerView    rvCategories;
    private Button          btnAdd;
    private EditText        etCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);

        categoryManager = new CategoryManager(getApplication());

        setupToolbar();
        initViews();
        observeCategories();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Categories");
        }
    }

    private void initViews() {
        rvCategories   = findViewById(R.id.rv_categories);
        btnAdd         = findViewById(R.id.btn_add_category);
        etCategoryName = findViewById(R.id.et_category_name);

        rvCategories.setLayoutManager(new LinearLayoutManager(this));
    }

    private void observeCategories() {
        categoryManager.getAllCategories().observe(this, categories -> {
            // Update adapter
        });
    }

    private void setupListeners() {
        btnAdd.setOnClickListener(v -> {
            String name = etCategoryName.getText().toString().trim();
            if (!name.isEmpty()) {
                categoryManager.addCategory(
                    name, "ic_category_all", "#6200EE"
                );
                etCategoryName.setText("");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}