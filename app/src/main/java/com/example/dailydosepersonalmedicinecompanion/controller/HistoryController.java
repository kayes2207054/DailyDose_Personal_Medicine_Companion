package com.example.dailydosepersonalmedicinecompanion.controller;

import android.content.Context;

import com.example.dailydosepersonalmedicinecompanion.database.DatabaseHelper;
import com.example.dailydosepersonalmedicinecompanion.model.DoseHistory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * HistoryController
 * Manages dose history tracking and adherence reporting
 */
public class HistoryController {
    private final DatabaseHelper dbHelper;

    public HistoryController(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public long addHistory(DoseHistory history) {
        return dbHelper.addDoseHistory(history);
    }

    public List<DoseHistory> getAllHistory() {
        return dbHelper.getAllDoseHistory();
    }

    public List<DoseHistory> getTodayHistory() {
        String today = DatabaseHelper.getCurrentDate();
        return getAllHistory().stream()
                .filter(h -> h.getDate().equals(today))
                .collect(Collectors.toList());
    }

    public List<DoseHistory> getHistoryByDate(String date) {
        return getAllHistory().stream()
                .filter(h -> h.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<DoseHistory> getTakenHistory() {
        return getAllHistory().stream()
                .filter(h -> h.getStatus().equals("TAKEN"))
                .collect(Collectors.toList());
    }

    public List<DoseHistory> getMissedHistory() {
        return getAllHistory().stream()
                .filter(h -> h.getStatus().equals("MISSED"))
                .collect(Collectors.toList());
    }

    public int getTotalDosesTaken() {
        return getTakenHistory().size();
    }

    public int getTotalDosesMissed() {
        return getMissedHistory().size();
    }
    
    public int getTodayDosesTaken() {
        return (int) getTodayHistory().stream()
                .filter(h -> h.getStatus().equals("TAKEN"))
                .count();
    }
    
    public int getTodayDosesMissed() {
        return (int) getTodayHistory().stream()
                .filter(h -> h.getStatus().equals("MISSED"))
                .count();
    }
    
    public int getTodayTotalDoses() {
        return getTodayHistory().size();
    }

    public double getAdherenceRate() {
        int total = getAllHistory().size();
        if (total == 0) return 0.0;
        int taken = getTotalDosesTaken();
        return (taken * 100.0) / total;
    }
    
    public int getActiveDays() {
        // Get unique dates from history
        return (int) getAllHistory().stream()
                .map(DoseHistory::getDate)
                .distinct()
                .count();
    }

    public List<DoseHistory> getRecentHistory(int days) {
        // Get history for the last N days
        return getAllHistory().stream()
                .limit(days * 5) // Approximate: 5 doses per day
                .collect(Collectors.toList());
    }
}
