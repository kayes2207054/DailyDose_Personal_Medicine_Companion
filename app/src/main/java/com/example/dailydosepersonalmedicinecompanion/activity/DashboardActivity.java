package com.example.dailydosepersonalmedicinecompanion.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
}
