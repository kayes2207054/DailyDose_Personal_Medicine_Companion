package com.example.dailydosepersonalmedicinecompanion.model;

import java.io.Serializable;

/**
 * Reminder Model
 * Represents a medicine reminder with date, time and status
 */
public class Reminder implements Serializable {
    private int id;
    private int medicineId;
    private String medicineName;
    private String date;
    private String time;
    private String reminderType;
    private boolean taken;
    private String status; // PENDING, TAKEN, MISSED, SNOOZED
    private String notes;
    private String createdAt;

    public Reminder() {
    }

    public Reminder(int medicineId, String medicineName, String date, String time, String reminderType) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.date = date;
        this.time = time;
        this.reminderType = reminderType;
        this.taken = false;
        this.status = "PENDING";
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", medicineName='" + medicineName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
