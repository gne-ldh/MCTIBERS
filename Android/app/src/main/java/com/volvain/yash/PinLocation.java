package com.volvain.yash;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
   // public static ArrayList<Double> list = new ArrayList<>();

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
       // Log.i("gauravrmsc","clearing list");
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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();
                //ArrayList<Double> list = new ArrayList<>();
                //list.add();
               // list.add(marker.getPosition().latitude);
                for(ArrayList<Double> lst:ListLocations){

                 //   Log.i("ana","before"+ListLocations.size());

                if(lst.get(0).equals(marker.getPosition().longitude)&&lst.get(1).equals(marker.getPosition().latitude)){
                    ListLocations.remove(ListLocations.indexOf(lst));

                  //  Log.i("ana","after"+ListLocations.size());
                    break;}}

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
        getPinnedLocations();
        String LocationName = sv.getQuery().toString() + ",India";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> AddressList = geocoder.getFromLocationName(LocationName, 1);
        if (AddressList.size() > 0) {
            Address address = AddressList.get(0);
            ArrayList<Double> list = new ArrayList<>();
            list.add(0, address.getLongitude());
            list.add(1, address.getLatitude());
            ListLocations.add(list);
            //  Toast.makeText(this, "location added in list", Toast.LENGTH_SHORT).show();
           /* if (db.getTableSize() == 1) {
                Toast.makeText(this, "list" + list.get(0), Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));
                moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 15, "" + address);
            }*/

           /* for (int i = 1; i < db.getTableSize(); i--) {
                Toast.makeText(this, "inside for", Toast.LENGTH_SHORT).show();
                ArrayList<Double> ls = getPinnedLocations();
                if (!ls.equals(list)) {
                    Toast.makeText(this, "inside if", Toast.LENGTH_SHORT).show();
                    ListLocations.add(list);
                    Toast.makeText(this, "list" + list.get(0), Toast.LENGTH_SHORT).show();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));
                    moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 15, "" + address);
                    getPinnedLocations();
                } else {
                    Toast.makeText(this, "Already Pinned", Toast.LENGTH_SHORT).show();
                }*/
            Log.i( "ana","before" +ListLocations.isEmpty() );

            for (ArrayList firstList:ListLocations){
                Log.i( "ana","before111" +firstList.isEmpty() );
                if(firstList.isEmpty())return;
                LatLng loc=new LatLng((Double)firstList.get(1),(Double) firstList.get(0));
                Log.i( "ana","lat" +firstList.get(1));
                Log.i( "ana","lng" +firstList.get(0));
                 mMap.addMarker(new MarkerOptions().position(loc));
                moveCamera(loc,15,"");
            }
        }
    }




    public void getPinnedLocations() {

      //  ArrayList<ArrayList<Double>> loc = new ArrayList<>();
        Log.i( "ana","inside1" +ListLocations.isEmpty() );
    //ArrayList<Double> lc = new ArrayList<>();
    //  Database db= new Database(this);
        Log.i( "ana","inside" +ListLocations.isEmpty() );
    //double lat = db.getLat();
    //double lng = db.getLng();
        /*if(lng!=0)
            lc.add(lng);

        if(lat!=0)
            lc.add(lat);*/
        ArrayList lst=db.latlng();
        Log.i( "ana","sizeList" +lst.isEmpty() );
        if(lst!=null)ListLocations=lst;
       // Log.i( "ana","sizeList" +lc.get(1) );
        //ListLocations.add(lc);
        //Log.i( "ana","sizeList" +ListLocations.isEmpty() );
     //   Toast.makeText(this, "sizeList" +loc.size() , Toast.LENGTH_SHORT).show();
       // return ListLocations;
}
    public void addToDatabase(){

    //    if(loginFragment.ID!=0)
           db.clearLocations();
        Log.i( "ana","database clear" +ListLocations.isEmpty() );
           db.insertLngLng(ListLocations);
        Log.i( "ana","insert" +ListLocations.isEmpty() );
        //TODO store LatLng in database
    }

}
