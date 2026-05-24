package com.telo.app.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.util.ClipboardHelper;

public class CardAdapter extends
        ListAdapter<CardEntry, CardAdapter.CardViewHolder> {

    public interface OnCardClickListener {
        void onClick(CardEntry card);
        void onCopyNumber(CardEntry card);
        void onFavorite(CardEntry card);
        void onDelete(CardEntry card);
    }

    private final Context             context;
    private final OnCardClickListener listener;

    public CardAdapter(
            Context context,
            OnCardClickListener listener) {
        super(DIFF_CALLBACK);
        this.context  = context;
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<CardEntry>
        DIFF_CALLBACK = new DiffUtil.ItemCallback<CardEntry>() {
            @Override
            public boolean areItemsTheSame(
                    @NonNull CardEntry a, @NonNull CardEntry b) {
                return a.getId().equals(b.getId());
            }

            @Override
            public boolean areContentsTheSame(
                    @NonNull CardEntry a, @NonNull CardEntry b) {
                return a.getCardholderName()
                    .equals(b.getCardholderName()) &&
                    a.isFavorite() == b.isFavorite();
            }
        };

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.item_card_entry, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CardViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        private final TextView    tvCardHolder;
        private final TextView    tvCardNumber;
        private final TextView    tvExpiry;
        private final TextView    tvBank;
        private final TextView    tvCardType;
        private final ImageButton btnCopy;
        private final ImageButton btnFavorite;
        private final ImageButton btnDelete;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardHolder = itemView.findViewById(R.id.tv_card_holder);
            tvCardNumber = itemView.findViewById(R.id.tv_card_number);
            tvExpiry     = itemView.findViewById(R.id.tv_card_expiry);
            tvBank       = itemView.findViewById(R.id.tv_card_bank);
            tvCardType   = itemView.findViewById(R.id.tv_card_type);
            btnCopy      = itemView.findViewById(R.id.btn_card_copy);
            btnFavorite  = itemView.findViewById(R.id.btn_card_favorite);
            btnDelete    = itemView.findViewById(R.id.btn_card_delete);
        }

        void bind(CardEntry card) {
            tvCardHolder.setText(card.getCardholderName());
            tvCardNumber.setText(card.getMaskedNumber());
            tvExpiry.setText(card.getExpiry());
            tvBank.setText(card.getBankName() != null
                ? card.getBankName() : "");
            tvCardType.setText(card.getCardType().name());

            if (card.isExpired()) {
                tvExpiry.setTextColor(
                    context.getColor(R.color.red)
                );
            }

            btnFavorite.setImageResource(
                card.isFavorite()
                    ? R.drawable.ic_favorite
                    : R.drawable.ic_favorite_border
            );

            itemView.setOnClickListener(v ->
                listener.onClick(card)
            );
            btnCopy.setOnClickListener(v -> {
                ClipboardHelper.copyPassword(
                    context, card.getCardNumber()
                );
                listener.onCopyNumber(card);
            });
            btnFavorite.setOnClickListener(v ->
                listener.onFavorite(card)
            );
            btnDelete.setOnClickListener(v ->
                listener.onDelete(card)
            );
        }
    }
}