package com.example.homework3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment {
    private SharedViewModel viewModel;
    private List<Location> locations;
    private LocationAdapter locationAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        fillLocations(viewModel);
        recyclerView = view.findViewById(R.id.recyclerView);
        locationAdapter = new LocationAdapter(locations);
        recyclerView.setAdapter(locationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        return view;
    }

    public void fillLocations(SharedViewModel viewModel){
        locations = new ArrayList<>();
        viewModel.getLocations().observe(getViewLifecycleOwner(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> l) {
                if (l != null){
                    for (int i = 0; i < l.size(); i++){
                        Location location = l.get(i);
                        locations.add(location);
                    }
                }
            }
        });
    }
}
