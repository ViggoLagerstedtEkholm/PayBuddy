package com.example.paybuddy.Maps;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paybuddy.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsPointFragment extends Fragment {
    private CoordinatesViewModel coordinatesViewModel;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            coordinatesViewModel.getLocation().observe(getViewLifecycleOwner(), locationModel -> {
                googleMap.clear();
                if(locationModel != null){
                    LatLng occasionLocation = new LatLng(locationModel.getLatitude(), locationModel.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(occasionLocation).title("Marker for occasion location."));
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(occasionLocation, 15);
                    googleMap.animateCamera(yourLocation);
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mapsView = inflater.inflate(R.layout.fragment_maps, container, false);
        coordinatesViewModel = new ViewModelProvider(requireActivity()).get(CoordinatesViewModel.class);
        return mapsView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}