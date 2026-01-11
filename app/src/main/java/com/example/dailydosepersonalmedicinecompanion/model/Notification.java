package com.example.dailydosepersonalmedicinecompanion.model;

import java.io.Serializable;

/**
 * Notification Model
 * Represents system notifications
 */
public class Notification implements Serializable {
    private int id;
    private int userId;
    private String type; // REMINDER, LOW_STOCK, MISSED_DOSE, etc.
    private String title;
    private String message;
    private String createdAt;
    private boolean isRead;
    private String relatedEntityType; // MEDICINE, REMINDER, etc.
    private int relatedEntityId;

    public Notification() {
    }

    public Notification(int userId, String type, String title, String message) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.isRead = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getRelatedEntityType() {
        return relatedEntityType;
    }

    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }

    public int getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(int relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
