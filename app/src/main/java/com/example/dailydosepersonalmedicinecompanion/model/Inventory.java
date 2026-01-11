package com.example.dailydosepersonalmedicinecompanion.model;

import java.io.Serializable;

/**
 * Inventory Model
 * Tracks medicine stock levels
 */
public class Inventory implements Serializable {
    private int id;
    private int medicineId;
    private String medicineName;
    private int stockLevel;
    private int lowStockThreshold;
    private String lastRefillDate;
    private String nextRefillDate;
    private String notes;

    public Inventory() {
    }

    public Inventory(int medicineId, String medicineName, int stockLevel, int lowStockThreshold) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.stockLevel = stockLevel;
        this.lowStockThreshold = lowStockThreshold;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public String getLastRefillDate() {
        return lastRefillDate;
    }

    public void setLastRefillDate(String lastRefillDate) {
        this.lastRefillDate = lastRefillDate;
    }

    public String getNextRefillDate() {
        return nextRefillDate;
    }

    public void setNextRefillDate(String nextRefillDate) {
        this.nextRefillDate = nextRefillDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isLowStock() {
        return stockLevel <= lowStockThreshold;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", medicineName='" + medicineName + '\'' +
                ", stockLevel=" + stockLevel +
                ", lowStockThreshold=" + lowStockThreshold +
                '}';
    }
}
