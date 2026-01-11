package com.example.dailydosepersonalmedicinecompanion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.model.Medicine;

import java.util.List;

/**
 * MedicineAdapter
 * Adapter for medicine list RecyclerView
 */
public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private List<Medicine> medicineList;
    private final OnMedicineClickListener editListener;
    private final OnMedicineClickListener deleteListener;

    public interface OnMedicineClickListener {
        void onClick(Medicine medicine);
    }

    public MedicineAdapter(List<Medicine> medicineList, OnMedicineClickListener editListener, OnMedicineClickListener deleteListener) {
        this.medicineList = medicineList;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.tvName.setText(medicine.getName());
        holder.tvDosage.setText(medicine.getDosage());
        holder.tvFrequency.setText(medicine.getFrequency());
        holder.tvInstructions.setText(medicine.getInstructions());

        holder.btnEdit.setOnClickListener(v -> editListener.onClick(medicine));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onClick(medicine));
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public void updateList(List<Medicine> newList) {
        this.medicineList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDosage, tvFrequency, tvInstructions;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_medicine_name);
            tvDosage = itemView.findViewById(R.id.tv_dosage);
            tvFrequency = itemView.findViewById(R.id.tv_frequency);
            tvInstructions = itemView.findViewById(R.id.tv_instructions);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
