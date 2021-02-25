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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class DialogAddLocation extends DialogFragment implements View.OnClickListener, OnSuccessListener {
    private static final int PERMISSION_FOR_FINE_LOCATION = 100;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallBack;

    private LocationViewModel locationViewModel;
    private View currentView;

    private final int UPDATING_INTERVAL_SPEED = 2000;
    private final int UPDATING_INTERVAL_MIN_SPEED = 1000;

    private TextView valueLatitude;
    private TextView valueLongitude;
    private TextView valueAltitude;
    private TextView valueAccuracy;
    private TextView valueUpdateStatus;
    private TextView valueTypeOfLocationAccuracy;
    private TextView valueAdress;

    private Switch slideLocation;
    private Switch slideLocationMode;

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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_location, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);

        final AlertDialog alertDialog= alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        initializeFields(view);

        return alertDialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentView = view;
    }

    private void initializeFields(View view) {
        Button backButton = (Button) view.findViewById(R.id.buttonBackLocation);
        Button saveButton = (Button) view.findViewById(R.id.buttonSaveLocation);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = Double.valueOf(String.valueOf(valueLatitude.getText()));
                double longitude = Double.valueOf(String.valueOf(valueLongitude.getText()));
                double altitude = Double.valueOf(String.valueOf(valueAltitude.getText()));
                double accuracy = Double.valueOf(String.valueOf(valueAccuracy.getText()));
                String address = valueAdress.getText().toString();

                LocationModel model = new LocationModel(latitude, longitude, altitude, accuracy, address);
                locationViewModel.setLocation(model);
                stopLocationUpdate();
                dismiss();
            }
        });

        valueLatitude = (TextView) view.findViewById(R.id.valueLatitude);
        valueLongitude = (TextView) view.findViewById(R.id.valueLongitude);
        valueAltitude = (TextView) view.findViewById(R.id.valueAltitude);
        valueAccuracy = (TextView) view.findViewById(R.id.valueAccuracy);
        valueUpdateStatus = (TextView) view.findViewById(R.id.valueUpdateStatus);
        valueTypeOfLocationAccuracy = (TextView) view.findViewById(R.id.valueTypeOfLocationAccuracy);
        valueAdress = (TextView) view.findViewById(R.id.valueAdress);

        slideLocation = (Switch) view.findViewById(R.id.slideLocation);
        slideLocationMode = (Switch) view.findViewById(R.id.slideLocationMode);

        slideLocation.setOnClickListener(this);
        slideLocationMode.setOnClickListener(this);
    }

    private void initializeLocationAPI() {
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
            } else {
                Toast.makeText(getContext(), "This app can't operate without your permission!", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }

    @Override
    public void onSuccess(Object location) {
        updateAllFields((Location) location);
    }

    private void updateAllFields(Location location) {
        if(location != null){
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());

            valueLatitude.setText(latitude);
            valueLongitude.setText(longitude);

            if (location.hasAltitude()) {
                String altitude = String.valueOf(location.getAltitude());
                valueAltitude.setText(altitude);
            } else {
                valueAltitude.setText("No altitude detected.");
            }

            if (location.hasAccuracy()) {
                String Accuracy = String.valueOf(location.getAccuracy());
                valueAccuracy.setText(Accuracy);
            } else {
                valueAccuracy.setText("No accuracy detected.");
            }

            Geocoder geocoder = new Geocoder(getActivity());

            try {
                List<Address> locations = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                valueAdress.setText(locations.get(0).getAddressLine(0));
            } catch (IOException e) {
                valueAdress.setText("Adress failed to fetch.");
            }
            catch(Exception e){
                valueAdress.setText("Loading...");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.slideLocation:
                if (slideLocation.isChecked()) {
                    startLocationUpdate();
                } else {
                    stopLocationUpdate();
                }
                break;
            case R.id.slideLocationMode:
                if (slideLocationMode.isChecked()) {
                    locationRequest.setPriority(locationRequest.PRIORITY_LOW_POWER);
                    valueTypeOfLocationAccuracy.setText("Lowest location accuracy.");
                } else {
                    locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
                    valueTypeOfLocationAccuracy.setText("Highest location acccuracy");
                }
                break;
        }
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            valueUpdateStatus.setText("Location is being updated.");
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FOR_FINE_LOCATION);
            }
        }
    }

    private void stopLocationUpdate() {
        valueUpdateStatus.setText("Location is NOT being updated.");
        valueLatitude.setText("Updates off.");
        valueLongitude.setText("Updates off.");
        valueAccuracy.setText("Updates off.");
        valueAdress.setText("Updates off.");
        valueAltitude.setText("Updates off");
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }
}
