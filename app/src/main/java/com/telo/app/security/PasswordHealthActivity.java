package com.telo.app.security;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.adapters.PasswordAdapter;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.passwords.PasswordExpiryChecker;
import com.telo.app.passwords.PasswordRepository;
import java.util.List;

public class PasswordHealthActivity extends AppCompatActivity {

    private PasswordRepository repository;
    private PasswordAdapter    weakAdapter;
    private PasswordAdapter    expiredAdapter;
    private PasswordAdapter    duplicateAdapter;
    private RecyclerView       rvWeak;
    private RecyclerView       rvExpired;
    private RecyclerView       rvDuplicates;
    private TextView           tvWeakHeader;
    private TextView           tvExpiredHeader;
    private TextView           tvDuplicatesHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_health);

        repository = new PasswordRepository(getApplication());

        setupToolbar();
        initViews();
        loadData();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Password Health");
        }
    }

    private void initViews() {
        rvWeak             = findViewById(R.id.rv_weak_passwords);
        rvExpired          = findViewById(R.id.rv_expired_passwords);
        rvDuplicates       = findViewById(R.id.rv_duplicate_passwords);
        tvWeakHeader       = findViewById(R.id.tv_weak_header);
        tvExpiredHeader    = findViewById(R.id.tv_expired_header);
        tvDuplicatesHeader = findViewById(R.id.tv_duplicates_header);

        rvWeak.setLayoutManager(
            new LinearLayoutManager(this)
        );
        rvExpired.setLayoutManager(
            new LinearLayoutManager(this)
        );
        rvDuplicates.setLayoutManager(
            new LinearLayoutManager(this)
        );
    }

    private void loadData() {
        repository.getAll().observe(this, passwords -> {
            if (passwords == null) return;

            // Weak passwords
            List<PasswordEntry> weak =
                SecurityAnalyzer.findWeak(passwords);
            tvWeakHeader.setText(
                "Weak Passwords (" + weak.size() + ")"
            );
            setupAdapter(rvWeak, weak);

            // Expired passwords
            List<PasswordEntry> expired =
                PasswordExpiryChecker.getExpired(passwords);
            tvExpiredHeader.setText(
                "Expired Passwords (" + expired.size() + ")"
            );
            setupAdapter(rvExpired, expired);

            // Duplicate passwords
            List<DuplicateFinder.DuplicateGroup> groups =
                DuplicateFinder.findDuplicatePasswords(passwords);
            int dupCount = groups.stream()
                .mapToInt(g -> g.entries.size())
                .sum();
            tvDuplicatesHeader.setText(
                "Duplicate Passwords (" + dupCount + ")"
            );
        });
    }

    private void setupAdapter(
            RecyclerView rv, List<PasswordEntry> list) {
        PasswordAdapter adapter = new PasswordAdapter(
            this,
            new PasswordAdapter.OnItemClickListener() {
                @Override
                public void onClick(PasswordEntry entry) {}
                @Override
                public void onCopyPassword(PasswordEntry entry) {}
                @Override
                public void onCopyUsername(PasswordEntry entry) {}
                @Override
                public void onFavorite(PasswordEntry entry) {}
                @Override
                public void onDelete(PasswordEntry entry) {}
                @Override
                public void onLongPress(PasswordEntry entry) {}
            }
        );
        adapter.submitList(list);
        rv.setAdapter(adapter);

        rv.setVisibility(
            list.isEmpty() ? View.GONE : View.VISIBLE
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}