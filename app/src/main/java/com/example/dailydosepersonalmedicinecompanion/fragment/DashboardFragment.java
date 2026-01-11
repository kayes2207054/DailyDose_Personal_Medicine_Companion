package com.example.dailydosepersonalmedicinecompanion.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.controller.HistoryController;
import com.example.dailydosepersonalmedicinecompanion.controller.InventoryController;
import com.example.dailydosepersonalmedicinecompanion.controller.MedicineController;
import com.example.dailydosepersonalmedicinecompanion.controller.ReminderController;

/**
 * DashboardFragment
 * Shows overview statistics and recent activity
 */
public class DashboardFragment extends Fragment {
    private TextView tvTotalMedicines, tvPendingReminders, tvDosesTaken, tvDosesMissed, tvAdherenceRate, tvLowStock;
    
    private MedicineController medicineController;
    private ReminderController reminderController;
    private HistoryController historyController;
    private InventoryController inventoryController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize controllers
        medicineController = new MedicineController(requireContext());
        reminderController = new ReminderController(requireContext());
        historyController = new HistoryController(requireContext());
        inventoryController = new InventoryController(requireContext());

        // Initialize views
        tvTotalMedicines = view.findViewById(R.id.tv_total_medicines);
        tvPendingReminders = view.findViewById(R.id.tv_pending_reminders);
        tvDosesTaken = view.findViewById(R.id.tv_doses_taken);
        tvDosesMissed = view.findViewById(R.id.tv_doses_missed);
        tvAdherenceRate = view.findViewById(R.id.tv_adherence_rate);
        tvLowStock = view.findViewById(R.id.tv_low_stock);

        loadStatistics();

        return view;
    }

    private void loadStatistics() {
        tvTotalMedicines.setText(String.valueOf(medicineController.getTotalMedicines()));
        tvPendingReminders.setText(String.valueOf(reminderController.getPendingCount()));
        tvDosesTaken.setText(String.valueOf(historyController.getTotalDosesTaken()));
        tvDosesMissed.setText(String.valueOf(historyController.getTotalDosesMissed()));
        tvAdherenceRate.setText(String.format("%.1f%%", historyController.getAdherenceRate()));
        tvLowStock.setText(String.valueOf(inventoryController.getLowStockCount()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatistics();
    }
}
