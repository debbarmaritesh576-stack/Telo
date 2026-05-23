package com.telo.app.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import com.telo.app.otp.OTPAlgorithm;
import com.telo.app.otp.OTPEntry;
import com.telo.app.otp.OTPType;

@Entity(tableName = "otp_entries")
public class OTPEntryEntity {

    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String issuer;
    public String secret;
    public String type;
    public String algorithm;
    public int    digits;
    public long   period;
    public long   counter;
    public String iconName;
    public String groupId;
    public String categoryId;
    public boolean isFavorite;
    public long   createdAt;
    public long   updatedAt;

    // ── Convert to OTPEntry ───────────────────────────────────

    public OTPEntry toOTPEntry() {
        OTPEntry entry = new OTPEntry();
        entry.setId(id);
        entry.setName(name);
        entry.setIssuer(issuer);
        entry.setSecret(secret);
        entry.setType(OTPType.valueOf(type));
        entry.setAlgorithm(OTPAlgorithm.valueOf(algorithm));
        entry.setDigits(digits);
        entry.setPeriod(period);
        entry.setCounter(counter);
        entry.setIconName(iconName);
        entry.setGroupId(groupId);
        entry.setCategoryId(categoryId);
        entry.setFavorite(isFavorite);
        entry.setCreatedAt(createdAt);
        return entry;
    }

    // ── Convert from OTPEntry ─────────────────────────────────

    public static OTPEntryEntity fromOTPEntry(OTPEntry entry) {
        OTPEntryEntity entity = new OTPEntryEntity();
        entity.id         = entry.getId();
        entity.name       = entry.getName();
        entity.issuer     = entry.getIssuer();
        entity.secret     = entry.getSecret();
        entity.type       = entry.getType().name();
        entity.algorithm  = entry.getAlgorithm().name();
        entity.digits     = entry.getDigits();
        entity.period     = entry.getPeriod();
        entity.counter    = entry.getCounter();
        entity.iconName   = entry.getIconName();
        entity.groupId    = entry.getGroupId();
        entity.categoryId = entry.getCategoryId();
        entity.isFavorite = entry.isFavorite();
        entity.createdAt  = entry.getCreatedAt();
        entity.updatedAt  = System.currentTimeMillis();
        return entity;
    }
}