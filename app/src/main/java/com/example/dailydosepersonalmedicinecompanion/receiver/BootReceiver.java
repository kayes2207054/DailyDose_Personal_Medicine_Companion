package com.example.dailydosepersonalmedicinecompanion.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.dailydosepersonalmedicinecompanion.controller.ReminderController;
import com.example.dailydosepersonalmedicinecompanion.model.Reminder;
import com.example.dailydosepersonalmedicinecompanion.service.AlarmScheduler;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "Device rebooted, rescheduling alarms...");

            ReminderController reminderController = new ReminderController(context);
            AlarmScheduler alarmScheduler = new AlarmScheduler(context);

            List<Reminder> pendingReminders = reminderController.getPendingReminders();

            for (Reminder reminder : pendingReminders) {
                alarmScheduler.scheduleAlarm(reminder);
            }

            Log.d(TAG, "Rescheduled " + pendingReminders.size() + " alarms");
        }
    }
}
