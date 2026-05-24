package com.telo.app.notes;

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
import com.telo.app.util.ColorHelper;
import com.telo.app.util.TimeHelper;

public class SecureNoteAdapter extends
        ListAdapter<SecureNote, SecureNoteAdapter.NoteViewHolder> {

    public interface OnNoteClickListener {
        void onClick(SecureNote note);
        void onDelete(SecureNote note);
        void onFavorite(SecureNote note);
    }

    private final Context             context;
    private final OnNoteClickListener listener;

    public SecureNoteAdapter(
            Context context,
            OnNoteClickListener listener) {
        super(DIFF_CALLBACK);
        this.context  = context;
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<SecureNote> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<SecureNote>() {
            @Override
            public boolean areItemsTheSame(
                    @NonNull SecureNote a, @NonNull SecureNote b) {
                return a.getId().equals(b.getId());
            }

            @Override
            public boolean areContentsTheSame(
                    @NonNull SecureNote a, @NonNull SecureNote b) {
                return a.getTitle().equals(b.getTitle()) &&
                       a.isFavorite() == b.isFavorite();
            }
        };

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.item_note_entry, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull NoteViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private final TextView    tvTitle;
        private final TextView    tvPreview;
        private final TextView    tvDate;
        private final ImageButton btnFavorite;
        private final ImageButton btnDelete;
        private final View        colorBar;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle     = itemView.findViewById(R.id.tv_note_title);
            tvPreview   = itemView.findViewById(R.id.tv_note_preview);
            tvDate      = itemView.findViewById(R.id.tv_note_date);
            btnFavorite = itemView.findViewById(R.id.btn_note_favorite);
            btnDelete   = itemView.findViewById(R.id.btn_note_delete);
            colorBar    = itemView.findViewById(R.id.view_note_color);
        }

        void bind(SecureNote note) {
            tvTitle.setText(note.getTitle());

            String content = note.getContent();
            tvPreview.setText(
                content != null && content.length() > 80
                    ? content.substring(0, 80) + "..."
                    : content
            );

            tvDate.setText(
                TimeHelper.formatRelative(note.getUpdatedAt())
            );

            if (note.getColorHex() != null) {
                colorBar.setBackgroundColor(
                    ColorHelper.parseColor(note.getColorHex())
                );
            }

            btnFavorite.setImageResource(
                note.isFavorite()
                    ? R.drawable.ic_favorite
                    : R.drawable.ic_favorite_border
            );

            itemView.setOnClickListener(v ->
                listener.onClick(note)
            );
            btnDelete.setOnClickListener(v ->
                listener.onDelete(note)
            );
            btnFavorite.setOnClickListener(v ->
                listener.onFavorite(note)
            );
        }
    }
}