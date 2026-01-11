package com.example.dailydosepersonalmedicinecompanion.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.model.Inventory;

import java.util.List;

/**
 * InventoryAdapter
 * Adapter for inventory list RecyclerView
 */
public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private List<Inventory> inventoryList;

    public InventoryAdapter(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inventory inventory = inventoryList.get(position);
        holder.tvMedicine.setText(inventory.getMedicineName());
        holder.tvStock.setText("Stock: " + inventory.getStockLevel());
        holder.tvThreshold.setText("Low Stock Alert: " + inventory.getLowStockThreshold());

        // Highlight low stock items
        if (inventory.isLowStock()) {
            holder.tvStock.setTextColor(Color.RED);
        } else {
            holder.tvStock.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public void updateList(List<Inventory> newList) {
        this.inventoryList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicine, tvStock, tvThreshold;

        ViewHolder(View itemView) {
            super(itemView);
            tvMedicine = itemView.findViewById(R.id.tv_inventory_medicine);
            tvStock = itemView.findViewById(R.id.tv_stock_level);
            tvThreshold = itemView.findViewById(R.id.tv_threshold);
        }
    }
}
