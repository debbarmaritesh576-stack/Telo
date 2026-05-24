package com.telo.app.sync;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.telo.app.R;
import com.telo.app.util.TimeHelper;

public class SyncActivity extends AppCompatActivity {

    private SyncViewModel viewModel;
    private DriveHelper   driveHelper;

    private Button      btnSyncNow;
    private Button      btnSignIn;
    private Button      btnSignOut;
    private Switch      switchAutoSync;
    private TextView    tvSyncStatus;
    private TextView    tvLastSync;
    private TextView    tvAccount;
    private ProgressBar pbSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        viewModel   = new ViewModelProvider(this)
            .get(SyncViewModel.class);
        driveHelper = new DriveHelper(this);

        setupToolbar();
        initViews();
        observeViewModel();
        setupListeners();
        updateSignInState();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cloud Sync");
        }
    }

    private void initViews() {
        btnSyncNow     = findViewById(R.id.btn_sync_now);
        btnSignIn      = findViewById(R.id.btn_sign_in);
        btnSignOut     = findViewById(R.id.btn_sign_out);
        switchAutoSync = findViewById(R.id.switch_auto_sync);
        tvSyncStatus   = findViewById(R.id.tv_sync_status);
        tvLastSync     = findViewById(R.id.tv_last_sync);
        tvAccount      = findViewById(R.id.tv_sync_account);
        pbSync         = findViewById(R.id.pb_sync);
        pbSync.setVisibility(View.GONE);
    }

    private void observeViewModel() {
        viewModel.getStatus().observe(this, status -> {
            tvSyncStatus.setText(status.getMessage() != null
                ? status.getMessage() : status.getState().name());
            pbSync.setVisibility(
                status.isSyncing() ? View.VISIBLE : View.GONE
            );
            if (status.getLastSyncTime() > 0) {
                tvLastSync.setText(
                    "Last sync: " + TimeHelper.formatRelative(
                        status.getLastSyncTime()
                    )
                );
            }
        });

        viewModel.getAutoSync().observe(this, enabled ->
            switchAutoSync.setChecked(enabled)
        );
    }

    private void setupListeners() {
        btnSyncNow.setOnClickListener(v -> viewModel.syncNow());

        switchAutoSync.setOnCheckedChangeListener((v, checked) ->
            viewModel.setAutoSync(checked)
        );

        btnSignIn.setOnClickListener(v ->
            startActivityForResult(
                driveHelper.getSignInIntent(), 1001
            )
        );

        btnSignOut.setOnClickListener(v ->
            driveHelper.signOut(this::updateSignInState)
        );
    }

    private void updateSignInState() {
        boolean signedIn = driveHelper.isSignedIn();
        btnSignIn.setVisibility(signedIn ? View.GONE : View.VISIBLE);
        btnSignOut.setVisibility(signedIn ? View.VISIBLE : View.GONE);
        btnSyncNow.setEnabled(signedIn);
        switchAutoSync.setEnabled(signedIn);

        if (signedIn) {
            tvAccount.setText(driveHelper.getAccountEmail());
        } else {
            tvAccount.setText("Not signed in");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}