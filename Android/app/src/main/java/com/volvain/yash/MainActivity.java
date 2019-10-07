package com.volvain.yash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.volvain.yash.DAO.Database;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_log_in);
        startActivity(new Intent(this, Home.class));
        Database db= new Database(this);

    }


    private static String  Channel_Id="Yash";
    private static String  Channel_Name="Yash";
    private static String  Channel_Desc="Yash notifications";

    public  void getNotification()
    {

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,Channel_Id)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("New request")
                .setContentText("help")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat manager=  NotificationManagerCompat.from(this);
        manager.notify(1,builder.build());


      /*  if (isServicesOk()) {
          //  init();

        }
    }*/

 /*   public boolean isServicesOk() {

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            //Everything is fine anf the user can make map request

            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it

            //   Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            // dialog.show();
        }
        else {
            Toast.makeText(this, "You can't make request ", Toast.LENGTH_SHORT).show();
        }
        return false;
    }*/
}}
