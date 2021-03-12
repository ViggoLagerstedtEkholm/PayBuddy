package com.example.paybuddy.Maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.Dialogs.DialogPreviewOccasion;
import com.example.paybuddy.R;
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

/**
 * This is a Google Maps fragment that should show an array of locations.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class AllOccasionsMapFragment extends Fragment {
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
            //Observe all the locations from our occasions.
            coordinatesViewModel.getLocations().observe(getViewLifecycleOwner(), occasionModels -> {
                googleMap.clear();
                if(occasionModels != null){
                    //Get all the locations from the occasions.
                    List<LocationModel> locationModels = new ArrayList<>();
                    for(int i = 0; i < occasionModels.size(); i++) {
                        locationModels.add(occasionModels.get(i).getLocationModel());
                    }

                    //Loop through all occasions and show different icons on the map depending on it's state.
                    for(int i = 0; i < occasionModels.size(); i++){
                        OccasionModel occasionModel = occasionModels.get(i);

                        if(occasionModel.getLocationModel() != null){
                            LocationModel location = occasionModel.getLocationModel();

                            LatLng occasionLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            //Convert the vector icons to a BitmapDescriptor.
                            BitmapDescriptor warningIcon = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_warning_24);
                            BitmapDescriptor pendingIcon = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_pending_actions_24);
                            BitmapDescriptor historyIcon = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_history_24);

                            //Show pending marker icon.
                            if(occasionModel.isExpired()){
                                Marker myMapMarker = googleMap.addMarker(new MarkerOptions()
                                        .position(occasionLocation)
                                        .title(occasionModels.get(i).getDescription())
                                        .snippet("This occasion expire " + occasionModel.getDate() + " and has "
                                                + occasionModel.getItems().size() + " items. It's marked as: Expired!")
                                        .icon(warningIcon));

                                //Add occasion object to the marker.
                                myMapMarker.setTag(occasionModel);
                            }

                            //Show pending history icon.
                            if(occasionModel.isPaid()){
                                Marker myMapMarker = googleMap.addMarker(new MarkerOptions()
                                        .position(occasionLocation)
                                        .title(occasionModels.get(i).getDescription())
                                        .snippet("This occasion expire " + occasionModel.getDate() + " and has "
                                                + occasionModel.getItems().size() + " items. It's marked as: Paid!")
                                        .icon(historyIcon));

                                //Add occasion object to the marker.
                                myMapMarker.setTag(occasionModel);
                            }

                            //Show expired marker icon.
                            if(!occasionModel.isPaid() && !occasionModel.isExpired()){
                                Marker myMapMarker = googleMap.addMarker(new MarkerOptions()
                                        .position(occasionLocation)
                                        .title(occasionModels.get(i).getDescription())
                                        .snippet("This occasion expire " + occasionModel.getDate() + " and has "
                                                + occasionModel.getItems().size() + " items. It's marked as: Paid!")
                                        .icon(pendingIcon));

                                //Add occasion object to the marker.
                                myMapMarker.setTag(occasionModel);
                            }

                            //Calculate the center point of N...1 amount of latitude and longitude coordinates.
                            //This is just so we can get a reasonable zoom position when the user first opens the map.
                            //https://en.wikipedia.org/wiki/Centroid
                            CameraUpdate centroidLocation = CameraUpdateFactory.newLatLngZoom(GetCentralGeoCoordinate(locationModels), 10);
                            googleMap.animateCamera(centroidLocation);

                            //When the user clicks a marker the preview dialog should appear.
                            googleMap.setOnMarkerClickListener(marker -> {
                                OccasionModel markerObject = (OccasionModel) marker.getTag();
                                DialogPreviewOccasion dialogFragment = new DialogPreviewOccasion(markerObject);
                                dialogFragment.show(getChildFragmentManager(), "Test");
                                return false;
                            });
                        }
                    }
                }
            });
        }
    };

    /**
     * Calculate center point between 1...N number of coordinates.
     * @param geoCoordinates List of locations (latitude, longitude).
     * @return LatLng
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
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

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
