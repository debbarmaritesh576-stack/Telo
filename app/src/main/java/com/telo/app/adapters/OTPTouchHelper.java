package com.telo.app.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class OTPTouchHelper extends ItemTouchHelper.SimpleCallback {

    public interface TouchHelperListener {
        void onSwipeLeft(int position);
        void onSwipeRight(int position);
        void onMoved(int fromPosition, int toPosition);
    }

    private final TouchHelperListener listener;

    public OTPTouchHelper(TouchHelperListener listener) {
        super(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        );
        this.listener = listener;
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target) {
        listener.onMoved(
            viewHolder.getAdapterPosition(),
            target.getAdapterPosition()
        );
        return true;
    }

    @Override
    public void onSwiped(
            @NonNull RecyclerView.ViewHolder viewHolder,
            int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            listener.onSwipeLeft(position);
        } else {
            listener.onSwipeRight(position);
        }
    }

    @Override
    public float getSwipeThreshold(
            @NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.5f;
    }
}