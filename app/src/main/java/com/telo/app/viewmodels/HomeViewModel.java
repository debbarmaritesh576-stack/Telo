package com.telo.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.telo.app.db.AppDatabase;
import com.telo.app.db.CategoryDao;
import com.telo.app.db.CategoryEntity;
import com.telo.app.db.OTPEntryEntity;
import com.telo.app.db.OTPEntryDao;
import com.telo.app.db.PasswordEntryEntity;
import com.telo.app.db.PasswordEntryDao;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final OTPEntryDao      otpDao;
    private final PasswordEntryDao passwordDao;
    private final CategoryDao      categoryDao;

    private final MutableLiveData<String>  selectedCategory;
    private final MutableLiveData<String>  searchQuery;
    private final MutableLiveData<Integer> activeTab;

    public static final int TAB_OTP      = 0;
    public static final int TAB_PASSWORD = 1;
    public static final int TAB_NOTES    = 2;
    public static final int TAB_CARDS    = 3;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db  = AppDatabase.getInstance(application);
        otpDao          = db.otpEntryDao();
        passwordDao     = db.passwordEntryDao();
        categoryDao     = db.categoryDao();
        selectedCategory = new MutableLiveData<>("all");
        searchQuery      = new MutableLiveData<>("");
        activeTab        = new MutableLiveData<>(TAB_OTP);
    }

    // ── Data ──────────────────────────────────────────────────

    public LiveData<List<OTPEntryEntity>> getAllOTP() {
        return otpDao.getAll();
    }

    public LiveData<List<OTPEntryEntity>> getOTPByCategory(String id) {
        return otpDao.getByCategory(id);
    }

    public LiveData<List<OTPEntryEntity>> searchOTP(String query) {
        return otpDao.search(query);
    }

    public LiveData<List<PasswordEntryEntity>> getAllPasswords() {
        return passwordDao.getAll();
    }

    public LiveData<List<CategoryEntity>> getAllCategories() {
        return categoryDao.getAll();
    }

    // ── Category ──────────────────────────────────────────────

    public void setSelectedCategory(String categoryId) {
        selectedCategory.setValue(categoryId);
    }

    public LiveData<String> getSelectedCategory() {
        return selectedCategory;
    }

    // ── Search ────────────────────────────────────────────────

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    // ── Tab ───────────────────────────────────────────────────

    public void setActiveTab(int tab) {
        activeTab.setValue(tab);
    }

    public LiveData<Integer> getActiveTab() {
        return activeTab;
    }

    // ── Stats ─────────────────────────────────────────────────

    public int getOTPCount() {
        return otpDao.getCount();
    }

    public int getPasswordCount() {
        return passwordDao.getCount();
    }
}