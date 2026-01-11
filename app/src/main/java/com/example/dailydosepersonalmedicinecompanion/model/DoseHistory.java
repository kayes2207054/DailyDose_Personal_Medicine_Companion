package com.example.dailydosepersonalmedicinecompanion.model;

import java.io.Serializable;

/**
 * DoseHistory Model
 * Records each dose taken, missed or snoozed
 */
public class DoseHistory implements Serializable {
    private int id;
    private int medicineId;
    private int reminderId;
    private String medicineName;
    private String date;
    private String time;
    private String status; // TAKEN, MISSED, SNOOZED
    private String recordedAt;
    private String notes;

    public DoseHistory() {
    }

    public DoseHistory(int medicineId, String medicineName, String date, String time, String status) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public DoseHistory(int medicineId, int reminderId, String medicineName, String date, String time, String status, String notes) {
        this.medicineId = medicineId;
        this.reminderId = reminderId;
        this.medicineName = medicineName;
        this.date = date;
        this.time = time;
        this.status = status;
        this.notes = notes;
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

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "DoseHistory{" +
                "id=" + id +
                ", medicineName='" + medicineName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
