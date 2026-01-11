package com.example.dailydosepersonalmedicinecompanion.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.adapter.MedicineAdapter;
import com.example.dailydosepersonalmedicinecompanion.controller.MedicineController;
import com.example.dailydosepersonalmedicinecompanion.model.Medicine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * MedicineFragment
 * Manages medicine list with add, edit, delete operations
 */
public class MedicineFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private MedicineAdapter adapter;
    private MedicineController medicineController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);

        medicineController = new MedicineController(requireContext());

        recyclerView = view.findViewById(R.id.recycler_medicines);
        fabAdd = view.findViewById(R.id.fab_add_medicine);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MedicineAdapter(medicineController.getAllMedicines(), 
            this::showEditDialog, this::deleteMedicine);
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddDialog());

        return view;
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_medicine, null);
        EditText etName = dialogView.findViewById(R.id.et_medicine_name);
        EditText etDosage = dialogView.findViewById(R.id.et_dosage);
        EditText etFrequency = dialogView.findViewById(R.id.et_frequency);
        EditText etInstructions = dialogView.findViewById(R.id.et_instructions);

        new AlertDialog.Builder(requireContext())
                .setTitle("Add Medicine")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String dosage = etDosage.getText().toString().trim();
                    String frequency = etFrequency.getText().toString().trim();
                    String instructions = etInstructions.getText().toString().trim();

                    if (!name.isEmpty() && !dosage.isEmpty() && !frequency.isEmpty()) {
                        Medicine medicine = new Medicine(name, dosage, frequency, instructions);
                        long result = medicineController.addMedicine(medicine);
                        if (result > 0) {
                            Toast.makeText(requireContext(), "Medicine added", Toast.LENGTH_SHORT).show();
                            refreshList();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditDialog(Medicine medicine) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_medicine, null);
        EditText etName = dialogView.findViewById(R.id.et_medicine_name);
        EditText etDosage = dialogView.findViewById(R.id.et_dosage);
        EditText etFrequency = dialogView.findViewById(R.id.et_frequency);
        EditText etInstructions = dialogView.findViewById(R.id.et_instructions);

        etName.setText(medicine.getName());
        etDosage.setText(medicine.getDosage());
        etFrequency.setText(medicine.getFrequency());
        etInstructions.setText(medicine.getInstructions());

        new AlertDialog.Builder(requireContext())
                .setTitle("Edit Medicine")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    medicine.setName(etName.getText().toString().trim());
                    medicine.setDosage(etDosage.getText().toString().trim());
                    medicine.setFrequency(etFrequency.getText().toString().trim());
                    medicine.setInstructions(etInstructions.getText().toString().trim());

                    if (medicineController.updateMedicine(medicine)) {
                        Toast.makeText(requireContext(), "Medicine updated", Toast.LENGTH_SHORT).show();
                        refreshList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteMedicine(Medicine medicine) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Medicine")
                .setMessage("Are you sure you want to delete " + medicine.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (medicineController.deleteMedicine(medicine.getId())) {
                        Toast.makeText(requireContext(), "Medicine deleted", Toast.LENGTH_SHORT).show();
                        refreshList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshList() {
        adapter.updateList(medicineController.getAllMedicines());
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
