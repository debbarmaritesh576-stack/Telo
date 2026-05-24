package com.telo.app.cards;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(
    tableName = "cards",
    indices = {
        @Index("categoryId"),
        @Index("isFavorite")
    }
)
public class CardEntity {

    @PrimaryKey
    @NonNull
    public String  id;
    public String  cardholderName;
    public String  encryptedCardNumber;
    public String  expiryMonth;
    public String  expiryYear;
    public String  encryptedCvv;
    public String  bankName;
    public String  cardType;
    public String  categoryId;
    public String  encryptedNotes;
    public boolean isFavorite;
    public long    createdAt;
    public long    updatedAt;
}