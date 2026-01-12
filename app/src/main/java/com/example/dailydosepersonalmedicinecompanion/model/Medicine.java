package com.example.dailydosepersonalmedicinecompanion.model;

import java.io.Serializable;

/**
 * Medicine Model
 * Represents a medicine with dosage, frequency and instructions
 */
public class Medicine implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String dosage;
    private String frequency;
    private String instructions;
    private int quantity;  // New field for medicine quantity/stock
    private String createdAt;
    private String updatedAt;

    public Medicine() {
        this.quantity = 0;  // Default quantity
    }

    public Medicine(String name, String dosage, String frequency, String instructions) {
        this();
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.instructions = instructions;
    }

    public Medicine(String name, String dosage, String frequency, String instructions, int quantity) {
        this();
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.instructions = instructions;
        this.quantity = quantity;
    }

    public Medicine(int id, String name, String dosage, String frequency, String instructions) {
        this();
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.instructions = instructions;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name + " (" + dosage + " - " + frequency + " times/day)";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Medicine medicine = (Medicine) obj;
        return id == medicine.id || (name != null && name.equals(medicine.name));
    }

    @Override
    public int hashCode() {
        return id != 0 ? Integer.hashCode(id) : (name != null ? name.hashCode() : 0);
    }
}
