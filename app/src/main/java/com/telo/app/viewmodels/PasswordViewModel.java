package com.telo.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.passwords.PasswordFilter;
import com.telo.app.passwords.PasswordRepository;
import com.telo.app.passwords.PasswordSearchHelper;
import java.util.List;

public class PasswordViewModel extends AndroidViewModel {

    private final PasswordRepository       repository;
    private final MutableLiveData<String>  searchQuery;
    private final MutableLiveData<String>  selectedCategory;
    private final MutableLiveData<String>  errorLiveData;
    private final MutableLiveData<Boolean> loadingLiveData;
    private       PasswordFilter.SortBy    sortBy;

    public PasswordViewModel(@NonNull Application application) {
        super(application);
        repository       = new PasswordRepository(application);
        searchQuery      = new MutableLiveData<>("");
        selectedCategory = new MutableLiveData<>("all");
        errorLiveData    = new MutableLiveData<>();
        loadingLiveData  = new MutableLiveData<>(false);
        sortBy           = PasswordFilter.SortBy.NAME_ASC;
    }

    // ── Read ──────────────────────────────────────────────────

    public LiveData<List<PasswordEntry>> getAllPasswords() {
        return repository.getAll();
    }

    public LiveData<List<PasswordEntry>> getByCategory(String categoryId) {
        return repository.getByCategory(categoryId);
    }

    public LiveData<List<PasswordEntry>> getFavorites() {
        return repository.getFavorites();
    }

    public LiveData<List<PasswordEntry>> search(String query) {
        searchQuery.setValue(query);
        return repository.search(query);
    }

    // ── Write ─────────────────────────────────────────────────

    public void addPassword(PasswordEntry entry) {
        loadingLiveData.setValue(true);
        try {
            repository.insert(entry);
        } catch (Exception e) {
            errorLiveData.setValue("Failed: " + e.getMessage());
        } finally {
            loadingLiveData.setValue(false);
        }
    }

    public void updatePassword(PasswordEntry entry) {
        try {
            repository.update(entry);
        } catch (Exception e) {
            errorLiveData.setValue("Update failed: " + e.getMessage());
        }
    }

    public void deletePassword(String id) {
        repository.delete(id);
    }

    public void toggleFavorite(PasswordEntry entry) {
        entry.setFavorite(!entry.isFavorite());
        repository.update(entry);
    }

    // ── Sort ──────────────────────────────────────────────────

    public void setSortBy(PasswordFilter.SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public PasswordFilter.SortBy getSortBy() {
        return sortBy;
    }

    // ── Category ──────────────────────────────────────────────

    public void setSelectedCategory(String categoryId) {
        selectedCategory.setValue(categoryId);
    }

    public LiveData<String> getSelectedCategory() {
        return selectedCategory;
    }

    // ── State ─────────────────────────────────────────────────

    public LiveData<String>  getError()   { return errorLiveData; }
    public LiveData<Boolean> getLoading() { return loadingLiveData; }
}