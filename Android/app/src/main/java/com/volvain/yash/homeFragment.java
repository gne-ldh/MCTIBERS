package com.volvain.yash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.volvain.yash.DAO.Database;


public class homeFragment extends Fragment {
    Button helpButton;
    TextView userName;
    Button logoutButton;
    EditText RequestMessagetf;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, null);
        helpButton = v.findViewById(R.id.helpButton);
        logoutButton = v.findViewById(R.id.logoutButton);
        RequestMessagetf = v.findViewById(R.id.RequestMessage);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        helpButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        userName = (TextView) v.findViewById(R.id.userName);
        userName.setText("Hi " + new Database(this.getContext()).getName());
        return v;


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendRequest() {
        if (Global.checkInternet() == 0) {
            Database db = new Database(getContext());
            if (db.checkId()) {
                String[] message = {RequestMessagetf.getText().toString()};
                Home home=((Home)getActivity());
                home.c=HelpSync.class;
                home.args=message;
                home.checkPermissions(HelpSync.class,message);

                //startActivity(new Intent(this.getContext(), HelpSync.class).putExtra("message", message));
            } else {
                Fragment fragment = new loginFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                Toast.makeText(this.getContext(), "Please Login First", Toast.LENGTH_LONG);
            }
        } else
            Toast.makeText(this.getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
    }

    private void logout() {
        Database obj = new Database(this.getContext());
        obj.logout();
        Intent i = new Intent(this.getContext(), Home.class);
        startActivity(i);
    }
}

