package com.telo.app.security;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;

public class SecurityDashboardActivity extends AppCompatActivity {

    private SecurityDashboardViewModel viewModel;

    private ProgressBar pbScore;
    private TextView    tvScore;
    private TextView    tvLevel;
    private TextView    tvSummary;
    private TextView    tvWeakCount;
    private TextView    tvDuplicateCount;
    private TextView    tvExpiredCount;
    private TextView    tvBreachedCount;
    private TextView    tvPasswordScore;
    private TextView    tvOtpScore;
    private TextView    tvBackupScore;
    private TextView    tvVaultScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_dashboard);

        viewModel = new ViewModelProvider(this)
            .get(SecurityDashboardViewModel.class);

        setupToolbar();
        initViews();
        observeViewModel();

        viewModel.analyze();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Security Dashboard");
        }
    }

    private void initViews() {
        pbScore          = findViewById(R.id.pb_security_score);
        tvScore          = findViewById(R.id.tv_security_score);
        tvLevel          = findViewById(R.id.tv_security_level);
        tvSummary        = findViewById(R.id.tv_security_summary);
        tvWeakCount      = findViewById(R.id.tv_weak_count);
        tvDuplicateCount = findViewById(R.id.tv_duplicate_count);
        tvExpiredCount   = findViewById(R.id.tv_expired_count);
        tvBreachedCount  = findViewById(R.id.tv_breached_count);
        tvPasswordScore  = findViewById(R.id.tv_password_score);
        tvOtpScore       = findViewById(R.id.tv_otp_score);
        tvBackupScore    = findViewById(R.id.tv_backup_score);
        tvVaultScore     = findViewById(R.id.tv_vault_score);
    }

    private void observeViewModel() {
        viewModel.getReport().observe(this, report -> {
            if (report == null) return;

            SecurityScore score = report.getScore();

            pbScore.setProgress(score.getTotalScore());
            tvScore.setText(score.getTotalScore() + "/100");
            tvLevel.setText(score.getLevel().name());
            tvSummary.setText(score.getSummary());

            tvWeakCount.setText(
                String.valueOf(report.getWeakPasswords() != null
                    ? report.getWeakPasswords().size() : 0)
            );
            tvDuplicateCount.setText(
                String.valueOf(report.getDuplicatePasswords() != null
                    ? report.getDuplicatePasswords().size() : 0)
            );
            tvExpiredCount.setText(
                String.valueOf(report.getExpiredPasswords() != null
                    ? report.getExpiredPasswords().size() : 0)
            );
            tvBreachedCount.setText(
                String.valueOf(report.getBreachedPasswords() != null
                    ? report.getBreachedPasswords().size() : 0)
            );

            tvPasswordScore.setText(score.getPasswordScore() + "%");
            tvOtpScore.setText(score.getOtpScore() + "%");
            tvBackupScore.setText(score.getBackupScore() + "%");
            tvVaultScore.setText(score.getVaultScore() + "%");

            // Color based on level
            int color;
            switch (score.getLevel()) {
                case CRITICAL:  color = getColor(R.color.red);    break;
                case POOR:      color = getColor(R.color.orange);  break;
                case FAIR:      color = getColor(R.color.yellow);  break;
                case GOOD:      color = getColor(R.color.green);   break;
                case EXCELLENT: color = getColor(R.color.blue);    break;
                default:        color = getColor(R.color.gray);    break;
            }
            tvLevel.setTextColor(color);
            pbScore.setProgressTintList(
                android.content.res.ColorStateList.valueOf(color)
            );
        });

        viewModel.getLoading().observe(this, loading -> {
            // Show/hide loading
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}