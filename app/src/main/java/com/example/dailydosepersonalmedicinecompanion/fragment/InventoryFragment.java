package com.example.dailydosepersonalmedicinecompanion.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailydosepersonalmedicinecompanion.R;
import com.example.dailydosepersonalmedicinecompanion.adapter.InventoryAdapter;
import com.example.dailydosepersonalmedicinecompanion.controller.InventoryController;

/**
 * InventoryFragment
 * Shows medicine stock levels
 */
public class InventoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private InventoryController inventoryController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        inventoryController = new InventoryController(requireContext());

        recyclerView = view.findViewById(R.id.recycler_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        adapter = new InventoryAdapter(inventoryController.getAllInventory());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateList(inventoryController.getAllInventory());
    }
}
