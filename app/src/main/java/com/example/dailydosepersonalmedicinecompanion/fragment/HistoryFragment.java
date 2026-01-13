package com.example.dailydosepersonalmedicinecompanion.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.adapter.HistoryAdapter;
import com.example.dailydosepersonalmedicinecompanion.controller.HistoryController;
import com.example.dailydosepersonalmedicinecompanion.controller.MedicineController;
import com.example.dailydosepersonalmedicinecompanion.database.DatabaseHelper;
import com.example.dailydosepersonalmedicinecompanion.model.DoseHistory;
import com.example.dailydosepersonalmedicinecompanion.model.Medicine;

import java.util.List;
import java.util.Random;

/**
 * HistoryFragment
 * Shows dose history and adherence statistics
 */
public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private HistoryController historyController;
    private MedicineController medicineController;
    
    private TextView tvTotalTaken, tvTotalMissed, tvAdherence;
    private LinearLayout emptyState;
    private Button btnAddSample;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        historyController = new HistoryController(requireContext());
        medicineController = new MedicineController(requireContext());

        // Initialize views
        tvTotalTaken = view.findViewById(R.id.tv_total_taken);
        tvTotalMissed = view.findViewById(R.id.tv_total_missed);
        tvAdherence = view.findViewById(R.id.tv_adherence);
        emptyState = view.findViewById(R.id.empty_state);
        btnAddSample = view.findViewById(R.id.btn_add_sample);
        
        recyclerView = view.findViewById(R.id.recycler_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        adapter = new HistoryAdapter(historyController.getAllHistory());
        recyclerView.setAdapter(adapter);

        // Add sample history button
        btnAddSample.setOnClickListener(v -> addSampleHistory());

        // Load data
        loadStatistics();
        updateEmptyState();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        adapter.updateList(historyController.getAllHistory());
        loadStatistics();
        updateEmptyState();
    }

    private void loadStatistics() {
        int totalTaken = historyController.getTotalDosesTaken();
        int totalMissed = historyController.getTotalDosesMissed();
        double adherence = historyController.getAdherenceRate();

        tvTotalTaken.setText(String.valueOf(totalTaken));
        tvTotalMissed.setText(String.valueOf(totalMissed));
        tvAdherence.setText(String.format("%.0f%%", adherence));
    }

    private void updateEmptyState() {
        List<DoseHistory> history = historyController.getAllHistory();
        if (history.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void addSampleHistory() {
        // Get all medicines
        List<Medicine> medicines = medicineController.getAllMedicines();
        
        if (medicines.isEmpty()) {
            Toast.makeText(requireContext(), 
                "Please add medicines first in the Medicines section", 
                Toast.LENGTH_LONG).show();
            return;
        }

        // Create sample history entries
        Random random = new Random();
        String[] statuses = {"TAKEN", "TAKEN", "TAKEN", "MISSED"}; // 75% taken, 25% missed
        String[] times = {"08:00", "12:00", "18:00", "20:00", "22:00"};
        
        // Add 5 random entries
        for (int i = 0; i < 5; i++) {
            Medicine medicine = medicines.get(random.nextInt(medicines.size()));
            
            DoseHistory history = new DoseHistory();
            history.setMedicineId(medicine.getId());
            history.setMedicineName(medicine.getName());
            history.setDate(DatabaseHelper.getCurrentDate());
            history.setTime(times[i % times.length]);
            history.setStatus(statuses[random.nextInt(statuses.length)]);
            history.setNotes("Sample entry for demo");
            
            historyController.addHistory(history);
        }

        Toast.makeText(requireContext(), 
            "✓ Added 5 sample history entries!", 
            Toast.LENGTH_SHORT).show();
        
        refreshData();
    }
}

