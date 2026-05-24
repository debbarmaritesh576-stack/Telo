package com.telo.app.notes;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class SecureNoteViewModel extends AndroidViewModel {

    private final SecureNoteRepository    repository;
    private final MutableLiveData<String> errorLiveData;
    private       SecureNote              currentNote;

    public SecureNoteViewModel(@NonNull Application application) {
        super(application);
        repository    = new SecureNoteRepository(application);
        errorLiveData = new MutableLiveData<>();
        currentNote   = new SecureNote();
    }

    // ── Read ──────────────────────────────────────────────────

    public LiveData<List<SecureNote>> getAllNotes() {
        return repository.getAll();
    }

    public LiveData<List<SecureNote>> getFavorites() {
        return repository.getFavorites();
    }

    // ── Write ─────────────────────────────────────────────────

    public void saveNote(String title, String content) {
        currentNote.setTitle(title);
        currentNote.setContent(content);

        if (currentNote.getCreatedAt() == 0) {
            repository.insert(currentNote);
        } else {
            repository.update(currentNote);
        }
    }

    public void deleteNote(SecureNote note) {
        repository.delete(note.getId());
    }

    public void toggleFavorite(SecureNote note) {
        note.setFavorite(!note.isFavorite());
        repository.update(note);
    }

    public void togglePin(SecureNote note) {
        note.setPinned(!note.isPinned());
        repository.update(note);
    }

    public void setCurrentNote(SecureNote note) {
        this.currentNote = note;
    }

    public void setColor(String colorHex) {
        currentNote.setColorHex(colorHex);
    }

    public SecureNote getCurrentNote() { return currentNote; }
    public LiveData<String> getError() { return errorLiveData; }
}