package com.volvain.yash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.volvain.yash.DAO.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PinLocation extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener{

    private int currLocSet=0;
    private static final String Fine_Location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15;
    private LocationRequest mLocationRequest=null;
    GoogleApiClient mGoogleApiClient=null;
    private Location mLastKnownLocation=null;
    private LocationCallback locationCallback=null;
    private AlertDialog.Builder loadingBuilder;
    private AlertDialog loading;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public static ArrayList<ArrayList<Double>> ListLocations = new ArrayList<>();

    double CurLat, CurLng, ENdLat, EndLng;

    SearchView sv;
    Database db;
    FloatingActionButton floatingButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_location2);
        floatingButton = (FloatingActionButton) findViewById(R.id.FloatingButton);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ListLocations.clear();
       // sv = (SearchView) findViewById(R.id.sv);
        db = new Database(this);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        loading=getDialogProgressBar().create();


    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);



        String apiKey = getString(R.string.apiKey);
        if(!Places.isInitialized())
            Places.initialize(getApplicationContext(),apiKey);
        PlacesClient placesClient=Places.createClient(this);
       AutocompleteSupportFragment autocompleteSupportFragment=(AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.autocomplete);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.ADDRESS));
        autocompleteSupportFragment.getView().setBackgroundColor(Color.WHITE);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
               String LocationName =place.getName();

                try {
                    getLoc(LocationName);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("anaa"," "+LocationName);
            }

            @Override
            public void onError(@NonNull Status status) {
             Log.i("anaa","error "+status);
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
       if(currLocSet!=0) {
           MarkerOptions options = new MarkerOptions()
                   .position(latLng)
                   .title(title);

           mMap.addMarker(options);
       }
       if(currLocSet==0)currLocSet=1;
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
                           loading.show();// Toast.makeText(PinLocation.this, "Processing!", Toast.LENGTH_LONG).show();
                        else if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                          loading.dismiss();
                            Toast.makeText(PinLocation.this,"Locations Added!",Toast.LENGTH_LONG).show();
                        }
                        else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                            loading.dismiss();
                            Toast.makeText(PinLocation.this,"Error Adding Locations",Toast.LENGTH_LONG).show();
                        }
                    }
                });}
        else Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();

    }


    public void getLoc(String location) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> AddressList = geocoder.getFromLocationName(location, 1);
        //   getPinnedLocations();
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
    }}
    protected void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();
    }
    protected void createLocationRequest() {

        //remove location updates so that it resets
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this); //Import should not be **android.Location.LocationListener**
        //import should be **import com.google.android.gms.location.LocationListener**;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //restart location updates with the new interval
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLocationChanged(Location location) {
       if(currLocSet==0)
        getDeviceLocation();
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    public AlertDialog.Builder getDialogProgressBar() {

        if (loadingBuilder == null) {
            loadingBuilder = new AlertDialog.Builder(this);

            loadingBuilder.setTitle("Loading...");

            final ProgressBar progressBar = new ProgressBar(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            loadingBuilder.setView(progressBar);
        }
        return loadingBuilder;

    }
}
