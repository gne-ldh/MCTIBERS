package com.volvain.yash;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private ProgressBar loginPB;
    private Context context;
    private AlertDialog.Builder loadingBuilder;
    private AlertDialog loading;
    loginFragment(){

    }
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
        loginPB=(ProgressBar)v.findViewById(R.id.loginPB);
        loginPB.setVisibility(View.GONE);
        loading =getDialogProgressBar().create();
         context=this.getContext();
        return v;


    }
    public void login(){

        if(Global.checkInternet()==0)
        {

            WorkManager.getInstance().cancelAllWork();//TODO add to logout
     final Long id=Long.parseLong(idField.getText().toString());
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
                        //    Toast.makeText(loginFragment.this.getContext(), "Processing!", Toast.LENGTH_LONG).show();
                           // loginPB.setVisibility(View.VISIBLE);
                            loading.show();
                        else if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                           // loginPB.setVisibility(View.GONE);
                            loading.dismiss();
                            Toast.makeText(loginFragment.this.getContext(),"Login Sucessful!",Toast.LENGTH_LONG).show();
                            getLoc();

                            BackgroundWork.sync();
                        }
                        else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                           // loginPB.setVisibility(View.GONE);
                            loading.dismiss();
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
    private void getLoc(){

        if(Global.checkInternet()==0){
             Log.i("userData","1");
            OneTimeWorkRequest work=new OneTimeWorkRequest.Builder(GetUserLocServer.class)
                    .build();
            Log.i("userData","2");
            WorkManager.getInstance().enqueue(work);
            Log.i("userData","3");
            WorkManager.getInstance().getWorkInfoByIdLiveData(work.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                //Toast.makeText(loginFragment.this.getContext(),"Locations Update Sucessful!",Toast.LENGTH_LONG).show();
                                //loginPB.setVisibility(View.GONE);
                                loading.dismiss();
                                Toast.makeText(loginFragment.this.getContext(),"Locations Update Sucessful!",Toast.LENGTH_LONG).show();
                                openHome();
                            }
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING||workInfo.getState() == WorkInfo.State.ENQUEUED)
                               //Toast.makeText(loginFragment.this.getContext(), "Processing!", Toast.LENGTH_LONG).show();
                            {loading.show();
                                //loginPB.setVisibility(View.VISIBLE);
                                }
                            else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                                loading.dismiss();
                              //  loginPB.setVisibility(View.GONE);
                                Toast.makeText(loginFragment.this.getContext(),"Error Adding Locations",Toast.LENGTH_LONG).show();
                                errorGettingLoc();
                            }
                        }
                    });}
        else Toast.makeText(this.getContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
    }
    private void progress(){
        ProgressBar p=new ProgressBar(context);
        p.setIndeterminate(true);
        AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setView(p);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
private void errorGettingLoc(){

    AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.MyDialogTheme);
    builder.setMessage("In order to Receieve Help Request \nPlease Add Locations \nWhere We can Reach to you")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(context,PinLocation.class);
                    context.startActivity(i);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(context,"You Won't be Receiving Help Requests",Toast.LENGTH_LONG).show();}
    });

    AlertDialog dialog=builder.create();
    dialog.show();
}

    public AlertDialog.Builder getDialogProgressBar() {

        if (loadingBuilder == null) {
            loadingBuilder = new AlertDialog.Builder(this.getContext());

            loadingBuilder.setTitle("Loading...");

            final ProgressBar progressBar = new ProgressBar(this.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            loadingBuilder.setView(progressBar);
        }
        return loadingBuilder;

}
}
