package com.volvain.yash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {
    EditText name;
    EditText phone;
    EditText password;
    EditText confirmPassword;
    Button submit;
public static final int PASSWORD=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
       // Log.i("hello","hello");
       /* Log.i("MMmmmm",""+"entering");
       Runtime r=Runtime.getRuntime();
        Log.i("MMmmmm",""+"entering");
        try {
         Process p=   r.exec("ping -c 1 google.com");
         int i=p.waitFor();
         Log.i("MMmmmm",""+i);
         //Toast.makeText(this,""+p.waitFor(),Toast.LENGTH_LONG);
        } catch (IOException e) {
            Log.i("MMmmmm",""+"error");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.i("MMmmmm",""+"error");
            e.printStackTrace();
        }*/
        name=(EditText) findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phone);
        password=(EditText)findViewById(R.id.loginPassword);
        confirmPassword=(EditText)findViewById(R.id.confirmPassword);
        submit=(Button)findViewById(R.id.register);

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(Global.checkInternet()==0)
                        signup();
                       else Toast.makeText(Signup.this,"Internet Connection UnAvailable",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public void signup(){
        String pass=password.getText().toString();
        Long Phone =Long.parseLong(phone.getText().toString());
        if(pass.equals(confirmPassword.getText().toString())){
            Data data = new Data.Builder()
                    .putString("password", pass)
                    .putString("name",name.getText().toString())
                    .putLong("phone",Phone)
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
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                Toast.makeText(Signup.this,"Signup Sucessful!",Toast.LENGTH_LONG).show();
                            }
                            else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                                Toast.makeText(Signup.this,"User Already Exists",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else Toast.makeText(this,"Password Match Incorrect",Toast.LENGTH_LONG).show();
    }
}
