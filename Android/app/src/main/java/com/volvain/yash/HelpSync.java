package com.volvain.yash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.work.BackoffPolicy;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.volvain.yash.DAO.Database;

import java.util.concurrent.TimeUnit;

public class HelpSync extends AppCompatActivity implements LocationListener {
   private LocationManager locationManager;
    private String provider;
    private Location location;
   static int no=0;
     public int AAAA=0;
    Long id;
    String name;
    Button stop;
    String message;

   protected void onCreate(Bundle savedInstanceState){

       super.onCreate(savedInstanceState);
       Server.serverUri=this.getString(R.string.server);

       setContentView(R.layout.helpframe);
       //message=messageField.getText().toString();//TODO create textfield and use for taking message from user
       message=this.getIntent().getStringExtra("message");
       stop=(Button) findViewById(R.id.stop);
       stop.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               stop();
           }
       });
       fetchPersonalDetails();
   locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);


       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           findLocationProvider();
       }

           onLocationChanged(location);
       }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Toast.makeText(this,"Request no "+no,Toast.LENGTH_LONG).show();
        Log.i("fff","Request no"+no);
        sendRequest(no,id,name,longitude,latitude,message);
       // Toast.makeText(this,"long="+longitude+"latitude=:"+latitude,Toast.LENGTH_LONG).show();
        no++;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findLocationProvider();

        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
       Toast.makeText(this,"Connection Lost Reestablishing Connection",Toast.LENGTH_LONG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findLocationProvider();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void findLocationProvider(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},HelpSync.this.AAAA);
        }
        //TODO implement above check in caller activity

        Criteria criteria=new Criteria();
        provider=locationManager.getBestProvider(criteria,false);
        location = locationManager.getLastKnownLocation(provider);
        Toast.makeText(this,""+location,Toast.LENGTH_LONG).show();
        if(location==null){
            provider=LocationManager.NETWORK_PROVIDER;
            location = locationManager.getLastKnownLocation(provider);

        }
    }
    private void sendRequest(int no,Long id,String name,Double longitude,Double latitude,String message){
       Data data=new Data.Builder()
               .putInt("no",no)
                .putLong("id",id)
                 .putString("name",name)
                .putDouble("longitude",longitude)
               .putDouble("latitude",latitude)
               .putString("message",message)
               .build();
        OneTimeWorkRequest work=new OneTimeWorkRequest.Builder(HelpReqServer.class)
                                .setInputData(data)
                                 .setBackoffCriteria(BackoffPolicy.LINEAR,1, TimeUnit.SECONDS)
                                .build();
        WorkManager.getInstance().enqueue(work);

        WorkManager.getInstance().getWorkInfoByIdLiveData(work.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING||workInfo.getState() == WorkInfo.State.ENQUEUED)
                        Toast.makeText(HelpSync.this, "Processing!", Toast.LENGTH_LONG).show();
                        else if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            if(HelpSync.this.no==0)
                            Toast.makeText(HelpSync.this,"Request Sent!",Toast.LENGTH_LONG).show();
                           else   Toast.makeText(HelpSync.this,"Updating Location !",Toast.LENGTH_LONG).show();
                        }
                        else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                            Toast.makeText(HelpSync.this,"Faild To Send Request",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void fetchPersonalDetails(){

            Database db= new Database(this);
            id=db.getSenderId();
            name=db.getSenderName();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},HelpSync.this.AAAA);
        }
        locationManager.requestLocationUpdates
                (provider, 500, 1, this);
    }
    public void stop(){
       WorkManager.getInstance().cancelAllWork();
       Intent i=new Intent(this,Home.class);
       startActivity(i);
    }
}
