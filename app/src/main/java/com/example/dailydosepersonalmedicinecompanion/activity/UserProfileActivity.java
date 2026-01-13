package com.example.dailydosepersonalmedicinecompanion.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.controller.UserController;
import com.example.dailydosepersonalmedicinecompanion.model.User;

/**
 * UserProfileActivity
 * Allows users to view and update their profile information
 */
public class UserProfileActivity extends AppCompatActivity {
    private EditText etFullName, etEmail, etUsername, etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnUpdateProfile, btnCancel;
    private UserController userController;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("User Profile");
        }

        userController = new UserController(this);
        currentUser = UserController.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etUsername = findViewById(R.id.et_username);
        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnUpdateProfile = findViewById(R.id.btn_update_profile);
        btnCancel = findViewById(R.id.btn_cancel);

        // Load current user data
        loadUserData();

        btnUpdateProfile.setOnClickListener(v -> updateProfile());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadUserData() {
        etFullName.setText(currentUser.getFullName());
        etEmail.setText(currentUser.getEmail());
        etUsername.setText(currentUser.getUsername());
        etUsername.setEnabled(false); // Username cannot be changed
    }

    private void updateProfile() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            Toast.makeText(this, "Full name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update basic info
        currentUser.setFullName(fullName);
        currentUser.setEmail(email);

        // If user wants to change password
        if (!newPassword.isEmpty()) {
            if (currentPassword.isEmpty()) {
                Toast.makeText(this, "Please enter current password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 4) {
                Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update with password change
            boolean success = userController.updateUserProfile(currentUser, currentPassword, newPassword);
            if (success) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                // Clear password fields
                etCurrentPassword.setText("");
                etNewPassword.setText("");
                etConfirmPassword.setText("");
            } else {
                Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update without password change
            boolean success = userController.updateUserProfile(currentUser, null, null);
            if (success) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
