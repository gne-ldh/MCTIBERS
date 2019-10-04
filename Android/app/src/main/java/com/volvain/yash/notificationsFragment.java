package com.volvain.yash;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.volvain.yash.DAO.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.WorkManager;

import com.volvain.yash.DAO.Database;

import static com.volvain.yash.DAO.Database.TableHelp;

public class notificationsFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View  v=inflater.inflate(R.layout.fragment_notifications,null);
      LinearLayout notificationContainer=(LinearLayout)v.findViewById(R.id.notificationContainer);



        Database db= new Database(this.getContext());
        Cursor rs=db.getCursor();
        while(rs.moveToNext())
        {
                    Long id = rs.getLong(0);
                    String name = rs.getString(1);
                    String message=rs.getString(4);
                    notificationContainer.addView(addNotification(id, name,message));

        }


        //TODO Fetch id and name from help table
      return v;
    }
    private Button addNotification(Long id,String name,String message){
        final Button  notificationButton=new Button(this.getContext());
        notificationButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        notificationButton.setText(name+" needs help\n\nMessage "+message+"\n\ncontact : "+id);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b=(Button)v;
                String text1=b.getText().toString();
                Long id=Long.parseLong(new StringBuffer(new StringBuffer(text1).reverse().substring(0,10)).reverse().toString());
                Log.i("gauravrmsc","a");
                WorkManager.getInstance().cancelAllWork();
               Intent intent=new Intent(notificationsFragment.this.getContext(),MapsActivity.class);
               intent.putExtra("id",id);
                Log.i("gauravrmsc","a1"+id);
               startActivity(intent);
               //TODO Get id from here for search
            }
        });
       return notificationButton;
    }

}
