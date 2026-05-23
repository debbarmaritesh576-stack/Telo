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
import java.util.ArrayList;
import java.util.List;

public class IconPackAdapter extends
        RecyclerView.Adapter<IconPackAdapter.IconViewHolder> {

    public interface OnIconSelectedListener {
        void onSelected(String iconRes);
    }

    public static class IconItem {
        public final String iconRes;
        public final String label;

        public IconItem(String iconRes, String label) {
            this.iconRes = iconRes;
            this.label   = label;
        }
    }

    private final Context                context;
    private final OnIconSelectedListener listener;
    private final List<IconItem>         icons;
    private       String                 selectedIconRes;

    public IconPackAdapter(
            Context context,
            OnIconSelectedListener listener) {
        this.context  = context;
        this.listener = listener;
        this.icons    = buildDefaultIcons();
    }

    private List<IconItem> buildDefaultIcons() {
        List<IconItem> list = new ArrayList<>();
        list.add(new IconItem("ic_otp_default",  "Default"));
        list.add(new IconItem("ic_google",        "Google"));
        list.add(new IconItem("ic_github",        "GitHub"));
        list.add(new IconItem("ic_facebook",      "Facebook"));
        list.add(new IconItem("ic_twitter",       "Twitter"));
        list.add(new IconItem("ic_microsoft",     "Microsoft"));
        list.add(new IconItem("ic_amazon",        "Amazon"));
        list.add(new IconItem("ic_discord",       "Discord"));
        list.add(new IconItem("ic_instagram",     "Instagram"));
        list.add(new IconItem("ic_steam",         "Steam"));
        list.add(new IconItem("ic_dropbox",       "Dropbox"));
        list.add(new IconItem("ic_paypal",        "PayPal"));
        list.add(new IconItem("ic_binance",       "Binance"));
        list.add(new IconItem("ic_netflix",       "Netflix"));
        list.add(new IconItem("ic_linkedin",      "LinkedIn"));
        list.add(new IconItem("ic_reddit",        "Reddit"));
        return list;
    }

    public void setSelected(String iconRes) {
        this.selectedIconRes = iconRes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.item_icon_pack, parent, false);
        return new IconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IconViewHolder holder, int position) {
        holder.bind(icons.get(position));
    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    class IconViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivIcon;
        private final TextView  tvLabel;
        private final View      container;

        IconViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon    = itemView.findViewById(R.id.iv_icon);
            tvLabel   = itemView.findViewById(R.id.tv_icon_label);
            container = itemView.findViewById(R.id.icon_container);
        }

        void bind(IconItem item) {
            tvLabel.setText(item.label);

            int resId = context.getResources().getIdentifier(
                item.iconRes, "drawable",
                context.getPackageName()
            );
            if (resId != 0) {
                ivIcon.setImageResource(resId);
            }

            boolean isSelected =
                item.iconRes.equals(selectedIconRes);

            container.setBackgroundResource(
                isSelected
                    ? R.drawable.bg_icon_selected
                    : R.drawable.bg_icon_normal
            );

            itemView.setOnClickListener(v -> {
                selectedIconRes = item.iconRes;
                notifyDataSetChanged();
                listener.onSelected(item.iconRes);
            });
        }
    }
}