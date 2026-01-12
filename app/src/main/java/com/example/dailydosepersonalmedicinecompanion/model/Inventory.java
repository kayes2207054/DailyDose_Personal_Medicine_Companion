package com.example.dailydosepersonalmedicinecompanion.model;

import java.io.Serializable;

/**
 * Inventory Model
 * Tracks medicine stock levels with advanced features
 */
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int medicineId;
    private String medicineName;
    private int stockLevel;  // Also called quantity
    private int lowStockThreshold;
    private int dailyUsage;  // Pills consumed per day (for refill estimation)
    private String lastRefillDate;
    private String estimatedRefillDate;  // Calculated based on daily usage
    private String notes;
    private String createdAt;
    private String updatedAt;

    public Inventory() {
        this.lowStockThreshold = 10;  // Default threshold
        this.dailyUsage = 1;  // Default daily usage
    }

    public Inventory(int medicineId, String medicineName, int stockLevel, int lowStockThreshold) {
        this();
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.stockLevel = stockLevel;
        this.lowStockThreshold = lowStockThreshold;
    }

    public Inventory(int medicineId, String medicineName, int stockLevel, int lowStockThreshold, int dailyUsage) {
        this();
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.stockLevel = stockLevel;
        this.lowStockThreshold = lowStockThreshold;
        this.dailyUsage = dailyUsage;
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

    public String getEstimatedRefillDate() {
        return estimatedRefillDate;
    }

    public void setEstimatedRefillDate(String estimatedRefillDate) {
        this.estimatedRefillDate = estimatedRefillDate;
    }

    public int getDailyUsage() {
        return dailyUsage;
    }

    public void setDailyUsage(int dailyUsage) {
        this.dailyUsage = dailyUsage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Decrease stock level when a dose is taken
     */
    public void decreaseQuantity(int amount) {
        this.stockLevel = Math.max(0, this.stockLevel - amount);
    }

    public boolean isLowStock() {
        return stockLevel <= lowStockThreshold;
    }

    @Override
    public String toString() {
        return medicineName + " - " + stockLevel + " pills (Refill: " + estimatedRefillDate + ")";
    }
}
