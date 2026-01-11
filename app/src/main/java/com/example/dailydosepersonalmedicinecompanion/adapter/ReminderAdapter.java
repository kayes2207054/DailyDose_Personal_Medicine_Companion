package com.example.dailydosepersonalmedicinecompanion.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.model.Reminder;

import java.util.List;

/**
 * ReminderAdapter
 * Adapter for reminder list RecyclerView
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private List<Reminder> reminderList;
    private final OnReminderClickListener takenListener;
    private final OnReminderClickListener deleteListener;

    public interface OnReminderClickListener {
        void onClick(Reminder reminder);
    }

    public ReminderAdapter(List<Reminder> reminderList, OnReminderClickListener takenListener, OnReminderClickListener deleteListener) {
        this.reminderList = reminderList;
        this.takenListener = takenListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);
        holder.tvMedicine.setText(reminder.getMedicineName());
        holder.tvDate.setText(reminder.getDate());
        holder.tvTime.setText(reminder.getTime());
        holder.tvStatus.setText(reminder.getStatus());

        // Color code by status
        if ("TAKEN".equals(reminder.getStatus())) {
            holder.tvStatus.setTextColor(Color.GREEN);
            holder.btnMarkTaken.setEnabled(false);
        } else if ("MISSED".equals(reminder.getStatus())) {
            holder.tvStatus.setTextColor(Color.RED);
            holder.btnMarkTaken.setEnabled(false);
        } else {
            holder.tvStatus.setTextColor(Color.BLUE);
            holder.btnMarkTaken.setEnabled(true);
        }

        holder.btnMarkTaken.setOnClickListener(v -> takenListener.onClick(reminder));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onClick(reminder));
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public void updateList(List<Reminder> newList) {
        this.reminderList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicine, tvDate, tvTime, tvStatus;
        Button btnMarkTaken;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvMedicine = itemView.findViewById(R.id.tv_reminder_medicine);
            tvDate = itemView.findViewById(R.id.tv_reminder_date);
            tvTime = itemView.findViewById(R.id.tv_reminder_time);
            tvStatus = itemView.findViewById(R.id.tv_reminder_status);
            btnMarkTaken = itemView.findViewById(R.id.btn_mark_taken);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
