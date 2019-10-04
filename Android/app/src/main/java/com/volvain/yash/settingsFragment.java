package com.volvain.yash;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class settingsFragment extends Fragment {

    FloatingActionButton btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_settings,null);
        btn=v.findViewById(R.id.addLocation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Func();
            }
        });
        return v;
    }
    public void Func(){
        Intent i=new Intent(this.getContext(),PinLocation.class);
        startActivity(i);

    }

}
