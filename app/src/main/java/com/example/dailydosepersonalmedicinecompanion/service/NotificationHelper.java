package com.example.dailydosepersonalmedicinecompanion.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.activity.DashboardActivity;
import com.example.dailydosepersonalmedicinecompanion.model.Reminder;
import com.example.dailydosepersonalmedicinecompanion.receiver.ReminderActionReceiver;

/**
 * NotificationHelper
 * Handles creation and display of medicine reminder notifications
 * with interactive actions (TAKEN, SNOOZE, MISS)
 */
public class NotificationHelper {
    private static final String CHANNEL_ID = "DailyDoseReminders";
    private static final String CHANNEL_NAME = "Medicine Reminders";
    private Context context;
    private NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    /**
     * Show interactive notification for a reminder
     */
    public void showReminderNotification(Reminder reminder) {
        // Main notification intent (opens app)
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        // Action buttons
        PendingIntent takenIntent = createActionIntent(reminder, "TAKEN");
        PendingIntent snoozeIntent = createActionIntent(reminder, "SNOOZE");
        PendingIntent missIntent = createActionIntent(reminder, "MISS");

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("💊 Medicine Reminder")
                .setContentText("Time to take: " + reminder.getMedicineName())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Time to take: " + reminder.getMedicineName() + "\n" +
                                "Time: " + reminder.getTime() + "\n" +
                                "Tap an action below"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setSound(getNotificationSound())
                .setVibrate(new long[]{0, 500, 250, 500})
                // Action buttons
                .addAction(R.drawable.ic_check, "✓ TAKEN", takenIntent)
                .addAction(R.drawable.ic_snooze, "⏰ SNOOZE", snoozeIntent)
                .addAction(R.drawable.ic_close, "✗ MISS", missIntent);

        // Show notification
        notificationManager.notify(reminder.getId(), builder.build());
    }

    /**
     * Create PendingIntent for action buttons
     */
    private PendingIntent createActionIntent(Reminder reminder, String action) {
        Intent intent = new Intent(context, ReminderActionReceiver.class);
        intent.setAction("com.example.dailydose.REMINDER_ACTION");
        intent.putExtra("reminderId", reminder.getId());
        intent.putExtra("action", action);
        intent.putExtra("medicineName", reminder.getMedicineName());
        intent.putExtra("medicineId", reminder.getMedicineId());

        int requestCode = reminder.getId() * 10 + getActionCode(action);
        return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    /**
     * Get unique code for each action
     */
    private int getActionCode(String action) {
        switch (action) {
            case "TAKEN": return 1;
            case "SNOOZE": return 2;
            case "MISS": return 3;
            default: return 0;
        }
    }

    /**
     * Get notification sound URI
     */
    private Uri getNotificationSound() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }

    /**
     * Create notification channel for Android O+
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for medicine reminders");
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Cancel a notification
     */
    public void cancelNotification(int reminderId) {
        if (notificationManager != null) {
            notificationManager.cancel(reminderId);
        }
    }

    /**
     * Show simple notification (no actions)
     */
    public void showSimpleNotification(String title, String message) {
        Intent intent = new Intent(context, DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
