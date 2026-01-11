# DailyDose - Personal Medicine Companion (Android)

## Project Overview
This is an Android version of the DailyDose Desktop Application - a comprehensive medicine tracking and reminder system ported from JavaFX/Swing to Android.

## Features
- 💊 **Medicine Management**: Add, edit, and delete medicines with dosage and instructions
- ⏰ **Reminder System**: Set medication reminders with date and time
- 📊 **Dose History**: Track taken and missed doses
- 📦 **Inventory Management**: Monitor medicine stock levels with low-stock alerts
- 👤 **User Management**: Patient and Guardian roles with authentication
- 📈 **Adherence Analytics**: Calculate medication compliance rates

## Project Structure

```
app/src/main/java/com/example/dailydosepersonalmedicinecompanion/
├── MainActivity.java                          # Splash screen
│
├── activity/                                  # Activities
│   ├── LoginActivity.java                     # User login
│   ├── RegistrationActivity.java              # User registration
│   └── DashboardActivity.java                 # Main dashboard with bottom navigation
│
├── fragment/                                  # Fragments
│   ├── DashboardFragment.java                 # Statistics overview
│   ├── MedicineFragment.java                  # Medicine list and CRUD
│   ├── ReminderFragment.java                  # Reminder management
│   ├── InventoryFragment.java                 # Stock tracking
│   └── HistoryFragment.java                   # Dose history
│
├── adapter/                                   # RecyclerView Adapters
│   ├── MedicineAdapter.java
│   ├── ReminderAdapter.java
│   ├── InventoryAdapter.java
│   └── HistoryAdapter.java
│
├── controller/                                # Business Logic
│   ├── MedicineController.java                # Medicine operations
│   ├── ReminderController.java                # Reminder operations
│   ├── HistoryController.java                 # History tracking
│   ├── InventoryController.java               # Stock management
│   └── UserController.java                    # Authentication
│
├── database/                                  # Database Layer
│   └── DatabaseHelper.java                    # SQLite operations
│
└── model/                                     # Data Models
    ├── Medicine.java
    ├── Reminder.java
    ├── DoseHistory.java
    ├── Inventory.java
    ├── User.java
    ├── Notification.java
    └── GuardianPatientLink.java
```

## Database Schema

### Tables
1. **medicines** - Medicine information
2. **reminders** - Medication reminders
3. **dose_history** - History of taken/missed doses
4. **inventory** - Stock levels
5. **users** - User accounts (Patient/Guardian)
6. **guardian_patient_links** - Guardian-patient relationships
7. **notifications** - System notifications

## Getting Started

### Prerequisites
- Android Studio (Latest version)
- Android SDK API Level 24 (Android 7.0) or higher
- Java 11

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

### First Run
- The app will create the SQLite database automatically
- Register a new user (Patient or Guardian role)
- Start adding medicines and setting reminders

## Usage

### Login
- Use registered credentials to login
- Default role: PATIENT

### Dashboard
- View total medicines count
- See pending reminders
- Check adherence rate
- Monitor low stock items

### Medicines
- Add new medicines with dosage and frequency
- Edit existing medicines
- Delete medicines
- View all medicines in a list

### Reminders
- Create reminders for specific medicines
- Select date and time
- Mark reminders as taken
- Delete completed reminders

### Inventory
- Track medicine stock levels
- Low stock alerts (red color)
- Refill tracking

### History
- View all dose history
- Filter by taken/missed status
- Color-coded status indicators

## Architecture

### MVC Pattern
- **Models**: Data classes (Medicine, Reminder, etc.)
- **Views**: Activities, Fragments, and Layouts
- **Controllers**: Business logic classes

### Database
- SQLite for local data storage
- DatabaseHelper singleton pattern
- Foreign key constraints for data integrity

## Technologies Used
- **Language**: Java 11
- **UI**: Android XML Layouts, Material Design
- **Database**: SQLite
- **Architecture**: MVC Pattern
- **Libraries**: 
  - AndroidX AppCompat
  - Material Components
  - RecyclerView
  - CardView
  - Fragment

## Comparison with Desktop Version

| Feature | Desktop (JavaFX/Swing) | Android |
|---------|------------------------|---------|
| UI Framework | JavaFX/Swing | Android XML + Material Design |
| Database | SQLite | SQLite |
| Controllers | Shared across views | Context-based |
| Navigation | Tabs | Bottom Navigation + Fragments |
| Notifications | Desktop popups | Android Notifications (future) |
| Alarm Service | Background Thread | AlarmManager (future) |

## Future Enhancements
- [ ] Push notifications for reminders
- [ ] AlarmManager integration for persistent alarms
- [ ] PDF/CSV export functionality
- [ ] Cloud sync
- [ ] Multi-language support
- [ ] Dark mode
- [ ] Widget support
- [ ] Medication intake photos
- [ ] Prescription OCR scanning

## Author
Based on the desktop version by Kayes Ahmed (@kayes2207054)
Android adaptation: 2026

## License
Educational project for Advanced Java/Android Lab

---

**Made with ❤️ for Better Health Management 💊⏰**
