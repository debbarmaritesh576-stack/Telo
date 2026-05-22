package com.telo.app.vault;

import android.content.Context;
import com.google.gson.Gson;
import com.telo.app.otp.OTPEntry;
import java.io.*;
import java.util.List;

public class VaultManager {

    private static final String VAULT_FILE = "telo_vault.enc";

    private final Context context;
    private final Gson    gson;
    private       Vault   vault;
    private       byte[]  masterKey;

    private static VaultManager instance;

    private VaultManager(Context context) {
        this.context = context.getApplicationContext();
        this.gson    = new Gson();
        this.vault   = new Vault();
    }

    public static synchronized VaultManager getInstance(Context context) {
        if (instance == null) {
            instance = new VaultManager(context);
        }
        return instance;
    }

    // ── Vault Init ────────────────────────────────────────────

    public boolean vaultExists() {
        return new File(context.getFilesDir(), VAULT_FILE).exists();
    }

    public void createVault(char[] password) throws Exception {
        byte[] salt      = VaultEncryption.generateSalt();
        byte[] key       = VaultEncryption.deriveKeyFromPassword(password, salt);
        byte[] masterKey = VaultEncryption.generateMasterKey();
        byte[] iv        = VaultEncryption.generateIv();
        byte[] encKey    = VaultEncryption.encrypt(masterKey, key, iv);

        PasswordSlot slot = new PasswordSlot();
        slot.setSalt(salt);
        slot.setEncryptedMasterKey(encKey);
        slot.setNonce(iv);

        this.vault = new Vault();
        this.vault.addSlot(slot);
        this.masterKey = masterKey;

        saveVault();
    }

    public boolean unlockWithPassword(char[] password) throws Exception {
        Vault loaded = loadVaultFromDisk();

        for (Slot slot : loaded.getSlots()) {
            if (slot.getType() == Slot.Type.PASSWORD) {
                PasswordSlot ps = (PasswordSlot) slot;
                byte[] key = VaultEncryption.deriveKeyFromPassword(
                    password, ps.getSalt()
                );
                try {
                    byte[] mk = VaultEncryption.decrypt(
                        ps.getEncryptedMasterKey(), key, ps.getNonce()
                    );
                    this.masterKey = mk;
                    this.vault     = loaded;
                    decryptEntries();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    public void lock() {
        VaultEncryption.wipeBytes(masterKey);
        masterKey = null;
        vault.lock();
    }

    public boolean isUnlocked() {
        return masterKey != null;
    }

    // ── Entry Operations ──────────────────────────────────────

    public void addOTPEntry(OTPEntry entry) throws Exception {
        VaultEntry ve = new VaultEntry();
        ve.setEntryType(VaultEntry.EntryType.OTP);
        ve.setOtpEntry(entry);
        vault.addEntry(ve);
        saveVault();
    }

    public void removeEntry(String id) throws Exception {
        vault.removeEntry(id);
        saveVault();
    }

    public void updateEntry(VaultEntry entry) throws Exception {
        vault.updateEntry(entry);
        saveVault();
    }

    public List<VaultEntry> getEntries() {
        return vault.getEntries();
    }

    // ── Persistence ───────────────────────────────────────────

    private void saveVault() throws Exception {
        byte[] plaintext = VaultSerializer.toBytes(vault.getEntries());
        byte[] iv        = VaultEncryption.generateIv();
        byte[] encrypted = VaultEncryption.encrypt(plaintext, masterKey, iv);

        vault.setIv(iv);
        vault.setEncryptedContent(encrypted);

        File file = new File(context.getFilesDir(), VAULT_FILE);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(gson.toJson(vault).getBytes());
        }
    }

    private Vault loadVaultFromDisk() throws Exception {
        File file = new File(context.getFilesDir(), VAULT_FILE);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return gson.fromJson(new String(data), Vault.class);
        }
    }

    private void decryptEntries() throws Exception {
        byte[] plaintext = VaultEncryption.decrypt(
            vault.getEncryptedContent(), masterKey, vault.getIv()
        );
        List<VaultEntry> entries = VaultSerializer.fromBytes(plaintext);
        vault.setEntries(entries);
        vault.setLocked(false);
    }

    // ── Panic Wipe ────────────────────────────────────────────

    public void panicWipe() {
        File file = new File(context.getFilesDir(), VAULT_FILE);
        if (file.exists()) file.delete();
        VaultEncryption.wipeBytes(masterKey);
        masterKey = null;
        vault     = new Vault();
    }
}