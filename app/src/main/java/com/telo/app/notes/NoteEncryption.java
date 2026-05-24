package com.telo.app.notes;

import com.telo.app.crypto.CryptoManager;

public class NoteEncryption {

    public static SecureNoteEntity encrypt(
            SecureNote note) throws Exception {
        SecureNoteEntity entity = new SecureNoteEntity();
        entity.id        = note.getId();
        entity.title     = note.getTitle();
        entity.categoryId = note.getCategoryId();
        entity.isFavorite = note.isFavorite();
        entity.isPinned   = note.isPinned();
        entity.colorHex   = note.getColorHex();
        entity.createdAt  = note.getCreatedAt();
        entity.updatedAt  = note.getUpdatedAt();

        // Encrypt content
        if (note.getContent() != null) {
            entity.encryptedContent =
                CryptoManager.encryptToBase64(note.getContent());
        }

        return entity;
    }

    public static SecureNote decrypt(
            SecureNoteEntity entity) throws Exception {
        SecureNote note = new SecureNote();
        note.setId(entity.id);
        note.setTitle(entity.title);
        note.setCategoryId(entity.categoryId);
        note.setFavorite(entity.isFavorite);
        note.setPinned(entity.isPinned);
        note.setColorHex(entity.colorHex);
        note.setCreatedAt(entity.createdAt);
        note.setUpdatedAt(entity.updatedAt);

        // Decrypt content
        if (entity.encryptedContent != null) {
            note.setContent(
                CryptoManager.decryptFromBase64(
                    entity.encryptedContent
                )
            );
        }

        return note;
    }
}