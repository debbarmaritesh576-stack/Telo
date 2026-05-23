package com.telo.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.passwords.PasswordStrengthChecker;
import com.telo.app.util.AnimationHelper;
import com.telo.app.util.ClipboardHelper;
import com.telo.app.util.ColorHelper;

public class PasswordAdapter extends
        ListAdapter<PasswordEntry, PasswordAdapter.PasswordViewHolder> {

    public interface OnItemClickListener {
        void onClick(PasswordEntry entry);
        void onCopyPassword(PasswordEntry entry);
        void onCopyUsername(PasswordEntry entry);
        void onFavorite(PasswordEntry entry);
        void onDelete(PasswordEntry entry);
        void onLongPress(PasswordEntry entry);
    }

    private final Context             context;
    private final OnItemClickListener listener;

    public PasswordAdapter(
            Context context,
            OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.context  = context;
        this.listener = listener;
    }

    // ── DiffUtil ──────────────────────────────────────────────

    private static final DiffUtil.ItemCallback<PasswordEntry> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<PasswordEntry>() {
            @Override
            public boolean areItemsTheSame(
                    @NonNull PasswordEntry a,
                    @NonNull PasswordEntry b) {
                return a.getId().equals(b.getId());
            }

            @Override
            public boolean areContentsTheSame(
                    @NonNull PasswordEntry a,
                    @NonNull PasswordEntry b) {
                return a.getTitle().equals(b.getTitle()) &&
                       a.isFavorite() == b.isFavorite();
            }
        };

    // ── Inflate ───────────────────────────────────────────────

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.item_password_entry, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PasswordViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // ── ViewHolder ────────────────────────────────────────────

    class PasswordViewHolder extends RecyclerView.ViewHolder {

        private final TextView    tvTitle;
        private final TextView    tvUsername;
        private final TextView    tvUrl;
        private final ImageView   ivStrength;
        private final ImageButton btnCopy;
        private final ImageButton btnFavorite;
        private final TextView    tvInitial;

        PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle     = itemView.findViewById(R.id.tv_pass_title);
            tvUsername  = itemView.findViewById(R.id.tv_pass_username);
            tvUrl       = itemView.findViewById(R.id.tv_pass_url);
            ivStrength  = itemView.findViewById(R.id.iv_pass_strength);
            btnCopy     = itemView.findViewById(R.id.btn_pass_copy);
            btnFavorite = itemView.findViewById(R.id.btn_pass_favorite);
            tvInitial   = itemView.findViewById(R.id.tv_pass_initial);
        }

        void bind(PasswordEntry entry) {
            tvTitle.setText(entry.getTitle());
            tvUsername.setText(
                entry.getUsername() != null
                    ? entry.getUsername()
                    : entry.getEmail()
            );
            tvUrl.setText(
                entry.getUrl() != null ? entry.getUrl() : ""
            );

            // Initial avatar
            String initial = entry.getTitle() != null &&
                             !entry.getTitle().isEmpty()
                ? String.valueOf(entry.getTitle().charAt(0)).toUpperCase()
                : "?";
            tvInitial.setText(initial);

            // Strength indicator
            if (entry.getPassword() != null) {
                PasswordStrengthChecker.Result result =
                    PasswordStrengthChecker.check(entry.getPassword());
                int color = getStrengthColor(result.strength);
                ivStrength.setColorFilter(color);
            }

            // Favorite
            btnFavorite.setImageResource(
                entry.isFavorite()
                    ? R.drawable.ic_favorite
                    : R.drawable.ic_favorite_border
            );

            // Copy password
            btnCopy.setOnClickListener(v -> {
                ClipboardHelper.copyPassword(
                    context, entry.getPassword()
                );
                listener.onCopyPassword(entry);
                AnimationHelper.pulse(btnCopy);
            });

            // Favorite click
            btnFavorite.setOnClickListener(v ->
                listener.onFavorite(entry)
            );

            // Item click
            itemView.setOnClickListener(v ->
                listener.onClick(entry)
            );

            // Long press
            itemView.setOnLongClickListener(v -> {
                listener.onLongPress(entry);
                return true;
            });
        }

        private int getStrengthColor(
                PasswordStrengthChecker.Strength strength) {
            switch (strength) {
                case VERY_WEAK:  return ColorHelper.parseColor("#F44336");
                case WEAK:       return ColorHelper.parseColor("#FF9800");
                case FAIR:       return ColorHelper.parseColor("#FFC107");
                case STRONG:     return ColorHelper.parseColor("#4CAF50");
                case VERY_STRONG:return ColorHelper.parseColor("#2196F3");
                default:         return ColorHelper.parseColor("#9E9E9E");
            }
        }
    }
}