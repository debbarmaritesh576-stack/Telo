package com.telo.app.passwords;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.telo.app.db.AppDatabase;
import com.telo.app.db.CategoryDao;
import com.telo.app.db.CategoryEntity;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryManager {

    private final CategoryDao dao;

    public CategoryManager(Application app) {
        dao = AppDatabase.getInstance(app).categoryDao();
    }

    // ── Read ──────────────────────────────────────────────────

    public LiveData<List<Category>> getAllCategories() {
        return Transformations.map(dao.getAll(), entities ->
            entities.stream()
                .map(this::toCategory)
                .collect(Collectors.toList())
        );
    }

    // ── Write ─────────────────────────────────────────────────

    public void addCategory(String name, String iconRes, String colorHex) {
        AppDatabase.DB_EXECUTOR.execute(() -> {
            CategoryEntity entity = CategoryEntity.create(
                UUID.randomUUID().toString(),
                name,
                iconRes,
                colorHex,
                0
            );
            dao.insert(entity);
        });
    }

    public void deleteCategory(String id) {
        AppDatabase.DB_EXECUTOR.execute(() ->
            dao.deleteById(id)
        );
    }

    public void updateCategory(Category category) {
        AppDatabase.DB_EXECUTOR.execute(() ->
            dao.update(toEntity(category))
        );
    }

    // ── Mapping ───────────────────────────────────────────────

    private Category toCategory(CategoryEntity e) {
        return new Category(e.id, e.name, e.iconRes, e.colorHex, e.sortOrder);
    }

    private CategoryEntity toEntity(Category c) {
        return CategoryEntity.create(
            c.getId(), c.getName(),
            c.getIconRes(), c.getColorHex(), c.getSortOrder()
        );
    }
}