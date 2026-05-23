package com.telo.app.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.telo.app.R;
import com.telo.app.viewmodels.BackupViewModel;

public class ExportFragment extends Fragment {

    private BackupViewModel viewModel;
    private Button          btnExportEncrypted;
    private Button          btnExportPlain;
    private Button          btnExportCSV;
    private TextView        tvExportStatus;

    private ActivityResultLauncher<String> exportLauncher;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_export, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity())
            .get(BackupViewModel.class);

        btnExportEncrypted = view.findViewById(R.id.btn_export_encrypted);
        btnExportPlain     = view.findViewById(R.id.btn_export_plain);
        btnExportCSV       = view.findViewById(R.id.btn_export_csv);
        tvExportStatus     = view.findViewById(R.id.tv_export_status);

        exportLauncher = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("*/*"),
            uri -> {
                if (uri != null) {
                    showPasswordAndExport(uri);
                }
            }
        );

        btnExportEncrypted.setOnClickListener(v ->
            exportLauncher.launch("telo_backup.telo")
        );

        btnExportPlain.setOnClickListener(v ->
            exportLauncher.launch("telo_backup.json")
        );

        btnExportCSV.setOnClickListener(v ->
            exportLauncher.launch("telo_backup.csv")
        );

        viewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) tvExportStatus.setText(msg);
        });
    }

    private void showPasswordAndExport(Uri uri) {
        EditText input = new EditText(requireContext());
        input.setHint("Backup password");
        input.setInputType(
            android.text.InputType.TYPE_CLASS_TEXT |
            android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        new android.app.AlertDialog.Builder(requireContext())
            .setTitle("Set Backup Password")
            .setView(input)
            .setPositiveButton("Export", (d, w) -> {
                String pass = input.getText().toString();
                if (!pass.isEmpty()) {
                    viewModel.exportEncrypted(uri, pass.toCharArray());
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
}