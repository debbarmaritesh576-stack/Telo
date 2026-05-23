package com.telo.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.telo.app.R;
import com.telo.app.db.GroupEntity;
import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends
        RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    public interface OnGroupClickListener {
        void onClick(GroupEntity group);
        void onDelete(GroupEntity group);
        void onEdit(GroupEntity group);
    }

    private final Context              context;
    private final OnGroupClickListener listener;
    private       List<GroupEntity>    groups;

    public GroupAdapter(
            Context context,
            OnGroupClickListener listener) {
        this.context  = context;
        this.listener = listener;
        this.groups   = new ArrayList<>();
    }

    public void setGroups(List<GroupEntity> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull GroupViewHolder holder, int position) {
        holder.bind(groups.get(position));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        private final TextView    tvName;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName    = itemView.findViewById(R.id.tv_group_name);
            btnEdit   = itemView.findViewById(R.id.btn_group_edit);
            btnDelete = itemView.findViewById(R.id.btn_group_delete);
        }

        void bind(GroupEntity group) {
            tvName.setText(group.name);

            itemView.setOnClickListener(v ->
                listener.onClick(group)
            );
            btnEdit.setOnClickListener(v ->
                listener.onEdit(group)
            );
            btnDelete.setOnClickListener(v ->
                listener.onDelete(group)
            );
        }
    }
}