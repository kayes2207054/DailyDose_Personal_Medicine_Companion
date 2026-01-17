package com.example.dailydosepersonalmedicinecompanion.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.dailydosepersonalmedicinecompanion.controller.HistoryController;
import com.example.dailydosepersonalmedicinecompanion.controller.ReminderController;
import com.example.dailydosepersonalmedicinecompanion.database.DatabaseHelper;
import com.example.dailydosepersonalmedicinecompanion.model.DoseHistory;
import com.example.dailydosepersonalmedicinecompanion.model.Reminder;
import com.example.dailydosepersonalmedicinecompanion.service.AlarmScheduler;
import com.example.dailydosepersonalmedicinecompanion.service.NotificationHelper;

import java.util.Calendar;
import java.util.List;

/**
 * ReminderActionReceiver
 * Handles actions from reminder notifications (TAKEN, SNOOZE, MISS)
 */
public class ReminderActionReceiver extends BroadcastReceiver {
    private static final String TAG = "ReminderActionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        int reminderId = intent.getIntExtra("reminderId", -1);
        String medicineName = intent.getStringExtra("medicineName");
        int medicineId = intent.getIntExtra("medicineId", -1);

        Log.d(TAG, "Action: " + action + ", ReminderId: " + reminderId);

        if (reminderId == -1 || action == null) {
            return;
        }

        ReminderController reminderController = new ReminderController(context);
        HistoryController historyController = new HistoryController(context);
        NotificationHelper notificationHelper = new NotificationHelper(context);

        switch (action) {
            case "TAKEN":
                handleTaken(context, reminderId, medicineId, medicineName, 
                           reminderController, historyController, notificationHelper);
                break;

            case "SNOOZE":
                handleSnooze(context, reminderId, reminderController, notificationHelper);
                break;

            case "MISS":
                handleMiss(context, reminderId, medicineId, medicineName,
                          reminderController, historyController, notificationHelper);
                break;
        }
    }

    /**
     * Handle TAKEN action
     */
    private void handleTaken(Context context, int reminderId, int medicineId, 
                           String medicineName, ReminderController reminderController,
                           HistoryController historyController, NotificationHelper notificationHelper) {
        
        // Add to dose history
        DoseHistory history = new DoseHistory();
        history.setMedicineId(medicineId);
        history.setReminderId(reminderId);
        history.setMedicineName(medicineName);
        history.setDate(DatabaseHelper.getCurrentDate());
        history.setTime(DatabaseHelper.getCurrentTime());
        history.setStatus("TAKEN");
        history.setNotes("Marked from notification");
        
        historyController.addHistory(history);
        
        // Delete the reminder (it's now in history)
        boolean success = reminderController.deleteReminder(reminderId);
        
        if (success) {
            // Cancel notification
            notificationHelper.cancelNotification(reminderId);
            
            // Show toast
            Toast.makeText(context, "✓ Marked as TAKEN: " + medicineName, 
                          Toast.LENGTH_SHORT).show();
            
            Log.d(TAG, "Reminder " + reminderId + " marked as TAKEN and removed from reminders");
        }
    }

    /**
     * Handle SNOOZE action (delay 5 minutes)
     */
    private void handleSnooze(Context context, int reminderId,
                             ReminderController reminderController,
                             NotificationHelper notificationHelper) {
        
        // Get reminder
        List<Reminder> reminders = reminderController.getAllReminders();
        Reminder reminder = null;
        for (Reminder r : reminders) {
            if (r.getId() == reminderId) {
                reminder = r;
                break;
            }
        }
        
        if (reminder != null) {
            // Cancel old alarm first
            AlarmScheduler alarmScheduler = new AlarmScheduler(context);
            alarmScheduler.cancelAlarm(reminderId);
            
            // Calculate new time (5 minutes from now)
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 5);
            
            String newTime = String.format("%02d:%02d", 
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE));
            
            // Update reminder time
            reminder.setTime(newTime);
            reminder.setNotes("Snoozed at " + DatabaseHelper.getCurrentTime());
            reminderController.updateReminder(reminder);
            
            // Schedule new alarm
            alarmScheduler.scheduleAlarm(reminder);
            
            // Cancel current notification
            notificationHelper.cancelNotification(reminderId);
            
            // Show toast
            Toast.makeText(context, "⏰ Snoozed for 5 minutes", 
                          Toast.LENGTH_SHORT).show();
            
            Log.d(TAG, "Reminder " + reminderId + " snoozed to " + newTime);
        }
    }

    /**
     * Handle MISS action
     */
    private void handleMiss(Context context, int reminderId, int medicineId,
                          String medicineName, ReminderController reminderController,
                          HistoryController historyController, NotificationHelper notificationHelper) {
        
        // Add to dose history
        DoseHistory history = new DoseHistory();
        history.setMedicineId(medicineId);
        history.setReminderId(reminderId);
        history.setMedicineName(medicineName);
        history.setDate(DatabaseHelper.getCurrentDate());
        history.setTime(DatabaseHelper.getCurrentTime());
        history.setStatus("MISSED");
        history.setNotes("Marked from notification");
        
        historyController.addHistory(history);
        
        // Delete the reminder (it's now in history)
        boolean success = reminderController.deleteReminder(reminderId);
        
        if (success) {
            // Cancel notification
            notificationHelper.cancelNotification(reminderId);
            
            // Show toast
            Toast.makeText(context, "✗ Marked as MISSED: " + medicineName,
                          Toast.LENGTH_SHORT).show();
            
            Log.d(TAG, "Reminder " + reminderId + " marked as MISSED and removed from reminders");
        }
    }
}
