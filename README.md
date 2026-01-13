# 💊 DailyDose - Personal Medicine Companion (Android)

![Version](https://img.shields.io/badge/version-2.0.0-blue.svg)
![Android](https://img.shields.io/badge/Android-8.0%2B-green.svg)
![Java](https://img.shields.io/badge/Java-11-orange.svg)

**A comprehensive Android application for managing medicines, setting reminders, tracking inventory, and monitoring medication adherence - Mobile version of the DailyDose Desktop App.**

---

## ✨ Key Features

### 🏠 Dashboard
- **Live Statistics**: Total medicines, pending reminders, doses taken/missed today, adherence rate
- **Real-time Clock**: Current date and time display  
- **Recent Activity**: Last 7 days dose history with color-coded status
- **Quick Stats**: Active days tracking, low stock alerts

### 💊 Medicine Management  
- **Search & Filter**: Real-time search by name/dosage, filter by frequency
- **Full CRUD**: Add, Edit, Delete medicines with validation
- **Details**: Name, dosage, frequency, instructions, quantity tracking
- **Modern UI**: RecyclerView with CardView, clean design

### ⏰ Reminder & Alarm System
- **Background Service**: 24/7 monitoring for upcoming reminders
- **Push Notifications**: Timely alerts with custom sound
- **Interactive Actions**:
  - ✓ **TAKEN** - Mark as taken (adds to history)
  - ⏰ **SNOOZE** - Delay 5 minutes
  - ✗ **MISS** - Mark as missed
- **Auto-status Updates**: PENDING → TAKEN/MISSED
- **Reminder Management**: Add, edit, delete reminders

### 📦 Inventory
- **Stock Tracking**: Real-time quantity monitoring
- **Low Stock Alerts**: Visual RED indicators
- **Daily Usage**: Calculate refill dates  
- **Threshold Management**: Custom low-stock levels

### 📊 History & Analytics
- **Complete Records**: All doses with date/time/status
- **Adherence Rate**: Percentage calculation
- **Status Breakdown**: Taken vs Missed counts
- **Date Filtering**: View specific periods

---

## 🚀 Installation

### Download APK
1. Go to [Releases](https://github.com/kayes2207054/DailyDose_Personal_Medicine_Companion/releases)
2. Download latest `DailyDose.apk`
3. Install on Android device (API 26+)

### Build from Source
```bash
git clone https://github.com/kayes2207054/DailyDose_Personal_Medicine_Companion.git
cd DailyDosePersonalMedicineCompanion
# Open in Android Studio
# Build > Build APK
```

---

## 📖 Quick Start Guide

### First Launch
1. **Splash Screen** → Auto-redirects to Login
2. **Register** new account OR use test account
3. **Dashboard** opens with navigation

### Add Medicine
1. Tap **Medicines** tab → **+ Add Medicine**
2. Enter: Name, Dosage, Frequency, Instructions
3. **Save** → Appears in list

### Set Reminder
1. Tap **Reminders** → **+ Add Reminder**
2. Select medicine, date, time
3. **Save** → Notification will trigger at time

### Handle Notification
- **Notification appears** at reminder time
- Tap **TAKEN** → Marks complete, adds to history
- Tap **SNOOZE** → Delays 5 minutes
- Tap **MISS** → Marks missed

---

## 🏗️ Architecture

**Pattern**: MVC (Model-View-Controller)

### Layers
- **Models**: `Medicine`, `Reminder`, `DoseHistory`, `Inventory`, `User`
- **Views**: Activities + Fragments with RecyclerViews
- **Controllers**: Business logic layer
- **Database**: SQLite with `DatabaseHelper` singleton

### Key Components
```
MainActivity (Splash) 
  ↓
LoginActivity / RegistrationActivity
  ↓
DashboardActivity
  ├── DashboardFragment (Statistics)
  ├── MedicineFragment (CRUD)
  ├── ReminderFragment (Alarms)
  ├── InventoryFragment (Stock)
  └── HistoryFragment (Records)
```

---

## 🛠️ Tech Stack

- **Language**: Java 11
- **Min SDK**: API 26 (Android 8.0)
- **Target SDK**: API 36 (Android 14)
- **Database**: SQLite
- **Build**: Gradle 8.7
- **UI**: Material Design, RecyclerView, CardView
- **Services**: Background AlarmManager, Notifications

---

## 📁 Project Structure

```
app/src/main/java/.../dailydosepersonalmedicinecompanion/
├── activity/
│   ├── DashboardActivity.java
│   ├── LoginActivity.java
│   └── RegistrationActivity.java
├── fragment/
│   ├── DashboardFragment.java
│   ├── MedicineFragment.java
│   ├── ReminderFragment.java
│   ├── InventoryFragment.java
│   └── HistoryFragment.java
├── adapter/
│   ├── MedicineAdapter.java
│   ├── ReminderAdapter.java
│   ├── InventoryAdapter.java
│   └── HistoryAdapter.java
├── controller/
│   ├── MedicineController.java
│   ├── ReminderController.java
│   ├── HistoryController.java
│   ├── InventoryController.java
│   └── UserController.java
├── database/
│   └── DatabaseHelper.java (v3)
├── model/
│   ├── Medicine.java
│   ├── Reminder.java
│   ├── DoseHistory.java
│   ├── Inventory.java
│   └── User.java
└── MainActivity.java
```

---

## 🔧 Troubleshooting

**Notifications not showing?**
- Enable notifications in Settings > Apps > DailyDose
- Check battery optimization settings

**Database errors?**
- Uninstall and reinstall app
- Or clear app data in Settings

**Reminders not triggering?**
- Ensure notification permission granted
- Check "Do Not Disturb" is off
- Verify future date/time selected

---

## 🚀 Roadmap

- [x] Medicine CRUD
- [x] Reminder system
- [x] Inventory tracking  
- [x] Dashboard statistics
- [x] User authentication
- [ ] Backup/Restore
- [ ] Settings screen
- [ ] Recurring reminders
- [ ] Charts/Analytics
- [ ] Export to CSV

---

## 👨‍💻 Author

**Kayes Ahmed**
- GitHub: [@kayes2207054](https://github.com/kayes2207054)
- Email: kayes2207054@gmail.com
- Institution: Leading University, Sylhet

---

## 📄 License

MIT License - See [LICENSE](LICENSE)

---

**Made with ❤️ for Better Health** 💊📱⏰

