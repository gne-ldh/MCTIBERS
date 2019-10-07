package com.volvain.yash;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.volvain.yash.DAO.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PinLocation extends AppCompatActivity implements OnMapReadyCallback {


    private static final String Fine_Location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15;


    private GoogleMap mMap;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public static ArrayList<ArrayList<Double>> ListLocations = new ArrayList<>();

    double CurLat, CurLng, ENdLat, EndLng;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    SearchView sv;
    Database db;
    FloatingActionButton floatingButton;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_location2);
        floatingButton = (FloatingActionButton) findViewById(R.id.FloatingButton);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

                ListLocations.clear();

        sv = (SearchView) findViewById(R.id.sv);
        db = new Database(this);
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
                   //   Toast.makeText(PinLocation.this, "Location  found" + new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), Toast.LENGTH_SHORT).show();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "you");
                    }
                }
            });

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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();

                for(ArrayList<Double> lst:ListLocations){



                if(lst.get(0).equals(marker.getPosition().longitude)&&lst.get(1).equals(marker.getPosition().latitude)){
                    ListLocations.remove(ListLocations.indexOf(lst));
                    break;}
                }

                return true;
            }
        });


        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToDatabase();
            }
        });

    }


    private void getLoc() throws IOException {

        String LocationName = sv.getQuery().toString() + ",India";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> AddressList = geocoder.getFromLocationName(LocationName, 1);
       getPinnedLocations();
        if (AddressList.size() > 0) {
            Address address = AddressList.get(0);
            ArrayList<Double> list = new ArrayList<>();
            list.add(0, address.getLongitude());
            list.add(1, address.getLatitude());
            ListLocations.add(list);

            for (ArrayList firstList:ListLocations){
                if(firstList.isEmpty())return;
                LatLng loc=new LatLng((Double)firstList.get(1),(Double) firstList.get(0));
                mMap.addMarker(new MarkerOptions().position(loc));
                moveCamera(loc,15,"");}

        }
    }




    public void getPinnedLocations() {

       ArrayList lst=db.get();
        if(lst!=null)ListLocations=lst;

    }

    public void addToDatabase(){

        if(Global.checkInternet()==0){
           db.clearLocations();
           db.insertLngLng(ListLocations);

        OneTimeWorkRequest workRequest=new OneTimeWorkRequest.Builder(SetUserLocServer.class).build();
        WorkManager.getInstance().enqueue(workRequest);
        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING||workInfo.getState() == WorkInfo.State.ENQUEUED)
                            Toast.makeText(PinLocation.this, "Processing!", Toast.LENGTH_LONG).show();
                        else if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            Toast.makeText(PinLocation.this,"Locations Added!",Toast.LENGTH_LONG).show();
                        }
                        else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                            Toast.makeText(PinLocation.this,"Error Adding Locations",Toast.LENGTH_LONG).show();
                        }
                    }
                });}
        else Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();

    }

}
