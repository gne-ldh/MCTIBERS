package com.volvain.yash;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class Signup_Server extends Worker {

    Context context;
    public Signup_Server(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Long phone= (Long) getInputData().getLong("phone",1l);
        String name= getInputData().getString("name");
        String password=getInputData().getString("password");
        String message=new Server(context).Signup(phone,name,password);
        //String message=new Server(context).Signup(9939424666l,"Gaurav","123");
        Data out=new Data.Builder()
                .putString("message",message)
                .build();
        if(message.equals("User Added Successfully")){
           // Toast.makeText(context,""+message,Toast.LENGTH_LONG);
        return Result.success(out);}
        else {
            //Toast.makeText(context,""+message,Toast.LENGTH_LONG);
            return Result.failure(out);
        }
    }

}

