package com.telo.app.vault;

import com.telo.app.otp.OTPEntry;
import java.util.UUID;

public class VaultEntry {

    public enum EntryType {
        OTP,
        PASSWORD
    }

    private String    id;
    private EntryType entryType;
    private OTPEntry  otpEntry;
    private String    groupId;
    private String    categoryId;
    private long      createdAt;
    private long      updatedAt;

    public VaultEntry() {
        this.id        = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public String getId()            { return id; }
    public EntryType getEntryType()  { return entryType; }
    public OTPEntry getOtpEntry()    { return otpEntry; }
    public String getGroupId()       { return groupId; }
    public String getCategoryId()    { return categoryId; }
    public long getCreatedAt()       { return createdAt; }
    public long getUpdatedAt()       { return updatedAt; }

    public void setId(String id)                   { this.id = id; }
    public void setEntryType(EntryType entryType)  { this.entryType = entryType; }
    public void setOtpEntry(OTPEntry otpEntry)     { this.otpEntry = otpEntry; }
    public void setGroupId(String groupId)         { this.groupId = groupId; }
    public void setCategoryId(String categoryId)   { this.categoryId = categoryId; }
    public void setCreatedAt(long createdAt)       { this.createdAt = createdAt; }
    public void setUpdatedAt(long updatedAt)       { this.updatedAt = updatedAt; }

    public void touch() {
        this.updatedAt = System.currentTimeMillis();
    }
}