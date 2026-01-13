package com.example.dailydosepersonalmedicinecompanion.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.adapter.InventoryAdapter;
import com.example.dailydosepersonalmedicinecompanion.controller.InventoryController;
import com.example.dailydosepersonalmedicinecompanion.controller.MedicineController;
import com.example.dailydosepersonalmedicinecompanion.database.DatabaseHelper;
import com.example.dailydosepersonalmedicinecompanion.model.Inventory;
import com.example.dailydosepersonalmedicinecompanion.model.Medicine;

import java.util.ArrayList;
import java.util.List;

/**
 * InventoryFragment
 * Shows medicine stock levels with add/refresh functionality
 */
public class InventoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private InventoryController inventoryController;
    private MedicineController medicineController;
    private Button btnAddInventory, btnRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        inventoryController = new InventoryController(requireContext());
        medicineController = new MedicineController(requireContext());

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_inventory);
        btnAddInventory = view.findViewById(R.id.btn_add_inventory);
        btnRefresh = view.findViewById(R.id.btn_refresh_inventory);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        adapter = new InventoryAdapter(inventoryController.getAllInventory());
        recyclerView.setAdapter(adapter);

        // Add inventory button
        btnAddInventory.setOnClickListener(v -> showAddInventoryDialog());

        // Refresh button
        btnRefresh.setOnClickListener(v -> refreshInventory());

        return view;
    }

    /**
     * Show dialog to add new inventory item
     */
    private void showAddInventoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_inventory, null);
        builder.setView(dialogView);

        // Get views from dialog
        Spinner spinnerMedicine = dialogView.findViewById(R.id.spinner_medicine);
        EditText etStockLevel = dialogView.findViewById(R.id.et_stock_level);
        EditText etLowStockThreshold = dialogView.findViewById(R.id.et_low_stock_threshold);
        EditText etDailyUsage = dialogView.findViewById(R.id.et_daily_usage);
        EditText etNotes = dialogView.findViewById(R.id.et_notes);
        Button btnSave = dialogView.findViewById(R.id.btn_save);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        // Load medicines into spinner
        List<Medicine> medicines = medicineController.getAllMedicines();
        List<String> medicineNames = new ArrayList<>();
        for (Medicine medicine : medicines) {
            medicineNames.add(medicine.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                medicineNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicine.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        // Save button
        btnSave.setOnClickListener(v -> {
            String stockLevelStr = etStockLevel.getText().toString().trim();
            String thresholdStr = etLowStockThreshold.getText().toString().trim();
            String dailyUsageStr = etDailyUsage.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();

            if (spinnerMedicine.getSelectedItemPosition() == -1) {
                Toast.makeText(requireContext(), "Please select a medicine", Toast.LENGTH_SHORT).show();
                return;
            }

            if (stockLevelStr.isEmpty()) {
                etStockLevel.setError("Required");
                return;
            }

            int selectedPosition = spinnerMedicine.getSelectedItemPosition();
            Medicine selectedMedicine = medicines.get(selectedPosition);

            int stockLevel = Integer.parseInt(stockLevelStr);
            int threshold = thresholdStr.isEmpty() ? 10 : Integer.parseInt(thresholdStr);
            int dailyUsage = dailyUsageStr.isEmpty() ? 1 : Integer.parseInt(dailyUsageStr);

            // Create inventory item
            Inventory inventory = new Inventory();
            inventory.setMedicineId(selectedMedicine.getId());
            inventory.setMedicineName(selectedMedicine.getName());
            inventory.setStockLevel(stockLevel);
            inventory.setLowStockThreshold(threshold);
            inventory.setDailyUsage(dailyUsage);
            inventory.setLastRefillDate(DatabaseHelper.getCurrentDate());
            inventory.setNotes(notes);

            // Calculate estimated refill date
            if (dailyUsage > 0 && stockLevel > 0) {
                int daysRemaining = stockLevel / dailyUsage;
                inventory.setEstimatedRefillDate("In " + daysRemaining + " days");
            }

            // Save to database
            long id = inventoryController.addInventory(inventory);
            if (id > 0) {
                Toast.makeText(requireContext(), "✓ Inventory added successfully", Toast.LENGTH_SHORT).show();
                refreshInventory();
                dialog.dismiss();
            } else {
                Toast.makeText(requireContext(), "✗ Failed to add inventory", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    /**
     * Refresh inventory list
     */
    private void refreshInventory() {
        adapter.updateList(inventoryController.getAllInventory());
        Toast.makeText(requireContext(), "Inventory refreshed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshInventory();
    }
}
