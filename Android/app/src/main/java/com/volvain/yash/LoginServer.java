package com.volvain.yash;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class LoginServer extends Worker {
    Context context;
    public LoginServer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Long id=getInputData().getLong("id",-1);
        String password=getInputData().getString("password");
        if(new Server(context).login(id,password)){
        return Result.success();}
        return Result.failure();
    }
}
