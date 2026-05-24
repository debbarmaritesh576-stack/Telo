package com.telo.app.cards;

import java.util.UUID;

public class CardEntry {

    public enum CardType {
        VISA,
        MASTERCARD,
        AMEX,
        RUPAY,
        DISCOVER,
        OTHER
    }

    private String   id;
    private String   cardholderName;
    private String   cardNumber;      // encrypted
    private String   expiryMonth;
    private String   expiryYear;
    private String   cvv;             // encrypted
    private String   bankName;
    private CardType cardType;
    private String   categoryId;
    private String   notes;
    private boolean  isFavorite;
    private long     createdAt;
    private long     updatedAt;

    public CardEntry() {
        this.id        = UUID.randomUUID().toString();
        this.cardType  = CardType.OTHER;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // ── Getters ───────────────────────────────────────────────

    public String   getId()             { return id; }
    public String   getCardholderName() { return cardholderName; }
    public String   getCardNumber()     { return cardNumber; }
    public String   getExpiryMonth()    { return expiryMonth; }
    public String   getExpiryYear()     { return expiryYear; }
    public String   getCvv()            { return cvv; }
    public String   getBankName()       { return bankName; }
    public CardType getCardType()       { return cardType; }
    public String   getCategoryId()     { return categoryId; }
    public String   getNotes()          { return notes; }
    public boolean  isFavorite()        { return isFavorite; }
    public long     getCreatedAt()      { return createdAt; }
    public long     getUpdatedAt()      { return updatedAt; }

    // ── Setters ───────────────────────────────────────────────

    public void setId(String id)                     { this.id = id; }
    public void setCardholderName(String name)        { this.cardholderName = name; }
    public void setCardNumber(String number)          { this.cardNumber = number; }
    public void setExpiryMonth(String month)          { this.expiryMonth = month; }
    public void setExpiryYear(String year)            { this.expiryYear = year; }
    public void setCvv(String cvv)                    { this.cvv = cvv; }
    public void setBankName(String bankName)          { this.bankName = bankName; }
    public void setCardType(CardType cardType)        { this.cardType = cardType; }
    public void setCategoryId(String categoryId)      { this.categoryId = categoryId; }
    public void setNotes(String notes)                { this.notes = notes; }
    public void setFavorite(boolean favorite)         { this.isFavorite = favorite; }
    public void setCreatedAt(long createdAt)          { this.createdAt = createdAt; }
    public void setUpdatedAt(long updatedAt)          { this.updatedAt = updatedAt; }

    public void touch() {
        this.updatedAt = System.currentTimeMillis();
    }

    // ── Helpers ───────────────────────────────────────────────

    public String getMaskedNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "**** **** **** ****";
        }
        return "**** **** **** " +
            cardNumber.substring(cardNumber.length() - 4);
    }

    public String getExpiry() {
        return expiryMonth + "/" + expiryYear;
    }

    public boolean isExpired() {
        try {
            int month = Integer.parseInt(expiryMonth);
            int year  = Integer.parseInt("20" + expiryYear);
            java.util.Calendar now = java.util.Calendar.getInstance();
            int currentYear  = now.get(java.util.Calendar.YEAR);
            int currentMonth = now.get(java.util.Calendar.MONTH) + 1;
            if (year < currentYear) return true;
            if (year == currentYear && month < currentMonth) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}