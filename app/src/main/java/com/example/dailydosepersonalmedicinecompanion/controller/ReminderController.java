package com.example.dailydosepersonalmedicinecompanion.controller;

import android.content.Context;

import com.example.dailydosepersonalmedicinecompanion.database.DatabaseHelper;
import com.example.dailydosepersonalmedicinecompanion.model.Reminder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ReminderController
 * Handles all business logic for reminder operations
 */
public class ReminderController {
    private final DatabaseHelper dbHelper;

    public ReminderController(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public long addReminder(Reminder reminder) {
        return dbHelper.addReminder(reminder);
    }

    public boolean updateReminder(Reminder reminder) {
        return dbHelper.updateReminder(reminder);
    }

    public boolean deleteReminder(int reminderId) {
        return dbHelper.deleteReminder(reminderId);
    }

    public List<Reminder> getAllReminders() {
        return dbHelper.getAllReminders();
    }

    public List<Reminder> getPendingReminders() {
        return dbHelper.getPendingReminders();
    }

    public List<Reminder> getTodayReminders() {
        String today = DatabaseHelper.getCurrentDate();
        return getAllReminders().stream()
                .filter(r -> r.getDate().equals(today))
                .collect(Collectors.toList());
    }

    public int getPendingCount() {
        return getPendingReminders().size();
    }

    public boolean markReminderAsTaken(int reminderId) {
        List<Reminder> reminders = getAllReminders();
        for (Reminder r : reminders) {
            if (r.getId() == reminderId) {
                r.setTaken(true);
                r.setStatus("TAKEN");
                return updateReminder(r);
            }
        }
        return false;
    }

    public boolean markReminderAsMissed(int reminderId) {
        List<Reminder> reminders = getAllReminders();
        for (Reminder r : reminders) {
            if (r.getId() == reminderId) {
                r.setStatus("MISSED");
                return updateReminder(r);
            }
        }
        return false;
    }
}
