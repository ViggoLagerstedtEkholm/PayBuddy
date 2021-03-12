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

/**
 * This is a Google Maps fragment that should show 1 location.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class MapsPointFragment extends Fragment {
    private CoordinatesViewModel coordinatesViewModel;

    /**
     * Callback interface for when the map is ready to be used.
     */
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * This method gets called when the map is ready to be used.
         * @param googleMap the map object.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //Observe the location and create a marker.
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


    /**
     * This method creates our ViewModel and inflates our fragment with the map.
     * @param inflater the inflater for our view.
     * @param container a view that containts other views.
     * @param savedInstanceState the last saved instance.
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mapsView = inflater.inflate(R.layout.fragment_maps, container, false);
        coordinatesViewModel = new ViewModelProvider(requireActivity()).get(CoordinatesViewModel.class);
        return mapsView;
    }

    /**
     * When we have created our view we can get the map fragment from our view.
     * @param view the inflated view for this fragment.
     * @param savedInstanceState latest saved instance.
     */
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