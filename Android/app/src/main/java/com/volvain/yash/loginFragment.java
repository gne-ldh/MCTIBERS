package com.volvain.yash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class loginFragment extends Fragment {
    private Button createAccountBtn;
    private Button loginBtn;
    private EditText idField;
    private EditText passswordField;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View v= inflater.inflate(R.layout.fragment_login,null);;
        createAccountBtn=(Button)v.findViewById(R.id.createAccount);
        loginBtn=(Button)v.findViewById(R.id.login_btn);
        idField=(EditText) v.findViewById(R.id.loginId);
        passswordField=(EditText)v.findViewById(R.id.loginPassword);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();;
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return v;


    }
    public void login(){
        if(Global.checkInternet()==0)
        {

            WorkManager.getInstance().cancelAllWork();//TODO add to logout
     Long id=Long.parseLong(idField.getText().toString());
     String password=passswordField.getText().toString();
     Data data= new Data.Builder()
               .putLong("id",id).putString("password",password).build();
        OneTimeWorkRequest workRequest=new OneTimeWorkRequest.Builder(LoginServer.class).setInputData(data).build();
        WorkManager.getInstance().enqueue(workRequest);
        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING||workInfo.getState() == WorkInfo.State.ENQUEUED)
                            Toast.makeText(loginFragment.this.getContext(), "Processing!", Toast.LENGTH_LONG).show();
                        else if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            Toast.makeText(loginFragment.this.getContext(),"Login Sucessful!",Toast.LENGTH_LONG).show();
                            openHome();
                            BackgroundWork.sync();
                        }
                        else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                            Toast.makeText(loginFragment.this.getContext(),"Invalid ID or Password",Toast.LENGTH_LONG).show();
                        }
                    }
                });}
         else Toast.makeText(this.getContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
    }
    public void openHome(){
        Fragment fragment = new homeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void openSignup(){
        Fragment fragment = new signupFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
