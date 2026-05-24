package com.telo.app.cards;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.telo.app.crypto.CryptoManager;
import com.telo.app.db.AppDatabase;
import java.util.List;
import java.util.stream.Collectors;

public class CardRepository {

    private final CardDao dao;

    public CardRepository(Application app) {
        dao = AppDatabase.getInstance(app).cardDao();
    }

    // ── Read ──────────────────────────────────────────────────

    public LiveData<List<CardEntry>> getAll() {
        return Transformations.map(dao.getAll(), entities ->
            entities.stream()
                .map(this::toEntry)
                .filter(e -> e != null)
                .collect(Collectors.toList())
        );
    }

    public LiveData<List<CardEntry>> getFavorites() {
        return Transformations.map(dao.getFavorites(), entities ->
            entities.stream()
                .map(this::toEntry)
                .filter(e -> e != null)
                .collect(Collectors.toList())
        );
    }

    // ── Write ─────────────────────────────────────────────────

    public void insert(CardEntry entry) {
        com.telo.app.db.AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                dao.insert(toEntity(entry));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void update(CardEntry entry) {
        entry.touch();
        com.telo.app.db.AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                dao.update(toEntity(entry));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void delete(String id) {
        com.telo.app.db.AppDatabase.DB_EXECUTOR.execute(() ->
            dao.deleteById(id)
        );
    }

    // ── Mapping ───────────────────────────────────────────────

    private CardEntry toEntry(CardEntity e) {
        try {
            CardEntry entry = new CardEntry();
            entry.setId(e.id);
            entry.setCardholderName(e.cardholderName);
            entry.setCardNumber(
                CryptoManager.decryptFromBase64(e.encryptedCardNumber)
            );
            entry.setExpiryMonth(e.expiryMonth);
            entry.setExpiryYear(e.expiryYear);
            entry.setCvv(
                CryptoManager.decryptFromBase64(e.encryptedCvv)
            );
            entry.setBankName(e.bankName);
            entry.setCardType(CardEntry.CardType.valueOf(e.cardType));
            entry.setCategoryId(e.categoryId);
            if (e.encryptedNotes != null) {
                entry.setNotes(
                    CryptoManager.decryptFromBase64(e.encryptedNotes)
                );
            }
            entry.setFavorite(e.isFavorite);
            entry.setCreatedAt(e.createdAt);
            entry.setUpdatedAt(e.updatedAt);
            return entry;
        } catch (Exception ex) {
            return null;
        }
    }

    private CardEntity toEntity(CardEntry e) throws Exception {
        CardEntity entity = new CardEntity();
        entity.id                  = e.getId();
        entity.cardholderName      = e.getCardholderName();
        entity.encryptedCardNumber = CryptoManager
            .encryptToBase64(e.getCardNumber());
        entity.expiryMonth         = e.getExpiryMonth();
        entity.expiryYear          = e.getExpiryYear();
        entity.encryptedCvv        = CryptoManager
            .encryptToBase64(e.getCvv());
        entity.bankName            = e.getBankName();
        entity.cardType            = e.getCardType().name();
        entity.categoryId          = e.getCategoryId();
        entity.encryptedNotes      = e.getNotes() != null
            ? CryptoManager.encryptToBase64(e.getNotes()) : null;
        entity.isFavorite          = e.isFavorite();
        entity.createdAt           = e.getCreatedAt();
        entity.updatedAt           = e.getUpdatedAt();
        return entity;
    }
}