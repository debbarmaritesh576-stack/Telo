package com.telo.app.passwords;

import java.util.UUID;

public class PasswordEntry {

    private String  id;
    private String  title;
    private String  username;
    private String  email;
    private String  password;
    private String  url;
    private String  notes;
    private String  categoryId;
    private String  groupId;
    private boolean isFavorite;
    private long    createdAt;
    private long    updatedAt;
    private long    passwordChangedAt;
    private boolean hasTotp;
    private String  totpId;

    public PasswordEntry() {
        this.id               = UUID.randomUUID().toString();
        this.createdAt        = System.currentTimeMillis();
        this.updatedAt        = System.currentTimeMillis();
        this.passwordChangedAt = System.currentTimeMillis();
    }

    public String getId()               { return id; }
    public String getTitle()            { return title; }
    public String getUsername()         { return username; }
    public String getEmail()            { return email; }
    public String getPassword()         { return password; }
    public String getUrl()              { return url; }
    public String getNotes()            { return notes; }
    public String getCategoryId()       { return categoryId; }
    public String getGroupId()          { return groupId; }
    public boolean isFavorite()         { return isFavorite; }
    public long getCreatedAt()          { return createdAt; }
    public long getUpdatedAt()          { return updatedAt; }
    public long getPasswordChangedAt()  { return passwordChangedAt; }
    public boolean isHasTotp()          { return hasTotp; }
    public String getTotpId()           { return totpId; }

    public void setId(String id)                       { this.id = id; }
    public void setTitle(String title)                 { this.title = title; }
    public void setUsername(String username)           { this.username = username; }
    public void setEmail(String email)                 { this.email = email; }
    public void setPassword(String password)           { this.password = password; }
    public void setUrl(String url)                     { this.url = url; }
    public void setNotes(String notes)                 { this.notes = notes; }
    public void setCategoryId(String categoryId)       { this.categoryId = categoryId; }
    public void setGroupId(String groupId)             { this.groupId = groupId; }
    public void setFavorite(boolean favorite)          { this.isFavorite = favorite; }
    public void setCreatedAt(long createdAt)           { this.createdAt = createdAt; }
    public void setUpdatedAt(long updatedAt)           { this.updatedAt = updatedAt; }
    public void setPasswordChangedAt(long time)        { this.passwordChangedAt = time; }
    public void setHasTotp(boolean hasTotp)            { this.hasTotp = hasTotp; }
    public void setTotpId(String totpId)               { this.totpId = totpId; }

    public void touch() {
        this.updatedAt = System.currentTimeMillis();
    }
}