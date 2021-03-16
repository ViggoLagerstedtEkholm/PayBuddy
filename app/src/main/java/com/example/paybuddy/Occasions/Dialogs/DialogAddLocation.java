package com.example.paybuddy.Occasions.Dialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Occasions.ViewModel.LocationViewModel;
import com.example.paybuddy.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

/**
 *  This dialog shows the "Add location" function. Here the user can add a location.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class DialogAddLocation extends DialogFragment implements OnSuccessListener {
    private static final int PERMISSION_FOR_FINE_LOCATION = 100;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallBack;

    private LocationViewModel locationViewModel;

    private TextView valueLatitude;
    private TextView valueLongitude;
    private TextView valueAltitude;
    private TextView valueAccuracy;
    private TextView valueUpdateStatus;
    private TextView valueTypeOfLocationAccuracy;
    private TextView valueAddress;

    private double latitude;
    private double longitude;
    private double altitude;
    private double accuracy;
    private String address;

    private SwitchCompat slideLocation;
    private SwitchCompat slideLocationMode;

    private Button saveButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationViewModel = new ViewModelProvider(requireParentFragment()).get(LocationViewModel.class);
        initializeLocationAPI();
        initializeGPSCapabilities();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_location, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);

        final AlertDialog alertDialog= alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        initializeFields(view);

        return alertDialog;
    }

    private void initializeFields(View view) {
        Button backButton = view.findViewById(R.id.buttonBackLocation);
        saveButton = view.findViewById(R.id.buttonSaveLocation);
        saveButton.setEnabled(false);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            saveButton.setEnabled(true);
        }

        backButton.setOnClickListener(v -> dismiss());

        saveButton.setOnClickListener(v -> {
            LocationModel model = new LocationModel(latitude, longitude, altitude, accuracy, address);
            locationViewModel.setLocation(model);
            stopLocationUpdate();
            dismiss();
        });

        valueLatitude = view.findViewById(R.id.valueLatitude);
        valueLongitude = view.findViewById(R.id.valueLongitude);
        valueAltitude = view.findViewById(R.id.valueAltitude);
        valueAccuracy = view.findViewById(R.id.valueAccuracy);
        valueUpdateStatus = view.findViewById(R.id.valueUpdateStatus);
        valueTypeOfLocationAccuracy = view.findViewById(R.id.valueTypeOfLocationAccuracy);
        valueAddress = view.findViewById(R.id.valueAdress);

        slideLocation = view.findViewById(R.id.slideLocation);
        slideLocationMode = view.findViewById(R.id.slideLocationMode);

        slideLocation.setOnClickListener(v -> {
            if (slideLocation.isChecked()) {
                startLocationUpdate();
            } else {
                stopLocationUpdate();
            }
        });
        slideLocationMode.setOnClickListener(v -> {
            if (slideLocationMode.isChecked()) {
                locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                valueTypeOfLocationAccuracy.setText(R.string.lowest_location_accuracy);
            } else {
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                valueTypeOfLocationAccuracy.setText(R.string.highest_location_accuracy);
            }
        });


    }

    private void initializeLocationAPI() {
        int UPDATING_INTERVAL_MIN_SPEED = 1000;
        int UPDATING_INTERVAL_SPEED = 2000;
        locationRequest = new LocationRequest()
                .setInterval(UPDATING_INTERVAL_SPEED)
                .setFastestInterval(UPDATING_INTERVAL_MIN_SPEED)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                updateAllFields(location);
            }
        };
    }

    private void initializeGPSCapabilities() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FOR_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_FOR_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeGPSCapabilities();
                saveButton.setEnabled(true);
            } else {
                Toast.makeText(getContext(), "Permission was not granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSuccess(Object location) {
        updateAllFields((Location) location);
    }

    private void updateAllFields(Location location) {
        if(location != null){
            latitude = Double.parseDouble(String.valueOf(location.getLatitude()));
            longitude = Double.parseDouble(String.valueOf(location.getLongitude()));

            valueLatitude.setText(String.valueOf(latitude));
            valueLongitude.setText(String.valueOf(longitude));

            if (location.hasAltitude()) {
                String altitudeText = String.valueOf(location.getAltitude());
                valueAltitude.setText(altitudeText);
                altitude = Double.parseDouble(String.valueOf(location.getAltitude()));
            } else {
                valueAltitude.setText(R.string.no_altitude_detected);
                altitude = 0.0;
            }

            if (location.hasAccuracy()) {
                String Accuracy = String.valueOf(location.getAccuracy());
                valueAccuracy.setText(Accuracy);
                accuracy = Double.parseDouble(String.valueOf(location.getAccuracy()));
            } else {
                valueAccuracy.setText(R.string.no_accuracy_detected);
                accuracy = 0.0;
            }

            try {
                Geocoder geocoder = new Geocoder(getActivity());
                List<Address> locations = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                valueAddress.setText(locations.get(0).getAddressLine(0));
                address = (locations.get(0).getAddressLine(0));
            } catch (IOException e) {
                valueAddress.setText(R.string.address_failed_to_fetch);
            }
            catch(Exception e){
                valueAddress.setText(R.string.loading);
            }
        }
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            valueUpdateStatus.setText(R.string.location_being_updated);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FOR_FINE_LOCATION);
            }
        }
    }

    private void stopLocationUpdate() {
        valueUpdateStatus.setText(R.string.location_not_being_updated);
        valueLatitude.setText(R.string.updates_off);
        valueLongitude.setText(R.string.updates_off);
        valueAccuracy.setText(R.string.updates_off);
        valueAddress.setText(R.string.updates_off);
        valueAltitude.setText(R.string.updates_off);
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }
}
