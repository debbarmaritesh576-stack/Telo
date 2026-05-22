package com.telo.app.vault;

import java.util.ArrayList;
import java.util.List;

public class Vault {

    private String         version;
    private List<Slot>     slots;
    private List<VaultEntry> entries;
    private byte[]         encryptedContent;
    private byte[]         iv;
    private boolean        isLocked;

    public Vault() {
        this.version  = "1.0";
        this.slots    = new ArrayList<>();
        this.entries  = new ArrayList<>();
        this.isLocked = true;
    }

    // ── Entry Management ──────────────────────────────────────

    public void addEntry(VaultEntry entry) {
        entries.add(entry);
    }

    public void removeEntry(String id) {
        entries.removeIf(e -> e.getId().equals(id));
    }

    public VaultEntry findEntry(String id) {
        for (VaultEntry e : entries) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    public void updateEntry(VaultEntry updated) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getId().equals(updated.getId())) {
                updated.touch();
                entries.set(i, updated);
                return;
            }
        }
    }

    // ── Slot Management ───────────────────────────────────────

    public void addSlot(Slot slot) {
        slots.add(slot);
    }

    public void removeSlot(String id) {
        slots.removeIf(s -> s.getId().equals(id));
    }

    // ── Getters & Setters ─────────────────────────────────────

    public String getVersion()              { return version; }
    public List<Slot> getSlots()            { return slots; }
    public List<VaultEntry> getEntries()    { return entries; }
    public byte[] getEncryptedContent()     { return encryptedContent; }
    public byte[] getIv()                   { return iv; }
    public boolean isLocked()               { return isLocked; }

    public void setVersion(String version)               { this.version = version; }
    public void setSlots(List<Slot> slots)               { this.slots = slots; }
    public void setEntries(List<VaultEntry> entries)     { this.entries = entries; }
    public void setEncryptedContent(byte[] content)      { this.encryptedContent = content; }
    public void setIv(byte[] iv)                         { this.iv = iv; }
    public void setLocked(boolean locked)                { this.isLocked = locked; }

    public void lock() {
        this.isLocked = true;
        this.entries.clear();
    }
}