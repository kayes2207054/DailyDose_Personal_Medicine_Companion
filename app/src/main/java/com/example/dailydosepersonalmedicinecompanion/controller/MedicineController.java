package com.example.dailydosepersonalmedicinecompanion.controller;

import android.content.Context;

import com.example.dailydosepersonalmedicinecompanion.database.DatabaseHelper;
import com.example.dailydosepersonalmedicinecompanion.model.Medicine;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MedicineController
 * Handles all business logic for medicine operations
 */
public class MedicineController {
    private final DatabaseHelper dbHelper;

    public MedicineController(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public long addMedicine(Medicine medicine) {
        return dbHelper.addMedicine(medicine);
    }

    public boolean updateMedicine(Medicine medicine) {
        return dbHelper.updateMedicine(medicine);
    }

    public boolean deleteMedicine(int medicineId) {
        return dbHelper.deleteMedicine(medicineId);
    }

    public List<Medicine> getAllMedicines() {
        return dbHelper.getAllMedicines();
    }

    public Medicine getMedicineById(int id) {
        return dbHelper.getMedicineById(id);
    }

    public List<Medicine> searchMedicines(String query) {
        return getAllMedicines().stream()
                .filter(m -> m.getName().toLowerCase().contains(query.toLowerCase()) ||
                        m.getDosage().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public int getTotalMedicines() {
        return getAllMedicines().size();
    }

    public boolean medicineExists(String medicineName) {
        return getAllMedicines().stream()
                .anyMatch(m -> m.getName().equalsIgnoreCase(medicineName));
    }
}
