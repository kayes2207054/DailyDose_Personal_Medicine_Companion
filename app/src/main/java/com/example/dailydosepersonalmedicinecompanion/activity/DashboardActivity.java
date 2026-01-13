package com.example.dailydosepersonalmedicinecompanion.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * DashboardActivity
 * Main dashboard with bottom navigation
 */
public class DashboardActivity extends AppCompatActivity {
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
}
