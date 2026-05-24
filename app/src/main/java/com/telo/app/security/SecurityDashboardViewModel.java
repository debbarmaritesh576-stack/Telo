package com.telo.app.security;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.telo.app.db.AppDatabase;
import com.telo.app.db.OTPEntryDao;
import com.telo.app.db.PasswordEntryDao;
import com.telo.app.db.PasswordEntryEntity;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.passwords.PasswordExpiryChecker;
import java.util.ArrayList;
import java.util.List;

public class SecurityDashboardViewModel extends AndroidViewModel {

    private final PasswordEntryDao            passwordDao;
    private final OTPEntryDao                 otpDao;
    private final MutableLiveData<SecurityReport> reportLiveData;
    private final MutableLiveData<Boolean>    loadingLiveData;

    public SecurityDashboardViewModel(
            @NonNull Application application) {
        super(application);
        AppDatabase db  = AppDatabase.getInstance(application);
        passwordDao     = db.passwordEntryDao();
        otpDao          = db.otpEntryDao();
        reportLiveData  = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>(false);
    }

    public void analyze() {
        loadingLiveData.setValue(true);
        AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                // Get all passwords
                List<PasswordEntryEntity> entities =
                    passwordDao.getAllSync();

                List<PasswordEntry> passwords = new ArrayList<>();
                for (PasswordEntryEntity e : entities) {
                    PasswordEntry entry = new PasswordEntry();
                    entry.setId(e.id);
                    entry.setTitle(e.title);
                    entry.setUsername(e.username);
                    try {
                        entry.setPassword(
                            com.telo.app.crypto.CryptoManager
                                .decryptFromBase64(e.encryptedPassword)
                        );
                    } catch (Exception ex) {
                        entry.setPassword("");
                    }
                    entry.setPasswordChangedAt(e.passwordChangedAt);
                    entry.setHasTotp(e.hasTotp);
                    passwords.add(entry);
                }

                int otpCount = otpDao.getCount();

                // Build report
                SecurityReport report = new SecurityReport();
                SecurityScore  score  = SecurityAnalyzer.analyze(
                    passwords, otpCount
                );
                report.setScore(score);
                report.setWeakPasswords(
                    SecurityAnalyzer.findWeak(passwords)
                );
                report.setDuplicatePasswords(
                    SecurityAnalyzer.findDuplicates(passwords)
                );
                report.setExpiredPasswords(
                    PasswordExpiryChecker.getExpired(passwords)
                );
                report.setExpiringSoon(
                    PasswordExpiryChecker.getExpiringSoon(passwords)
                );
                report.setTotalPasswords(passwords.size());
                report.setTotalOTP(otpCount);

                reportLiveData.postValue(report);
                loadingLiveData.postValue(false);

            } catch (Exception e) {
                loadingLiveData.postValue(false);
            }
        });
    }

    public LiveData<SecurityReport> getReport()  { return reportLiveData; }
    public LiveData<Boolean>        getLoading() { return loadingLiveData; }
}