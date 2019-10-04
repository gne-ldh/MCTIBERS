package com.volvain.yash;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SetUserLocServer extends Worker {
    Context context;
    public SetUserLocServer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Long id=0l;
        String userLoc="";
       int result=new Server(context).SendUserLoc(id,userLoc);
       if(result==0)return Result.failure();
        return Result.success();
    }
}
