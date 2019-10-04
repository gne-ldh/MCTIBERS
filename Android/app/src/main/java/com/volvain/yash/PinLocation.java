package com.volvain.yash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PinLocation extends AppCompatActivity implements OnMapReadyCallback {




    private static final String Fine_Location = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_Location = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15;


    private GoogleMap mMap;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    ArrayList<LatLng> ListLocations = new ArrayList<>();

    double CurLat, CurLng, ENdLat, EndLng;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    SearchView sv;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_location2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sv= (SearchView) findViewById(R.id.sv);
        getDeviceLocation();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onMapReady(GoogleMap googleMap) {
        sv = findViewById(R.id.sv);
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                try {
                    getLoc();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {

            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        Location currentLocation = (Location) task.getResult();
                        CurLat = currentLocation.getLatitude();
                        CurLng = currentLocation.getLongitude();
                        Toast.makeText(PinLocation.this, "Location  found" + new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), Toast.LENGTH_SHORT).show();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "you");
                    }
                }
            });
            // }
        } catch (SecurityException e) {
           e.printStackTrace();
        }
    }



    private void moveCamera(LatLng latLng, float zoom, String title) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);

        mMap.addMarker(options);
    }





    private void getLoc() throws IOException {
        String LocationName = sv.getQuery().toString()+",India";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> AddressList = geocoder.getFromLocationName(LocationName, 1);
        if (AddressList.size() > 0) {
            Address address = AddressList.get(0);
            ListLocations.add(new LatLng(address.getLatitude(), address.getLongitude()));
            mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 15,""+address );
        }
    }



}
