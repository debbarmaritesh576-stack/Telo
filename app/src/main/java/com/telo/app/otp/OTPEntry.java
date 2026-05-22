package com.telo.app.otp;

import java.util.UUID;

public class OTPEntry {

    private String id;
    private String name;
    private String issuer;
    private String secret;
    private OTPType type;
    private OTPAlgorithm algorithm;
    private int digits;
    private long period;
    private long counter;
    private String iconName;
    private String groupId;
    private String categoryId;
    private boolean isFavorite;
    private long createdAt;

    public OTPEntry() {
        this.id        = UUID.randomUUID().toString();
        this.type      = OTPType.TOTP;
        this.algorithm = OTPAlgorithm.SHA1;
        this.digits    = 6;
        this.period    = 30;
        this.counter   = 0;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId()              { return id; }
    public String getName()            { return name; }
    public String getIssuer()          { return issuer; }
    public String getSecret()          { return secret; }
    public OTPType getType()           { return type; }
    public OTPAlgorithm getAlgorithm() { return algorithm; }
    public int getDigits()             { return digits; }
    public long getPeriod()            { return period; }
    public long getCounter()           { return counter; }
    public String getIconName()        { return iconName; }
    public String getGroupId()         { return groupId; }
    public String getCategoryId()      { return categoryId; }
    public boolean isFavorite()        { return isFavorite; }
    public long getCreatedAt()         { return createdAt; }

    public void setId(String id)             { this.id = id; }
    public void setName(String name)         { this.name = name; }
    public void setIssuer(String issuer)     { this.issuer = issuer; }
    public void setSecret(String secret)     { this.secret = secret; }
    public void setType(OTPType type)        { this.type = type; }
    public void setAlgorithm(OTPAlgorithm a) { this.algorithm = a; }
    public void setDigits(int digits)        { this.digits = digits; }
    public void setPeriod(long period)       { this.period = period; }
    public void setCounter(long counter)     { this.counter = counter; }
    public void setIconName(String icon)     { this.iconName = icon; }
    public void setGroupId(String groupId)   { this.groupId = groupId; }
    public void setCategoryId(String cat)    { this.categoryId = cat; }
    public void setFavorite(boolean fav)     { this.isFavorite = fav; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public void incrementCounter() {
        this.counter++;
    }
}