package com.example.dailydosepersonalmedicinecompanion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dailydosepersonalmedicinecompanion.model.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * DatabaseHelper
 * Handles all SQLite database operations for DailyDose Android application
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "daily_dose.db";
    private static final int DATABASE_VERSION = 3;  // Incremented for low_stock_threshold fix

    // Table names
    private static final String TABLE_MEDICINES = "medicines";
    private static final String TABLE_REMINDERS = "reminders";
    private static final String TABLE_DOSE_HISTORY = "dose_history";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_INVENTORY = "inventory";
    private static final String TABLE_GUARDIAN_PATIENT_LINKS = "guardian_patient_links";
    private static final String TABLE_NOTIFICATIONS = "notifications";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Medicines table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MEDICINES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE NOT NULL," +
                "dosage TEXT NOT NULL," +
                "frequency TEXT NOT NULL," +
                "instructions TEXT," +
                "quantity INTEGER DEFAULT 0," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        // Create Reminders table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_REMINDERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "medicine_id INTEGER NOT NULL," +
                "medicine_name TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "time TEXT NOT NULL," +
                "reminder_type TEXT NOT NULL," +
                "taken BOOLEAN DEFAULT 0," +
                "status TEXT DEFAULT 'PENDING'," +
                "notes TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(medicine_id) REFERENCES " + TABLE_MEDICINES + "(id) ON DELETE CASCADE)");

        // Create Dose History table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DOSE_HISTORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "medicine_id INTEGER NOT NULL," +
                "reminder_id INTEGER," +
                "medicine_name TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "time TEXT," +
                "status TEXT NOT NULL," +
                "recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "notes TEXT," +
                "FOREIGN KEY(medicine_id) REFERENCES " + TABLE_MEDICINES + "(id) ON DELETE CASCADE," +
                "FOREIGN KEY(reminder_id) REFERENCES " + TABLE_REMINDERS + "(id) ON DELETE SET NULL)");

        // Create Users table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password_hash TEXT NOT NULL," +
                "role TEXT NOT NULL," +
                "full_name TEXT," +
                "email TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "active BOOLEAN DEFAULT 1)");

        // Create Inventory table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_INVENTORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "medicine_id INTEGER NOT NULL," +
                "medicine_name TEXT NOT NULL," +
                "stock_level INTEGER DEFAULT 0," +
                "low_stock_threshold INTEGER DEFAULT 10," +
                "daily_usage INTEGER DEFAULT 1," +
                "last_refill_date TEXT," +
                "estimated_refill_date TEXT," +
                "notes TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(medicine_id) REFERENCES " + TABLE_MEDICINES + "(id) ON DELETE CASCADE)");

        // Create Guardian-Patient Links table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GUARDIAN_PATIENT_LINKS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "guardian_id INTEGER NOT NULL," +
                "patient_id INTEGER NOT NULL," +
                "linked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "active BOOLEAN DEFAULT 1," +
                "FOREIGN KEY(guardian_id) REFERENCES " + TABLE_USERS + "(id) ON DELETE CASCADE," +
                "FOREIGN KEY(patient_id) REFERENCES " + TABLE_USERS + "(id) ON DELETE CASCADE)");

        // Create Notifications table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATIONS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "type TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "message TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "is_read BOOLEAN DEFAULT 0," +
                "related_entity_type TEXT," +
                "related_entity_id INTEGER," +
                "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id) ON DELETE CASCADE)");

        Log.d(TAG, "Database created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add new columns for version 2
            try {
                db.execSQL("ALTER TABLE " + TABLE_MEDICINES + " ADD COLUMN quantity INTEGER DEFAULT 0");
            } catch (Exception e) {
                Log.d(TAG, "Column quantity already exists or error: " + e.getMessage());
            }
            
            try {
                db.execSQL("ALTER TABLE " + TABLE_INVENTORY + " ADD COLUMN daily_usage INTEGER DEFAULT 1");
                db.execSQL("ALTER TABLE " + TABLE_INVENTORY + " ADD COLUMN estimated_refill_date TEXT");
                db.execSQL("ALTER TABLE " + TABLE_INVENTORY + " ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
                db.execSQL("ALTER TABLE " + TABLE_INVENTORY + " ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
            } catch (Exception e) {
                Log.d(TAG, "Column already exists or error: " + e.getMessage());
            }
        }
        
        if (oldVersion < 3) {
            // Add low_stock_threshold column for version 3
            try {
                db.execSQL("ALTER TABLE " + TABLE_INVENTORY + " ADD COLUMN low_stock_threshold INTEGER DEFAULT 10");
            } catch (Exception e) {
                Log.d(TAG, "Column low_stock_threshold already exists or error: " + e.getMessage());
            }
        }
        
        // For other major changes, uncomment the following:
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUARDIAN_PATIENT_LINKS);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOSE_HISTORY);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINES);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINES);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // onCreate(db);
    }

    // ============= MEDICINE OPERATIONS =============

    public long addMedicine(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", medicine.getName());
        values.put("dosage", medicine.getDosage());
        values.put("frequency", medicine.getFrequency());
        values.put("instructions", medicine.getInstructions());
        values.put("quantity", medicine.getQuantity());
        
        long id = db.insert(TABLE_MEDICINES, null, values);
        Log.d(TAG, "Medicine added: " + medicine.getName() + ", ID: " + id);
        return id;
    }

    public boolean updateMedicine(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", medicine.getName());
        values.put("dosage", medicine.getDosage());
        values.put("frequency", medicine.getFrequency());
        values.put("instructions", medicine.getInstructions());
        values.put("quantity", medicine.getQuantity());
        
        int rows = db.update(TABLE_MEDICINES, values, "id = ?", 
                new String[]{String.valueOf(medicine.getId())});
        return rows > 0;
    }

    public boolean deleteMedicine(int medicineId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_MEDICINES, "id = ?", 
                new String[]{String.valueOf(medicineId)});
        return rows > 0;
    }

    public List<Medicine> getAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICINES + " ORDER BY name", null);

        if (cursor.moveToFirst()) {
            do {
                Medicine medicine = new Medicine();
                medicine.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                medicine.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                medicine.setDosage(cursor.getString(cursor.getColumnIndexOrThrow("dosage")));
                medicine.setFrequency(cursor.getString(cursor.getColumnIndexOrThrow("frequency")));
                medicine.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow("instructions")));
                
                // Handle quantity column (may not exist in old databases)
                int quantityIndex = cursor.getColumnIndex("quantity");
                if (quantityIndex != -1) {
                    medicine.setQuantity(cursor.getInt(quantityIndex));
                }
                
                medicine.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
                medicines.add(medicine);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return medicines;
    }

    public Medicine getMedicineById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEDICINES, null, "id = ?", 
                new String[]{String.valueOf(id)}, null, null, null);

        Medicine medicine = null;
        if (cursor.moveToFirst()) {
            medicine = new Medicine();
            medicine.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            medicine.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            medicine.setDosage(cursor.getString(cursor.getColumnIndexOrThrow("dosage")));
            medicine.setFrequency(cursor.getString(cursor.getColumnIndexOrThrow("frequency")));
            medicine.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow("instructions")));
        }
        cursor.close();
        return medicine;
    }

    // ============= REMINDER OPERATIONS =============

    public long addReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medicine_id", reminder.getMedicineId());
        values.put("medicine_name", reminder.getMedicineName());
        values.put("date", reminder.getDate());
        values.put("time", reminder.getTime());
        values.put("reminder_type", reminder.getReminderType());
        values.put("taken", reminder.isTaken() ? 1 : 0);
        values.put("status", reminder.getStatus());
        values.put("notes", reminder.getNotes());
        
        long id = db.insert(TABLE_REMINDERS, null, values);
        Log.d(TAG, "Reminder added: " + reminder.getMedicineName() + ", ID: " + id);
        return id;
    }

    public boolean updateReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medicine_id", reminder.getMedicineId());
        values.put("medicine_name", reminder.getMedicineName());
        values.put("date", reminder.getDate());
        values.put("time", reminder.getTime());
        values.put("reminder_type", reminder.getReminderType());
        values.put("taken", reminder.isTaken() ? 1 : 0);
        values.put("status", reminder.getStatus());
        values.put("notes", reminder.getNotes());
        
        int rows = db.update(TABLE_REMINDERS, values, "id = ?", 
                new String[]{String.valueOf(reminder.getId())});
        return rows > 0;
    }

    public boolean deleteReminder(int reminderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_REMINDERS, "id = ?", 
                new String[]{String.valueOf(reminderId)});
        return rows > 0;
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> reminders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REMINDERS + 
                " ORDER BY date, time", null);

        if (cursor.moveToFirst()) {
            do {
                reminders.add(cursorToReminder(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reminders;
    }

    public List<Reminder> getPendingReminders() {
        List<Reminder> reminders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REMINDERS, null, "status = ?", 
                new String[]{"PENDING"}, null, null, "date, time");

        if (cursor.moveToFirst()) {
            do {
                reminders.add(cursorToReminder(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reminders;
    }

    public Reminder getReminderById(int reminderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REMINDERS, null, "id = ?", 
                new String[]{String.valueOf(reminderId)}, null, null, null);

        Reminder reminder = null;
        if (cursor.moveToFirst()) {
            reminder = cursorToReminder(cursor);
        }
        cursor.close();
        return reminder;
    }

    private Reminder cursorToReminder(Cursor cursor) {
        Reminder reminder = new Reminder();
        reminder.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        reminder.setMedicineId(cursor.getInt(cursor.getColumnIndexOrThrow("medicine_id")));
        reminder.setMedicineName(cursor.getString(cursor.getColumnIndexOrThrow("medicine_name")));
        reminder.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        reminder.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
        reminder.setReminderType(cursor.getString(cursor.getColumnIndexOrThrow("reminder_type")));
        reminder.setTaken(cursor.getInt(cursor.getColumnIndexOrThrow("taken")) == 1);
        reminder.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
        reminder.setNotes(cursor.getString(cursor.getColumnIndexOrThrow("notes")));
        reminder.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
        return reminder;
    }

    // ============= DOSE HISTORY OPERATIONS =============

    public long addDoseHistory(DoseHistory history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medicine_id", history.getMedicineId());
        values.put("reminder_id", history.getReminderId());
        values.put("medicine_name", history.getMedicineName());
        values.put("date", history.getDate());
        values.put("time", history.getTime());
        values.put("status", history.getStatus());
        values.put("notes", history.getNotes());
        
        return db.insert(TABLE_DOSE_HISTORY, null, values);
    }

    public List<DoseHistory> getAllDoseHistory() {
        List<DoseHistory> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DOSE_HISTORY + 
                " ORDER BY date DESC, time DESC", null);

        if (cursor.moveToFirst()) {
            do {
                historyList.add(cursorToDoseHistory(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyList;
    }

    private DoseHistory cursorToDoseHistory(Cursor cursor) {
        DoseHistory history = new DoseHistory();
        history.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        history.setMedicineId(cursor.getInt(cursor.getColumnIndexOrThrow("medicine_id")));
        history.setReminderId(cursor.getInt(cursor.getColumnIndexOrThrow("reminder_id")));
        history.setMedicineName(cursor.getString(cursor.getColumnIndexOrThrow("medicine_name")));
        history.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        history.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
        history.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
        history.setRecordedAt(cursor.getString(cursor.getColumnIndexOrThrow("recorded_at")));
        history.setNotes(cursor.getString(cursor.getColumnIndexOrThrow("notes")));
        return history;
    }

    // ============= USER OPERATIONS =============

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password_hash", user.getPasswordHash());
        values.put("role", user.getRole());
        values.put("full_name", user.getFullName());
        values.put("email", user.getEmail());
        values.put("active", user.isActive() ? 1 : 0);
        
        return db.insert(TABLE_USERS, null, values);
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, "username = ?", 
                new String[]{username}, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }
        cursor.close();
        return user;
    }

    public User authenticateUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, 
                "username = ? AND password_hash = ? AND active = 1", 
                new String[]{username, hashedPassword}, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }
        cursor.close();
        return user;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password_hash", user.getPasswordHash());
        values.put("full_name", user.getFullName());
        values.put("email", user.getEmail());
        values.put("active", user.isActive() ? 1 : 0);
        
        int rowsAffected = db.update(TABLE_USERS, values, "id = ?", 
                new String[]{String.valueOf(user.getId())});
        return rowsAffected > 0;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        user.setPasswordHash(cursor.getString(cursor.getColumnIndexOrThrow("password_hash")));
        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
        user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
        user.setActive(cursor.getInt(cursor.getColumnIndexOrThrow("active")) == 1);
        return user;
    }

    // ============= INVENTORY OPERATIONS =============

    public long addInventory(Inventory inventory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medicine_id", inventory.getMedicineId());
        values.put("medicine_name", inventory.getMedicineName());
        values.put("stock_level", inventory.getStockLevel());
        values.put("low_stock_threshold", inventory.getLowStockThreshold());
        values.put("daily_usage", inventory.getDailyUsage());
        values.put("last_refill_date", inventory.getLastRefillDate());
        values.put("estimated_refill_date", inventory.getEstimatedRefillDate());
        values.put("notes", inventory.getNotes());
        
        return db.insert(TABLE_INVENTORY, null, values);
    }

    public boolean updateInventory(Inventory inventory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stock_level", inventory.getStockLevel());
        values.put("low_stock_threshold", inventory.getLowStockThreshold());
        values.put("daily_usage", inventory.getDailyUsage());
        values.put("last_refill_date", inventory.getLastRefillDate());
        values.put("estimated_refill_date", inventory.getEstimatedRefillDate());
        values.put("notes", inventory.getNotes());
        values.put("updated_at", getCurrentDateTime());
        
        int rows = db.update(TABLE_INVENTORY, values, "id = ?", 
                new String[]{String.valueOf(inventory.getId())});
        return rows > 0;
    }

    public List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INVENTORY, null);

        if (cursor.moveToFirst()) {
            do {
                inventoryList.add(cursorToInventory(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return inventoryList;
    }

    private Inventory cursorToInventory(Cursor cursor) {
        Inventory inventory = new Inventory();
        inventory.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        inventory.setMedicineId(cursor.getInt(cursor.getColumnIndexOrThrow("medicine_id")));
        inventory.setMedicineName(cursor.getString(cursor.getColumnIndexOrThrow("medicine_name")));
        inventory.setStockLevel(cursor.getInt(cursor.getColumnIndexOrThrow("stock_level")));
        inventory.setLowStockThreshold(cursor.getInt(cursor.getColumnIndexOrThrow("low_stock_threshold")));
        
        // Handle new fields (may not exist in old databases)
        int dailyUsageIndex = cursor.getColumnIndex("daily_usage");
        if (dailyUsageIndex != -1) {
            inventory.setDailyUsage(cursor.getInt(dailyUsageIndex));
        }
        
        inventory.setLastRefillDate(cursor.getString(cursor.getColumnIndexOrThrow("last_refill_date")));
        
        int estimatedRefillDateIndex = cursor.getColumnIndex("estimated_refill_date");
        if (estimatedRefillDateIndex != -1) {
            inventory.setEstimatedRefillDate(cursor.getString(estimatedRefillDateIndex));
        }
        
        inventory.setNotes(cursor.getString(cursor.getColumnIndexOrThrow("notes")));
        
        int createdAtIndex = cursor.getColumnIndex("created_at");
        if (createdAtIndex != -1) {
            inventory.setCreatedAt(cursor.getString(createdAtIndex));
        }
        
        int updatedAtIndex = cursor.getColumnIndex("updated_at");
        if (updatedAtIndex != -1) {
            inventory.setUpdatedAt(cursor.getString(updatedAtIndex));
        }
        
        return inventory;
    }

    // ============= UTILITY METHODS =============

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error hashing password", e);
            return password; // Fallback (not recommended for production)
        }
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
