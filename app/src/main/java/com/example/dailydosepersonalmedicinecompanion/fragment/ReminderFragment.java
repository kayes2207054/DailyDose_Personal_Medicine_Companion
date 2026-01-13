package com.example.dailydosepersonalmedicinecompanion.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.adapter.ReminderAdapter;
import com.example.dailydosepersonalmedicinecompanion.controller.HistoryController;
import com.example.dailydosepersonalmedicinecompanion.controller.MedicineController;
import com.example.dailydosepersonalmedicinecompanion.controller.ReminderController;
import com.example.dailydosepersonalmedicinecompanion.database.DatabaseHelper;
import com.example.dailydosepersonalmedicinecompanion.model.DoseHistory;
import com.example.dailydosepersonalmedicinecompanion.model.Medicine;
import com.example.dailydosepersonalmedicinecompanion.model.Reminder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * ReminderFragment
 * Manages medication reminders
 */
public class ReminderFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private ReminderAdapter adapter;
    private ReminderController reminderController;
    private MedicineController medicineController;
    private HistoryController historyController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        reminderController = new ReminderController(requireContext());
        medicineController = new MedicineController(requireContext());
        historyController = new HistoryController(requireContext());

        recyclerView = view.findViewById(R.id.recycler_reminders);
        fabAdd = view.findViewById(R.id.fab_add_reminder);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ReminderAdapter(reminderController.getPendingReminders(), 
            this::markAsTaken, this::deleteReminder);
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddDialog());

        return view;
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_reminder, null);
        Spinner spinnerMedicine = dialogView.findViewById(R.id.spinner_medicine);
        TextView tvDate = dialogView.findViewById(R.id.tv_date);
        TextView tvTime = dialogView.findViewById(R.id.tv_time);

        // Populate medicine spinner
        List<Medicine> medicines = medicineController.getAllMedicines();
        List<String> medicineNames = new ArrayList<>();
        for (Medicine m : medicines) {
            medicineNames.add(m.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_spinner_item, medicineNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicine.setAdapter(adapter);

        final String[] selectedDate = {""};
        final String[] selectedTime = {""};

        tvDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                selectedDate[0] = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                tvDate.setText(selectedDate[0]);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        tvTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
                selectedTime[0] = String.format("%02d:%02d", hourOfDay, minute);
                tvTime.setText(selectedTime[0]);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        });

        new AlertDialog.Builder(requireContext())
                .setTitle("Add Reminder")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    int position = spinnerMedicine.getSelectedItemPosition();
                    if (position >= 0 && !selectedDate[0].isEmpty() && !selectedTime[0].isEmpty()) {
                        Medicine medicine = medicines.get(position);
                        Reminder reminder = new Reminder(medicine.getId(), medicine.getName(), 
                            selectedDate[0], selectedTime[0], "DAILY");
                        long result = reminderController.addReminder(reminder);
                        if (result > 0) {
                            Toast.makeText(requireContext(), "Reminder added", Toast.LENGTH_SHORT).show();
                            refreshList();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void markAsTaken(Reminder reminder) {
        // Add to dose history
        DoseHistory history = new DoseHistory();
        history.setMedicineId(reminder.getMedicineId());
        history.setReminderId(reminder.getId());
        history.setMedicineName(reminder.getMedicineName());
        history.setDate(DatabaseHelper.getCurrentDate());
        history.setTime(DatabaseHelper.getCurrentTime());
        history.setStatus("TAKEN");
        history.setNotes("Marked from reminder list");
        
        historyController.addHistory(history);
        
        // Delete the reminder (it's now in history)
        if (reminderController.deleteReminder(reminder.getId())) {
            Toast.makeText(requireContext(), "✓ Marked as taken", Toast.LENGTH_SHORT).show();
            refreshList();
        }
    }

    private void deleteReminder(Reminder reminder) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Reminder")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (reminderController.deleteReminder(reminder.getId())) {
                        Toast.makeText(requireContext(), "Reminder deleted", Toast.LENGTH_SHORT).show();
                        refreshList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshList() {
        adapter.updateList(reminderController.getPendingReminders());
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
