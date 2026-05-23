package com.telo.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.db.CategoryEntity;
import com.telo.app.util.ColorHelper;
import java.util.ArrayList;
import java.util.List;

public class CategoryChipAdapter extends
        RecyclerView.Adapter<CategoryChipAdapter.ChipViewHolder> {

    public interface OnCategoryClickListener {
        void onCategorySelected(CategoryEntity category);
    }

    private final Context                  context;
    private final OnCategoryClickListener  listener;
    private       List<CategoryEntity>     categories;
    private       String                   selectedId;

    public CategoryChipAdapter(
            Context context,
            OnCategoryClickListener listener) {
        this.context    = context;
        this.listener   = listener;
        this.categories = new ArrayList<>();
        this.selectedId = "all";
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setSelected(String categoryId) {
        this.selectedId = categoryId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.item_category_chip, parent, false);
        return new ChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ChipViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ChipViewHolder extends RecyclerView.ViewHolder {

        private final TextView  tvName;
        private final ImageView ivIcon;
        private final View      container;

        ChipViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName    = itemView.findViewById(R.id.tv_chip_name);
            ivIcon    = itemView.findViewById(R.id.iv_chip_icon);
            container = itemView.findViewById(R.id.chip_container);
        }

        void bind(CategoryEntity category) {
            tvName.setText(category.name);

            boolean isSelected = category.id.equals(selectedId);

            // Selected state styling
            if (isSelected) {
                int color = ColorHelper.parseColor(
                    category.colorHex != null
                        ? category.colorHex : "#6200EE"
                );
                container.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(color)
                );
                tvName.setTextColor(
                    ContextCompat.getColor(context, R.color.white)
                );
            } else {
                container.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        context, R.color.surface_variant
                    )
                );
                tvName.setTextColor(
                    ContextCompat.getColor(context, R.color.on_surface)
                );
            }

            // Icon
            if (category.iconRes != null) {
                int resId = context.getResources().getIdentifier(
                    category.iconRes, "drawable",
                    context.getPackageName()
                );
                if (resId != 0) {
                    ivIcon.setImageResource(resId);
                    ivIcon.setVisibility(View.VISIBLE);
                } else {
                    ivIcon.setVisibility(View.GONE);
                }
            }

            // Click
            itemView.setOnClickListener(v -> {
                selectedId = category.id;
                notifyDataSetChanged();
                listener.onCategorySelected(category);
            });
        }
    }
}