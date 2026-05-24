package com.telo.app.cards;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;

public class CardActivity extends AppCompatActivity {

    private CardViewModel viewModel;
    private CardAdapter   adapter;
    private RecyclerView  rvCards;
    private View          formContainer;
    private EditText      etHolder;
    private EditText      etNumber;
    private EditText      etExpiryMonth;
    private EditText      etExpiryYear;
    private EditText      etCvv;
    private EditText      etBank;
    private Button        btnSave;
    private Button        btnAddCard;
    private TextView      tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        viewModel = new ViewModelProvider(this)
            .get(CardViewModel.class);

        setupToolbar();
        initViews();
        setupAdapter();
        setupListeners();
        observeViewModel();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cards");
        }
    }

    private void initViews() {
        rvCards        = findViewById(R.id.rv_cards);
        formContainer  = findViewById(R.id.card_form_container);
        etHolder       = findViewById(R.id.et_card_holder);
        etNumber       = findViewById(R.id.et_card_number);
        etExpiryMonth  = findViewById(R.id.et_card_expiry_month);
        etExpiryYear   = findViewById(R.id.et_card_expiry_year);
        etCvv          = findViewById(R.id.et_card_cvv);
        etBank         = findViewById(R.id.et_card_bank);
        btnSave        = findViewById(R.id.btn_card_save);
        btnAddCard     = findViewById(R.id.btn_add_card);
        tvEmpty        = findViewById(R.id.tv_cards_empty);

        formContainer.setVisibility(View.GONE);
        rvCards.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupAdapter() {
        adapter = new CardAdapter(this,
            new CardAdapter.OnCardClickListener() {
                @Override
                public void onClick(CardEntry card) {
                    viewModel.setCurrentCard(card);
                    showForm();
                    populateForm(card);
                }
                @Override
                public void onCopyNumber(CardEntry card) {
                    showSnackbar("Card number copied — clears in 30s");
                }
                @Override
                public void onFavorite(CardEntry card) {
                    viewModel.toggleFavorite(card);
                }
                @Override
                public void onDelete(CardEntry card) {
                    showDeleteConfirm(card);
                }
            }
        );
        rvCards.setAdapter(adapter);
    }

    private void setupListeners() {
        btnAddCard.setOnClickListener(v -> {
            showForm();
        });

        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setCardNumber(s.toString());
            }
        });

        btnSave.setOnClickListener(v -> {
            viewModel.setCardholderName(
                etHolder.getText().toString()
            );
            viewModel.setCardNumber(
                etNumber.getText().toString()
            );
            viewModel.setExpiry(
                etExpiryMonth.getText().toString(),
                etExpiryYear.getText().toString()
            );
            viewModel.setCvv(etCvv.getText().toString());
            viewModel.setBankName(etBank.getText().toString());
            viewModel.saveCard();
            hideForm();
        });
    }

    private void observeViewModel() {
        viewModel.getAllCards().observe(this, cards -> {
            adapter.submitList(cards);
            tvEmpty.setVisibility(
                cards == null || cards.isEmpty()
                    ? View.VISIBLE : View.GONE
            );
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                showSnackbar(error);
            }
        });
    }

    private void showForm() {
        formContainer.setVisibility(View.VISIBLE);
        rvCards.setVisibility(View.GONE);
    }

    private void hideForm() {
        formContainer.setVisibility(View.GONE);
        rvCards.setVisibility(View.VISIBLE);
        clearForm();
    }

    private void populateForm(CardEntry card) {
        etHolder.setText(card.getCardholderName());
        etNumber.setText(card.getMaskedNumber());
        etExpiryMonth.setText(card.getExpiryMonth());
        etExpiryYear.setText(card.getExpiryYear());
        etBank.setText(card.getBankName());
    }

    private void clearForm() {
        etHolder.setText("");
        etNumber.setText("");
        etExpiryMonth.setText("");
        etExpiryYear.setText("");
        etCvv.setText("");
        etBank.setText("");
    }

    private void showDeleteConfirm(CardEntry card) {
        new android.app.AlertDialog.Builder(this)
            .setTitle("Delete card?")
            .setMessage("This cannot be undone")
            .setPositiveButton("Delete", (d, w) ->
                viewModel.deleteCard(card.getId())
            )
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showSnackbar(String msg) {
        com.google.android.material.snackbar.Snackbar
            .make(btnAddCard, msg,
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}