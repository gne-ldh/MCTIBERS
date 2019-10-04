package com.volvain.yash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
import com.volvain.yash.DAO.Database;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_log_in);
        startActivity(new Intent(this, Home.class));
        Database db= new Database(this);
        //Intent i =new Intent(this,Signup.class);
        //startActivity(i);
      // String s= new Server(this).Signup(9939424667l,"GKM","148");
        //Log.i("******************","$$$$$$$$$$$$$$"+s);
       // Button b=new Button(this);
        //b.setText("Hello");
       // LinearLayout l=(LinearLayout)findViewById(R.id.lay);
        //l.addView(b);
    }


    private static String  Channel_Id="Yash";
    private static String  Channel_Name="Yash";
    private static String  Channel_Desc="Yash notifications";

    public  void getNotification()
    {
        // Intent i=new Intent(this,map.class);
        //   PendingIntent resultPendingIntent=PendingIntent.getActivity(this,1,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,Channel_Id)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("New request")
                .setContentText("help")
                .setAutoCancel(true)
                //.setContentIntent(resultPendingIntent)
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
