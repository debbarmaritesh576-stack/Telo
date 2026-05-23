package com.telo.app.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.db.OTPEntryEntity;
import com.telo.app.otp.OTPEntry;
import com.telo.app.otp.TOTPGenerator;
import com.telo.app.util.AnimationHelper;
import com.telo.app.util.ClipboardHelper;
import java.util.Timer;
import java.util.TimerTask;

public class OTPAdapter extends
        ListAdapter<OTPEntryEntity, OTPAdapter.OTPViewHolder> {

    public interface OnItemClickListener {
        void onCopy(OTPEntryEntity entry, String code);
        void onFavorite(OTPEntryEntity entry);
        void onEdit(OTPEntryEntity entry);
        void onDelete(OTPEntryEntity entry);
        void onLongPress(OTPEntryEntity entry);
    }

    private final Context             context;
    private final OnItemClickListener listener;
    private       boolean             tapToReveal;
    private final Handler             handler;

    public OTPAdapter(Context context, OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.context     = context;
        this.listener    = listener;
        this.tapToReveal = true;
        this.handler     = new Handler(Looper.getMainLooper());
    }

    // ── DiffUtil ──────────────────────────────────────────────

    private static final DiffUtil.ItemCallback<OTPEntryEntity> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<OTPEntryEntity>() {
            @Override
            public boolean areItemsTheSame(
                    @NonNull OTPEntryEntity a,
                    @NonNull OTPEntryEntity b) {
                return a.id.equals(b.id);
            }

            @Override
            public boolean areContentsTheSame(
                    @NonNull OTPEntryEntity a,
                    @NonNull OTPEntryEntity b) {
                return a.name.equals(b.name) &&
                       a.isFavorite == b.isFavorite;
            }
        };

    // ── Inflate ───────────────────────────────────────────────

    @NonNull
    @Override
    public OTPViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.item_otp_entry, parent, false);
        return new OTPViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull OTPViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setTapToReveal(boolean tapToReveal) {
        this.tapToReveal = tapToReveal;
        notifyDataSetChanged();
    }

    // ── ViewHolder ────────────────────────────────────────────

    class OTPViewHolder extends RecyclerView.ViewHolder {

        private final TextView    tvName;
        private final TextView    tvIssuer;
        private final TextView    tvCode;
        private final TextView    tvTimer;
        private final ProgressBar progressBar;
        private final ImageButton btnCopy;
        private final ImageButton btnFavorite;
        private final ImageButton btnMore;
        private final ImageView   ivIcon;

        private Timer  timer;
        private boolean revealed = false;

        OTPViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName      = itemView.findViewById(R.id.tv_otp_name);
            tvIssuer    = itemView.findViewById(R.id.tv_otp_issuer);
            tvCode      = itemView.findViewById(R.id.tv_otp_code);
            tvTimer     = itemView.findViewById(R.id.tv_otp_timer);
            progressBar = itemView.findViewById(R.id.progress_otp);
            btnCopy     = itemView.findViewById(R.id.btn_otp_copy);
            btnFavorite = itemView.findViewById(R.id.btn_otp_favorite);
            btnMore     = itemView.findViewById(R.id.btn_otp_more);
            ivIcon      = itemView.findViewById(R.id.iv_otp_icon);
        }

        void bind(OTPEntryEntity entity) {
            tvName.setText(entity.name);
            tvIssuer.setText(entity.issuer != null
                ? entity.issuer : "");

            // Favorite icon
            btnFavorite.setImageResource(
                entity.isFavorite
                    ? R.drawable.ic_favorite
                    : R.drawable.ic_favorite_border
            );

            // Tap to reveal
            if (tapToReveal && !revealed) {
                tvCode.setText("● ● ● ● ● ●");
                tvCode.setOnClickListener(v -> {
                    revealed = true;
                    startTimer(entity);
                    AnimationHelper.pulse(tvCode);
                });
            } else {
                startTimer(entity);
            }

            // Copy
            btnCopy.setOnClickListener(v -> {
                try {
                    OTPEntry entry = entity.toOTPEntry();
                    String code = TOTPGenerator.generate(entry);
                    ClipboardHelper.copyOTPCode(context, code);
                    listener.onCopy(entity, code);
                    AnimationHelper.pulse(btnCopy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Favorite
            btnFavorite.setOnClickListener(v ->
                listener.onFavorite(entity)
            );

            // Long press
            itemView.setOnLongClickListener(v -> {
                listener.onLongPress(entity);
                return true;
            });
        }

        private void startTimer(OTPEntryEntity entity) {
            if (timer != null) timer.cancel();
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> {
                        try {
                            OTPEntry entry  = entity.toOTPEntry();
                            String   code   = TOTPGenerator.generate(entry);
                            long     remaining = TOTPGenerator
                                .getRemainingSeconds(entry);
                            float    progress  = TOTPGenerator
                                .getProgress(entry);

                            tvCode.setText(formatCode(code));
                            tvTimer.setText(remaining + "s");
                            progressBar.setProgress(
                                (int) (progress * 100)
                            );

                            // Red when < 5s
                            if (remaining <= 5) {
                                tvCode.setTextColor(
                                    context.getColor(R.color.red)
                                );
                            } else {
                                tvCode.setTextColor(
                                    context.getColor(R.color.on_surface)
                                );
                            }
                        } catch (Exception e) {
                            tvCode.setText("------");
                        }
                    });
                }
            }, 0, 1000);
        }

        private String formatCode(String code) {
            if (code.length() == 6) {
                return code.substring(0, 3) + " " + code.substring(3);
            }
            return code;
        }
    }
}