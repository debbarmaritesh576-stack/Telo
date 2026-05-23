package com.telo.app.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.telo.app.R;
import com.telo.app.otp.OTPEntry;
import com.telo.app.util.QRParser;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanQRActivity extends AppCompatActivity {

    public static final String EXTRA_OTP_ENTRY = "otp_entry_json";
    private static final int   CAMERA_PERMISSION_CODE = 100;

    private PreviewView     previewView;
    private ExecutorService cameraExecutor;
    private boolean         scanned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        previewView    = findViewById(R.id.preview_view);
        cameraExecutor = Executors.newSingleThreadExecutor();

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_CODE
            );
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> future =
            ProcessCameraProvider.getInstance(this);

        future.addListener(() -> {
            try {
                ProcessCameraProvider provider = future.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(
                    previewView.getSurfaceProvider()
                );

                ImageAnalysis analysis = new ImageAnalysis.Builder()
                    .setBackpressureStrategy(
                        ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build();

                analysis.setAnalyzer(cameraExecutor, imageProxy -> {
                    if (scanned) {
                        imageProxy.close();
                        return;
                    }

                    @SuppressWarnings("UnsafeExperimentalUsageError")
                    android.media.Image mediaImage =
                        imageProxy.getImage();

                    if (mediaImage != null) {
                        InputImage image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.getImageInfo()
                                      .getRotationDegrees()
                        );

                        BarcodeScanner scanner =
                            BarcodeScanning.getClient(
                                new BarcodeScannerOptions.Builder()
                                    .setBarcodeFormats(
                                        Barcode.FORMAT_QR_CODE)
                                    .build()
                            );

                        scanner.process(image)
                            .addOnSuccessListener(barcodes -> {
                                for (Barcode barcode : barcodes) {
                                    String raw = barcode.getRawValue();
                                    if (raw != null) {
                                        handleQRResult(raw);
                                    }
                                }
                            })
                            .addOnCompleteListener(
                                task -> imageProxy.close()
                            );
                    } else {
                        imageProxy.close();
                    }
                });

                provider.unbindAll();
                provider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                );

            } catch (Exception e) {
                Toast.makeText(this,
                    "Camera error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void handleQRResult(String raw) {
        if (scanned) return;
        scanned = true;

        try {
            OTPEntry entry = QRParser.parse(raw);
            Intent result  = new Intent();
            result.putExtra("otp_name",   entry.getName());
            result.putExtra("otp_issuer", entry.getIssuer());
            result.putExtra("otp_secret", entry.getSecret());
            result.putExtra("otp_type",   entry.getType().name());
            setResult(RESULT_OK, result);
            finish();
        } catch (Exception e) {
            scanned = false;
            runOnUiThread(() ->
                Toast.makeText(this,
                    "Invalid QR code",
                    Toast.LENGTH_SHORT).show()
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        );
        if (requestCode == CAMERA_PERMISSION_CODE &&
            grantResults.length > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            Toast.makeText(this,
                "Camera permission required",
                Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}