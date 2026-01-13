package com.example.dailydosepersonalmedicinecompanion.controller;

import android.content.Context;

import com.example.dailydosepersonalmedicinecompanion.database.DatabaseHelper;
import com.example.dailydosepersonalmedicinecompanion.model.User;

/**
 * UserController
 * Manages user authentication and user operations
 */
public class UserController {
    private final DatabaseHelper dbHelper;
    private static User currentUser;

    public UserController(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public long registerUser(String username, String password, String role, String fullName, String email) {
        // Check if username already exists
        if (dbHelper.getUserByUsername(username) != null) {
            return -1; // User already exists
        }

        String hashedPassword = DatabaseHelper.hashPassword(password);
        User user = new User(username, hashedPassword, role, fullName, email);
        return dbHelper.addUser(user);
    }

    public User login(String username, String password) {
        User user = dbHelper.authenticateUser(username, password);
        if (user != null) {
            currentUser = user;
        }
        return user;
    }

    public void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getUserByUsername(String username) {
        return dbHelper.getUserByUsername(username);
    }

    public boolean isFirstTimeUser() {
        // Check if database has no users yet
        return dbHelper.getUserByUsername("admin") == null;
    }

    /**
     * Update user profile information
     * @param user User object with updated information
     * @param currentPassword Current password (required if changing password)
     * @param newPassword New password (null if not changing)
     * @return true if update successful, false otherwise
     */
    public boolean updateUserProfile(User user, String currentPassword, String newPassword) {
        // If password change is requested, verify current password
        if (newPassword != null && !newPassword.isEmpty()) {
            if (currentPassword == null || currentPassword.isEmpty()) {
                return false;
            }
            
            // Verify current password
            User verifiedUser = dbHelper.authenticateUser(user.getUsername(), currentPassword);
            if (verifiedUser == null) {
                return false; // Current password is incorrect
            }
            
            // Hash new password and update user object
            String hashedPassword = DatabaseHelper.hashPassword(newPassword);
            user.setPasswordHash(hashedPassword);
        }
        
        // Update user in database
        boolean success = dbHelper.updateUser(user);
        
        // Update current user in session if successful
        if (success) {
            currentUser = user;
        }
        
        return success;
    }
}
