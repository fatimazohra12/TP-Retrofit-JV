package com.example.tp_retrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> items;
    private Context context;
    private OnItemActionListener listener;  // Interface to handle item actions

    // Constructor
    public ItemAdapter(List<Item> items, Context context, OnItemActionListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.itemName.setText(item.getName());
        holder.itemDescription.setText(item.getDescription());

        // Set listeners for modify and delete buttons
        holder.modifyButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onModifyClick(item);  // Trigger the modify action
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(item);  // Trigger the delete action
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder class to hold the views for each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemDescription;
        Button modifyButton, deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            modifyButton = itemView.findViewById(R.id.btnModify);
            deleteButton = itemView.findViewById(R.id.btnDelete);
        }
    }

    // Interface to handle item actions (modify and delete)
    public interface OnItemActionListener {
        void onModifyClick(Item item);
        void onDeleteClick(Item item);
    }
}
