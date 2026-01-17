package com.example.dailydosepersonalmedicinecompanion.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.controller.HistoryController;
import com.example.dailydosepersonalmedicinecompanion.controller.InventoryController;
import com.example.dailydosepersonalmedicinecompanion.controller.MedicineController;
import com.example.dailydosepersonalmedicinecompanion.controller.ReminderController;
import com.example.dailydosepersonalmedicinecompanion.controller.UserController;
import com.example.dailydosepersonalmedicinecompanion.fragment.DashboardFragment;
import com.example.dailydosepersonalmedicinecompanion.fragment.HistoryFragment;
import com.example.dailydosepersonalmedicinecompanion.fragment.InventoryFragment;
import com.example.dailydosepersonalmedicinecompanion.fragment.MedicineFragment;
import com.example.dailydosepersonalmedicinecompanion.fragment.ReminderFragment;
import com.example.dailydosepersonalmedicinecompanion.model.User;
import com.example.dailydosepersonalmedicinecompanion.service.ReminderService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * DashboardActivity
 * Main dashboard with bottom navigation
 */
public class DashboardActivity extends AppCompatActivity {
    private static final int REQUEST_NOTIFICATION_PERMISSION = 100;
    private static final int REQUEST_EXACT_ALARM_PERMISSION = 101;
    
    private MaterialToolbar toolbar;
    private TextView tvWelcome;
    private BottomNavigationView bottomNav;

    private MedicineController medicineController;
    private ReminderController reminderController;
    private HistoryController historyController;
    private InventoryController inventoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set up toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize controllers
        medicineController = new MedicineController(this);
        reminderController = new ReminderController(this);
        historyController = new HistoryController(this);
        inventoryController = new InventoryController(this);

        tvWelcome = findViewById(R.id.tv_welcome);
        bottomNav = findViewById(R.id.bottom_navigation);

        User currentUser = UserController.getCurrentUser();
        if (currentUser != null) {
            tvWelcome.setText("Welcome, " + currentUser.getFullName());
        }

        // Request necessary permissions for notifications and alarms
        requestNotificationPermissions();

        // Start Reminder Service (with try-catch for safety)
        try {
            startReminderService();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Note: Background reminder service could not start", Toast.LENGTH_SHORT).show();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                selectedFragment = new DashboardFragment();
            } else if (itemId == R.id.nav_medicines) {
                selectedFragment = new MedicineFragment();
            } else if (itemId == R.id.nav_reminders) {
                selectedFragment = new ReminderFragment();
            } else if (itemId == R.id.nav_inventory) {
                selectedFragment = new InventoryFragment();
            } else if (itemId == R.id.nav_history) {
                selectedFragment = new HistoryFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DashboardFragment())
                    .commit();
        }
    }

    /**
     * Start the background reminder service
     */
    private void startReminderService() {
        Intent serviceIntent = new Intent(this, ReminderService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_profile) {
            // Navigate to User Profile
            startActivity(new Intent(this, UserProfileActivity.class));
            return true;
        } else if (itemId == R.id.action_contact) {
            // Send email to support
            sendSupportEmail();
            return true;
        } else if (itemId == R.id.action_logout) {
            // Show logout confirmation
            showLogoutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendSupportEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:anonymous091119@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Daily Dose - Support Request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Support Team,\n\nI need assistance with:\n\n");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email via..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email app installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    // Clear current user
                    UserController userController = new UserController(this);
                    userController.logout();
                    
                    // Navigate to login
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Request notification and alarm permissions
     */
    private void requestNotificationPermissions() {
        // Request POST_NOTIFICATIONS permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            } else {
                checkExactAlarmPermission();
            }
        } else {
            checkExactAlarmPermission();
        }
    }

    /**
     * Check and request exact alarm permission
     */
    private void checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("This app needs permission to schedule exact alarms for medicine reminders. Please enable it in settings.")
                        .setPositiveButton("Open Settings", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        })
                        .setNegativeButton("Later", null)
                        .show();
            } else {
                checkNotificationPolicyAccess();
            }
        } else {
            checkNotificationPolicyAccess();
        }
    }

    /**
     * Check if app can bypass Do Not Disturb mode
     */
    private void checkNotificationPolicyAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null && !notificationManager.isNotificationPolicyAccessGranted()) {
                new AlertDialog.Builder(this)
                        .setTitle("Do Not Disturb Access")
                        .setMessage("Allow this app to override Do Not Disturb mode so medicine reminders always play sound?")
                        .setPositiveButton("Allow", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                            startActivity(intent);
                        })
                        .setNegativeButton("Skip", null)
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
                checkExactAlarmPermission();
            } else {
                Toast.makeText(this, "Notification permission denied. Reminders may not work properly.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
