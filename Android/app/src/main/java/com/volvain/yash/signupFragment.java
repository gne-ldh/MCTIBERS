package com.volvain.yash;

import android.app.AlertDialog;
import android.os.Bundle;
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

public class signupFragment extends Fragment {
    EditText nameField;
    EditText phoneField;
    EditText passwordField;
    EditText confirmPasswordField;
    Button submitButton;
    private AlertDialog.Builder loadingBuilder;
    private AlertDialog loading;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.fragment_signup,null);
        nameField=(EditText) v.findViewById(R.id.name);
        phoneField=(EditText)v.findViewById(R.id.phone);
        passwordField=(EditText)v.findViewById(R.id.loginPassword);
        confirmPasswordField=(EditText)v.findViewById(R.id.confirmPassword);
        submitButton=(Button)v.findViewById(R.id.register);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        loading=getDialogProgressBar().create();
       return v;
    }
    public void signup(){
        if(Global.checkInternet()==0) {
            String password = passwordField.getText().toString();
            Long phone = Long.parseLong(phoneField.getText().toString());
            if (password.equals(confirmPasswordField.getText().toString())) {
                Data data = new Data.Builder()
                        .putString("password", password)
                        .putString("name", nameField.getText().toString())
                        .putLong("phone", phone)
                        .build();

                OneTimeWorkRequest Work =
                        new OneTimeWorkRequest.Builder(Signup_Server.class)
                                .setInputData(data)
                                // .setInputData(Name)
                                //.setInputData(Phone)
                                .build();
                WorkManager.getInstance().enqueue(Work);
                WorkManager.getInstance().getWorkInfoByIdLiveData(Work.getId())
                        .observe(this, new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(@Nullable WorkInfo workInfo) {
                                if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING||workInfo.getState() == WorkInfo.State.ENQUEUED)
                                    //Toast.makeText(signupFragment.this.getContext(), "Processing!", Toast.LENGTH_LONG).show();
                                    loading.show();

                               else if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                    {loading.dismiss();
                                        Toast.makeText(signupFragment.this.getContext(), "Signup Sucessful!\nNow You Can Login to Access Your Account", Toast.LENGTH_LONG).show();

                                        openLogin();}
                                } else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                                    loading.dismiss();
                                    Toast.makeText(signupFragment.this.getContext(), "User Already Exists", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            } else
                Toast.makeText(this.getContext(), "Password Match Incorrect", Toast.LENGTH_LONG).show();
        }
         else Toast.makeText(this.getContext(),"No Internet Connection",Toast.LENGTH_LONG);
    }
    public void openLogin(){
        Fragment fragment = new loginFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
