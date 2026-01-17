package com.example.dailydosepersonalmedicinecompanion.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.activity.DashboardActivity;
import com.example.dailydosepersonalmedicinecompanion.controller.ReminderController;
import com.example.dailydosepersonalmedicinecompanion.model.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ReminderService
 * Background service that monitors reminders 24/7
 * Checks every 30 seconds for pending reminders
 * Triggers notifications when reminder time is reached
 */
public class ReminderService extends Service {
    private static final String TAG = "ReminderService";
    private static final String CHANNEL_ID = "DailyDoseReminders";
    private static final int CHECK_INTERVAL = 30000; // 30 seconds

    private Handler handler;
    private Runnable reminderChecker;
    private ReminderController reminderController;
    private NotificationHelper notificationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "ReminderService created");
        
        reminderController = new ReminderController(this);
        notificationHelper = new NotificationHelper(this);
        handler = new Handler();
        
        // Create notification channel
        createNotificationChannel();
        
        // Start as foreground service (with try-catch for compatibility)
        try {
            startForeground(1, createForegroundNotification());
        } catch (Exception e) {
            Log.e(TAG, "Could not start foreground service", e);
        }
        
        // Initialize reminder checker
        reminderChecker = new Runnable() {
            @Override
            public void run() {
                checkReminders();
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "ReminderService started");
        
        // Start checking reminders
        handler.post(reminderChecker);
        
        // Restart service if killed
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ReminderService destroyed");
        
        // Stop checker
        if (handler != null && reminderChecker != null) {
            handler.removeCallbacks(reminderChecker);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Check all pending reminders and trigger notifications if time matches
     */
    private void checkReminders() {
        try {
            List<Reminder> pendingReminders = reminderController.getPendingReminders();
            Log.d(TAG, "Checking " + pendingReminders.size() + " pending reminders");
            
            Calendar now = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            
            String currentDate = dateFormat.format(now.getTime());
            String currentTime = timeFormat.format(now.getTime());
            
            for (Reminder reminder : pendingReminders) {
                if (shouldTriggerReminder(reminder, currentDate, currentTime)) {
                    Log.d(TAG, "Triggering reminder: " + reminder.getMedicineName());
                    notificationHelper.showReminderNotification(reminder);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking reminders", e);
        }
    }

    /**
     * Check if reminder should be triggered now
     */
    private boolean shouldTriggerReminder(Reminder reminder, String currentDate, String currentTime) {
        try {
            // Check if date matches
            if (!reminder.getDate().equals(currentDate)) {
                return false;
            }
            
            // Check if time matches (within 1 minute window)
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date reminderTime = timeFormat.parse(reminder.getTime());
            Date nowTime = timeFormat.parse(currentTime);
            
            if (reminderTime == null || nowTime == null) {
                return false;
            }
            
            // Calculate time difference in minutes
            long diffMs = Math.abs(nowTime.getTime() - reminderTime.getTime());
            long diffMinutes = diffMs / (60 * 1000);
            
            // Trigger if within 1 minute
            return diffMinutes <= 1;
            
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing time", e);
            return false;
        }
    }

    /**
     * Create notification channel for Android O+
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Medicine Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for medicine reminders");
            channel.enableVibration(true);
            channel.setShowBadge(true);
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Create foreground notification to keep service running
     */
    private Notification createForegroundNotification() {
        Intent notificationIntent = new Intent(this, DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("DailyDose Running")
                .setContentText("Monitoring your medicine reminders")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }
}
