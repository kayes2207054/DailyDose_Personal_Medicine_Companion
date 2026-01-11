package com.example.dailydosepersonalmedicinecompanion.model;

import java.io.Serializable;

/**
 * GuardianPatientLink Model
 * Links guardians to patients for monitoring
 */
public class GuardianPatientLink implements Serializable {
    private int id;
    private int guardianId;
    private int patientId;
    private String guardianUsername;
    private String patientUsername;
    private String linkedAt;
    private boolean active;

    public GuardianPatientLink() {
    }

    public GuardianPatientLink(int guardianId, int patientId) {
        this.guardianId = guardianId;
        this.patientId = patientId;
        this.active = true;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(int guardianId) {
        this.guardianId = guardianId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getGuardianUsername() {
        return guardianUsername;
    }

    public void setGuardianUsername(String guardianUsername) {
        this.guardianUsername = guardianUsername;
    }

    public String getPatientUsername() {
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
    }

    public String getLinkedAt() {
        return linkedAt;
    }

    public void setLinkedAt(String linkedAt) {
        this.linkedAt = linkedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "GuardianPatientLink{" +
                "id=" + id +
                ", guardianUsername='" + guardianUsername + '\'' +
                ", patientUsername='" + patientUsername + '\'' +
                ", active=" + active +
                '}';
    }
}
