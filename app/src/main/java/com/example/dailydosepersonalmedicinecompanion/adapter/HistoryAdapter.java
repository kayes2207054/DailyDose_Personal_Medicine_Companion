package com.example.dailydosepersonalmedicinecompanion.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.model.DoseHistory;

import java.util.List;

/**
 * HistoryAdapter
 * Adapter for dose history list RecyclerView
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<DoseHistory> historyList;

    public HistoryAdapter(List<DoseHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoseHistory history = historyList.get(position);
        holder.tvMedicine.setText(history.getMedicineName());
        holder.tvDate.setText(history.getDate());
        holder.tvTime.setText(history.getTime());

        // Set status icon and color
        if ("TAKEN".equals(history.getStatus())) {
            holder.tvStatus.setText("✓");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
        } else if ("MISSED".equals(history.getStatus())) {
            holder.tvStatus.setText("✗");
            holder.tvStatus.setTextColor(Color.parseColor("#F44336")); // Red
        } else if ("SNOOZED".equals(history.getStatus())) {
            holder.tvStatus.setText("⏰");
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800")); // Orange
        } else {
            holder.tvStatus.setText("○");
            holder.tvStatus.setTextColor(Color.parseColor("#9E9E9E")); // Gray
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void updateList(List<DoseHistory> newList) {
        this.historyList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicine, tvDate, tvTime, tvStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvMedicine = itemView.findViewById(R.id.tv_history_medicine);
            tvDate = itemView.findViewById(R.id.tv_history_date);
            tvTime = itemView.findViewById(R.id.tv_history_time);
            tvStatus = itemView.findViewById(R.id.tv_history_status);
        }
    }
}
