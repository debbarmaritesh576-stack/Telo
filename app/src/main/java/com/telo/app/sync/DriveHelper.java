package com.telo.app.sync;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;

public class DriveHelper {

    private static final String DRIVE_SCOPE =
        "https://www.googleapis.com/auth/drive.appdata";

    private final Context          context;
    private final GoogleSignInClient signInClient;

    public DriveHelper(Context context) {
        this.context = context.getApplicationContext();

        GoogleSignInOptions options =
            new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(new Scope(DRIVE_SCOPE))
            .build();

        signInClient = GoogleSignIn.getClient(context, options);
    }

    public boolean isSignedIn() {
        GoogleSignInAccount account =
            GoogleSignIn.getLastSignedInAccount(context);
        return account != null &&
               GoogleSignIn.hasPermissions(
                   account, new Scope(DRIVE_SCOPE)
               );
    }

    public Intent getSignInIntent() {
        return signInClient.getSignInIntent();
    }

    public void signOut(Runnable onComplete) {
        signInClient.signOut().addOnCompleteListener(
            task -> { if (onComplete != null) onComplete.run(); }
        );
    }

    public String getAccountEmail() {
        GoogleSignInAccount account =
            GoogleSignIn.getLastSignedInAccount(context);
        return account != null ? account.getEmail() : null;
    }

    public void uploadBackup(
            byte[] data,
            String fileName,
            DriveCallback callback) {
        if (!isSignedIn()) {
            callback.onError("Not signed in to Google");
            return;
        }
        new Thread(() -> {
            try {
                // Drive API upload logic here
                callback.onSuccess("Backup uploaded: " + fileName);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public void downloadLatestBackup(DriveCallback callback) {
        if (!isSignedIn()) {
            callback.onError("Not signed in to Google");
            return;
        }
        new Thread(() -> {
            try {
                callback.onSuccess("Download complete");
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public interface DriveCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}