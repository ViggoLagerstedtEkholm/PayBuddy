package com.example.paybuddy.Maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.Dialogs.DialogPreviewOccasion;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.LocationViewModel;
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

public class AllOccasionsMapFragment extends Fragment {
    private CoordinatesViewModel coordinatesViewModel;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            coordinatesViewModel.getLocations().observe(getViewLifecycleOwner(), new Observer<List<OccasionModel>>() {
                @Override
                public void onChanged(List<OccasionModel> occasionModels) {
                    googleMap.clear();
                    if(occasionModels != null){
                        List<LocationModel> locationModels = new ArrayList<>();
                        for(int i = 0; i < occasionModels.size(); i++) {
                            locationModels.add(occasionModels.get(i).getLocationModel());
                        }

                        for(int i = 0; i < occasionModels.size(); i++){
                            OccasionModel occasionModel = occasionModels.get(i);

                            if(occasionModel.getLocationModel() != null){
                                LocationModel location = occasionModel.getLocationModel();

                                LatLng occasionLocation = new LatLng(location.getLatitude(), location.getLongitude());

                                BitmapDescriptor warningIcon = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_warning_24);
                                BitmapDescriptor pendingIcon = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_pending_actions_24);
                                BitmapDescriptor historyIcon = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_history_24);

                                if(occasionModel.isExpired()){
                                    Marker myMapMarker = googleMap.addMarker(new MarkerOptions()
                                            .position(occasionLocation)
                                            .title(occasionModels.get(i).getDescription())
                                            .snippet("This occasion expire " + occasionModel.getDate() + " and has "
                                                    + occasionModel.getItems().size() + " items. It's marked as: Expired!")
                                            .icon(warningIcon));

                                    myMapMarker.setTag(occasionModel);
                                }

                                if(occasionModel.isPaid()){
                                    Marker myMapMarker = googleMap.addMarker(new MarkerOptions()
                                            .position(occasionLocation)
                                            .title(occasionModels.get(i).getDescription())
                                            .snippet("This occasion expire " + occasionModel.getDate() + " and has "
                                                    + occasionModel.getItems().size() + " items. It's marked as: Paid!")
                                            .icon(historyIcon));

                                    myMapMarker.setTag(occasionModel);
                                }

                                if(!occasionModel.isPaid() && !occasionModel.isExpired()){
                                    Marker myMapMarker = googleMap.addMarker(new MarkerOptions()
                                            .position(occasionLocation)
                                            .title(occasionModels.get(i).getDescription())
                                            .snippet("This occasion expire " + occasionModel.getDate() + " and has "
                                                    + occasionModel.getItems().size() + " items. It's marked as: Paid!")
                                            .icon(pendingIcon));

                                    myMapMarker.setTag(occasionModel);
                                }


                                CameraUpdate centroidLocation = CameraUpdateFactory.newLatLngZoom(GetCentralGeoCoordinate(locationModels), 10);
                                googleMap.animateCamera(centroidLocation);

                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        OccasionModel markerObject = (OccasionModel) marker.getTag();
                                        DialogPreviewOccasion dialogFragment = new DialogPreviewOccasion(markerObject);
                                        dialogFragment.show(getChildFragmentManager(), "Test");
                                        return false;
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    };

    /**
     * Calculate center point between 1...N number of coordinates.
     * @param geoCoordinates List of locations (latitude, longitude).
     * @return
     */
    public static LatLng GetCentralGeoCoordinate(List<LocationModel> geoCoordinates)
    {
        if (geoCoordinates.size() == 1)
        {
            return new LatLng(geoCoordinates.get(0).getLatitude(), geoCoordinates.get(0).getLongitude());
        }

        double x = 0;
        double y = 0;
        double z = 0;

        for (int i = 0; i < geoCoordinates.size(); i++)
        {
            double latitude = geoCoordinates.get(i).getLatitude() * Math.PI / 180;
            double longitude = geoCoordinates.get(i).getLongitude() * Math.PI / 180;

            x += Math.cos(latitude) * Math.cos(longitude);
            y += Math.cos(latitude) * Math.sin(longitude);
            z += Math.sin(latitude);
        }

        double total = geoCoordinates.size();

        x = x / total;
        y = y / total;
        z = z / total;

        double centralLongitude = Math.atan2(y, x);
        double centralSquareRoot = Math.sqrt(x * x + y * y);
        double centralLatitude = Math.atan2(z, centralSquareRoot);

        return new LatLng(centralLatitude * 180 / Math.PI, centralLongitude * 180 / Math.PI);
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

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
