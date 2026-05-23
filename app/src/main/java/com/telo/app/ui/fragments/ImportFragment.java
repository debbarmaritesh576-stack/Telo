package com.telo.app.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.telo.app.R;
import com.telo.app.importers.AegisImporter;
import com.telo.app.importers.AndOTPImporter;
import com.telo.app.importers.GoogleAuthImporter;
import com.telo.app.importers.ImportResult;
import com.telo.app.importers.TwoFASImporter;

public class ImportFragment extends Fragment {

    private Button   btnImportAegis;
    private Button   btnImportGoogle;
    private Button   btnImportAndOTP;
    private Button   btnImport2FAS;
    private TextView tvImportResult;

    private String currentImporter;

    private ActivityResultLauncher<String[]> fileLauncher;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
            R.layout.fragment_import, container, false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnImportAegis  = view.findViewById(R.id.btn_import_aegis);
        btnImportGoogle = view.findViewById(R.id.btn_import_google);
        btnImportAndOTP = view.findViewById(R.id.btn_import_andotp);
        btnImport2FAS   = view.findViewById(R.id.btn_import_2fas);
        tvImportResult  = view.findViewById(R.id.tv_import_result);

        fileLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null) performImport(uri);
            }
        );

        btnImportAegis.setOnClickListener(v -> {
            currentImporter = "aegis";
            fileLauncher.launch(new String[]{"*/*"});
        });

        btnImportGoogle.setOnClickListener(v -> {
            currentImporter = "google";
            fileLauncher.launch(new String[]{"*/*"});
        });

        btnImportAndOTP.setOnClickListener(v -> {
            currentImporter = "andotp";
            fileLauncher.launch(new String[]{"*/*"});
        });

        btnImport2FAS.setOnClickListener(v -> {
            currentImporter = "2fas";
            fileLauncher.launch(new String[]{"*/*"});
        });
    }

    private void performImport(Uri uri) {
        com.telo.app.db.AppDatabase.DB_EXECUTOR.execute(() -> {
            ImportResult result;

            switch (currentImporter) {
                case "aegis":
                    result = new AegisImporter(requireContext())
                        .importFromUri(uri);
                    break;
                case "google":
                    result = new GoogleAuthImporter(requireContext())
                        .importFromUri(uri);
                    break;
                case "andotp":
                    result = new AndOTPImporter(requireContext())
                        .importFromUri(uri);
                    break;
                case "2fas":
                    result = new TwoFASImporter(requireContext())
                        .importFromUri(uri);
                    break;
                default:
                    return;
            }

            final ImportResult finalResult = result;
            requireActivity().runOnUiThread(() -> {
                if (finalResult.isSuccess()) {
                    tvImportResult.setText(
                        "Imported " +
                        finalResult.getSuccessCount() +
                        " entries successfully"
                    );
                } else {
                    tvImportResult.setText(
                        "Import failed: " +
                        finalResult.getErrorMessage()
                    );
                }
            });
        });
    }
}