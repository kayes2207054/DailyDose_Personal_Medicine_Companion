package com.example.dailydosepersonalmedicinecompanion.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.example.dailydosepersonalmedicinecompanion.controller.ReminderController;
import com.example.dailydosepersonalmedicinecompanion.model.Reminder;
import com.example.dailydosepersonalmedicinecompanion.service.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Acquire wake lock to ensure the device wakes up
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
                "DailyDose:AlarmWakeLock"
        );
        wakeLock.acquire(10000); // Hold wake lock for 10 seconds

        try {
            int reminderId = intent.getIntExtra("reminderId", -1);
            String medicineName = intent.getStringExtra("medicineName");

            Log.d(TAG, "Alarm received for: " + medicineName + " (ID: " + reminderId + ")");

            if (reminderId == -1) {
                Log.e(TAG, "Invalid reminder ID");
                return;
            }

            ReminderController reminderController = new ReminderController(context);
            Reminder reminder = reminderController.getReminderById(reminderId);

            if (reminder == null || !"PENDING".equals(reminder.getStatus())) {
                Log.d(TAG, "Reminder not found or not pending");
                return;
            }

            NotificationHelper notificationHelper = new NotificationHelper(context);
            notificationHelper.showReminderNotification(reminder);
            
            Log.d(TAG, "Notification shown successfully");
        } finally {
            // Release wake lock
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
        }
    }
}
