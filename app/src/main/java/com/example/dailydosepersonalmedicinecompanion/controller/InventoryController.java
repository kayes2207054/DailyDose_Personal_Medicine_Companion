package com.example.dailydosepersonalmedicinecompanion.controller;

import android.content.Context;

import com.example.dailydosepersonalmedicinecompanion.database.DatabaseHelper;
import com.example.dailydosepersonalmedicinecompanion.model.Inventory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * InventoryController
 * Handles inventory management and stock tracking
 */
public class InventoryController {
    private final DatabaseHelper dbHelper;

    public InventoryController(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public long addInventory(Inventory inventory) {
        return dbHelper.addInventory(inventory);
    }

    public boolean updateInventory(Inventory inventory) {
        return dbHelper.updateInventory(inventory);
    }

    public List<Inventory> getAllInventory() {
        return dbHelper.getAllInventory();
    }

    public List<Inventory> getLowStockItems() {
        return getAllInventory().stream()
                .filter(Inventory::isLowStock)
                .collect(Collectors.toList());
    }

    public int getLowStockCount() {
        return getLowStockItems().size();
    }

    public boolean refillStock(int inventoryId, int quantity) {
        List<Inventory> items = getAllInventory();
        for (Inventory item : items) {
            if (item.getId() == inventoryId) {
                item.setStockLevel(item.getStockLevel() + quantity);
                item.setLastRefillDate(DatabaseHelper.getCurrentDate());
                return updateInventory(item);
            }
        }
        return false;
    }

    public boolean decrementStock(int medicineId) {
        List<Inventory> items = getAllInventory();
        for (Inventory item : items) {
            if (item.getMedicineId() == medicineId && item.getStockLevel() > 0) {
                item.setStockLevel(item.getStockLevel() - 1);
                return updateInventory(item);
            }
        }
        return false;
    }
}
