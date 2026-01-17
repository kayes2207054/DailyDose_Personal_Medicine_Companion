package com.example.dailydosepersonalmedicinecompanion.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.dailydosepersonalmedicinecompanion.model.Reminder;
import com.example.dailydosepersonalmedicinecompanion.receiver.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmScheduler {
    private static final String TAG = "AlarmScheduler";
    private Context context;
    private AlarmManager alarmManager;

    public AlarmScheduler(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void scheduleAlarm(Reminder reminder) {
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String dateTimeString = reminder.getDate() + " " + reminder.getTime();
            Date reminderDateTime = dateTimeFormat.parse(dateTimeString);

            if (reminderDateTime == null || reminderDateTime.getTime() <= System.currentTimeMillis()) {
                return;
            }

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("reminderId", reminder.getId());
            intent.putExtra("medicineName", reminder.getMedicineName());
            intent.putExtra("medicineId", reminder.getMedicineId());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, reminder.getId(), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP, reminderDateTime.getTime(), pendingIntent
                    );
                } else {
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP, reminderDateTime.getTime(), pendingIntent
                    );
                }
                Log.d(TAG, "Alarm scheduled for: " + reminder.getMedicineName());
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date/time", e);
        }
    }

    public void cancelAlarm(int reminderId) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, reminderId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
