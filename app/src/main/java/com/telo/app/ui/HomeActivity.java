package com.telo.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.telo.app.R;
import com.telo.app.adapters.CategoryChipAdapter;
import com.telo.app.adapters.OTPAdapter;
import com.telo.app.adapters.PasswordAdapter;
import com.telo.app.db.CategoryEntity;
import com.telo.app.db.OTPEntryEntity;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.security.AutoLockManager;
import com.telo.app.util.PreferenceHelper;
import com.telo.app.util.ScreenshotBlocker;
import com.telo.app.viewmodels.HomeViewModel;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private HomeViewModel        viewModel;
    private OTPAdapter           otpAdapter;
    private PasswordAdapter      passwordAdapter;
    private CategoryChipAdapter  categoryAdapter;

    private RecyclerView         rvMain;
    private RecyclerView         rvCategories;
    private FloatingActionButton fab;
    private BottomNavigationView bottomNav;
    private SearchView           searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ScreenshotBlocker.apply(
            this, PreferenceHelper.isScreenshotBlocked()
        );

        viewModel = new ViewModelProvider(this)
            .get(HomeViewModel.class);

        initViews();
        setupAdapters();
        setupBottomNav();
        setupFab();
        setupSearch();
        observeViewModel();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvMain       = findViewById(R.id.rv_main);
        rvCategories = findViewById(R.id.rv_categories);
        fab          = findViewById(R.id.fab_add);
        bottomNav    = findViewById(R.id.bottom_nav);
        searchView   = findViewById(R.id.search_view);
    }

    private void setupAdapters() {
        // OTP Adapter
        otpAdapter = new OTPAdapter(this, new OTPAdapter.OnItemClickListener() {
            @Override
            public void onCopy(OTPEntryEntity entry, String code) {
                showSnackbar("Copied — clears in 30s");
            }
            @Override
            public void onFavorite(OTPEntryEntity entry) {
                viewModel.getAllOTP();
            }
            @Override
            public void onEdit(OTPEntryEntity entry) {
                Intent intent = new Intent(
                    HomeActivity.this, EditOTPActivity.class
                );
                intent.putExtra("entry_id", entry.id);
                startActivity(intent);
            }
            @Override
            public void onDelete(OTPEntryEntity entry) {
                showDeleteDialog(entry.id, entry.name);
            }
            @Override
            public void onLongPress(OTPEntryEntity entry) {
                showEntryOptions(entry);
            }
        });

        // Password Adapter
        passwordAdapter = new PasswordAdapter(this,
            new PasswordAdapter.OnItemClickListener() {
                @Override
                public void onClick(PasswordEntry entry) {
                    Intent intent = new Intent(
                        HomeActivity.this, EditPasswordActivity.class
                    );
                    intent.putExtra("entry_id", entry.getId());
                    startActivity(intent);
                }
                @Override
                public void onCopyPassword(PasswordEntry entry) {
                    showSnackbar("Password copied — clears in 30s");
                }
                @Override
                public void onCopyUsername(PasswordEntry entry) {
                    showSnackbar("Username copied");
                }
                @Override
                public void onFavorite(PasswordEntry entry) {}
                @Override
                public void onDelete(PasswordEntry entry) {}
                @Override
                public void onLongPress(PasswordEntry entry) {}
            }
        );

        // Category Adapter
        categoryAdapter = new CategoryChipAdapter(this, category -> {
            viewModel.setSelectedCategory(category.id);
        });

        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setLayoutManager(
            new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)
        );
        rvMain.setAdapter(otpAdapter);
        rvCategories.setAdapter(categoryAdapter);
    }

    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_otp) {
                viewModel.setActiveTab(HomeViewModel.TAB_OTP);
                rvMain.setAdapter(otpAdapter);
                return true;
            } else if (id == R.id.nav_password) {
                viewModel.setActiveTab(HomeViewModel.TAB_PASSWORD);
                rvMain.setAdapter(passwordAdapter);
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupFab() {
        fab.setOnClickListener(v -> showAddBottomSheet());
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    viewModel.setSearchQuery(newText);
                    return true;
                }
            }
        );
    }

    private void observeViewModel() {
        // OTP entries
        viewModel.getAllOTP().observe(this, entries -> {
            otpAdapter.submitList(entries);
        });

        // Password entries
        viewModel.getAllPasswords().observe(this, entries -> {
            passwordAdapter.submitList(entries);
        });

        // Categories
        viewModel.getAllCategories().observe(this, categories -> {
            categoryAdapter.setCategories(categories);
        });

        // Selected category
        viewModel.getSelectedCategory().observe(this, categoryId -> {
            categoryAdapter.setSelected(categoryId);
            if (categoryId.equals("all")) {
                viewModel.getAllOTP().observe(this,
                    entries -> otpAdapter.submitList(entries));
            } else {
                viewModel.getOTPByCategory(categoryId).observe(this,
                    entries -> otpAdapter.submitList(entries));
            }
        });
    }

    // ── Helpers ───────────────────────────────────────────────

    private void showSnackbar(String message) {
        com.google.android.material.snackbar.Snackbar
            .make(fab, message,
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
            .show();
    }

    private void showDeleteDialog(String id, String name) {
        new android.app.AlertDialog.Builder(this)
            .setTitle("Delete " + name + "?")
            .setMessage("This cannot be undone")
            .setPositiveButton("Delete", (d, w) -> {
                // delete
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showEntryOptions(OTPEntryEntity entry) {
        // Show bottom sheet options
    }

    private void showAddBottomSheet() {
        // Show add options bottom sheet
    }

    @Override
    protected void onResume() {
        super.onResume();
        AutoLockManager.getInstance(this).onUserActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AutoLockManager.getInstance(this).onAppBackground();
    }
}