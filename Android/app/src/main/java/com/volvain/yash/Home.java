package com.volvain.yash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.volvain.yash.DAO.Database;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

FloatingActionButton btn;
 CheckLocation checkLoc;
 Class c=null;
 String[] args=null;
 @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Server.serverUri="https://projectmctibers.appspot.com";
        Database db= new Database(this);
        //TODO if id exists
        super.onCreate(savedInstanceState);
        Server.serverUri=this.getString(R.string.server);



        setContentView(R.layout.activity_main2);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        if(db.checkId()){
            BackgroundWork.sync();
       if(this.getIntent().hasExtra("fragmentNo")){
            if(this.getIntent().getStringExtra("fragmentNo").equals("NotificationFragment"))
                loadFragment(new notificationsFragment());}
        else loadFragment(new homeFragment());}

        else{ loadFragment(new loginFragment());}
        checkLoc=new CheckLocation(this,this);

    }

    private  boolean loadFragment(Fragment fragment){
        if (fragment !=null){
getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment =null;
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment= new homeFragment();
                break;
            case  R.id.navigation_dashboard:

                fragment=new settingsFragment();
                break;
            case R.id.navigation_notifications:

                fragment=new notificationsFragment();
                break;
            case  R.id.login:
                if(new Database(this).getId()!=0l)
                    fragment=new Profile();

               else fragment=new loginFragment();
                break;
        }
        return loadFragment(fragment);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            checkLoc.displayLocationSettingsRequest(c,args);
           // new CheckLocation(this,this,PinLocation.class).displayLocationSettingsRequest();
            //Log.i("checkLoc","here");
            //Intent i=new Intent(this,PinLocation.class);
            //startActivity(i);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermissions(Class c,String... s){
       checkLoc.checkPermission();
    checkLoc.displayLocationSettingsRequest(c,s);}
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 2) {
            checkLoc.checkPermission();
        }

    }
}
