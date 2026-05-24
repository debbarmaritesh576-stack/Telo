package com.telo.app.cards;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class CardViewModel extends AndroidViewModel {

    private final CardRepository          repository;
    private final MutableLiveData<String> errorLiveData;
    private       CardEntry               currentCard;

    public CardViewModel(@NonNull Application application) {
        super(application);
        repository    = new CardRepository(application);
        errorLiveData = new MutableLiveData<>();
        currentCard   = new CardEntry();
    }

    // ── Read ──────────────────────────────────────────────────

    public LiveData<List<CardEntry>> getAllCards() {
        return repository.getAll();
    }

    public LiveData<List<CardEntry>> getFavorites() {
        return repository.getFavorites();
    }

    // ── Write ─────────────────────────────────────────────────

    public void saveCard() {
        if (!validate()) return;
        if (currentCard.getCreatedAt() == 0) {
            repository.insert(currentCard);
        } else {
            repository.update(currentCard);
        }
    }

    public void deleteCard(String id) {
        repository.delete(id);
    }

    public void toggleFavorite(CardEntry card) {
        card.setFavorite(!card.isFavorite());
        repository.update(card);
    }

    // ── Field Updates ─────────────────────────────────────────

    public void setCardholderName(String name) {
        currentCard.setCardholderName(name);
    }

    public void setCardNumber(String number) {
        currentCard.setCardNumber(
            CardNumberFormatter.stripSpaces(number)
        );
        currentCard.setCardType(
            CardNumberFormatter.detectType(number)
        );
    }

    public void setExpiry(String month, String year) {
        currentCard.setExpiryMonth(month);
        currentCard.setExpiryYear(year);
    }

    public void setCvv(String cvv) {
        currentCard.setCvv(cvv);
    }

    public void setBankName(String bank) {
        currentCard.setBankName(bank);
    }

    // ── Validate ──────────────────────────────────────────────

    private boolean validate() {
        if (!CardValidator.isValidCardholder(
                currentCard.getCardholderName())) {
            errorLiveData.setValue("Invalid cardholder name");
            return false;
        }
        if (!CardValidator.isValidNumber(
                currentCard.getCardNumber())) {
            errorLiveData.setValue("Invalid card number");
            return false;
        }
        if (!CardValidator.isValidExpiry(
                currentCard.getExpiryMonth(),
                currentCard.getExpiryYear())) {
            errorLiveData.setValue("Invalid expiry date");
            return false;
        }
        return true;
    }

    public void setCurrentCard(CardEntry card) {
        this.currentCard = card;
    }

    public CardEntry getCurrentCard()  { return currentCard; }
    public LiveData<String> getError() { return errorLiveData; }
}