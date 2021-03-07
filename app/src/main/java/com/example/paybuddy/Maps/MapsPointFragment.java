package com.example.paybuddy.Maps;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.OccasionViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsPointFragment extends Fragment {
    private CoordinatesViewModel coordinatesViewModel;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            coordinatesViewModel.getLocation().observe(getViewLifecycleOwner(), new Observer<LocationModel>() {
                @Override
                public void onChanged(LocationModel locationModel) {
                    googleMap.clear();
                    if(locationModel != null){
                        LatLng occasionLocation = new LatLng(locationModel.getLatitude(), locationModel.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(occasionLocation).title("Marker for occasion location."));
                        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(occasionLocation, 15);
                        googleMap.animateCamera(yourLocation);
                    }
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
        coordinatesViewModel = new ViewModelProvider(getActivity()).get(CoordinatesViewModel.class);
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