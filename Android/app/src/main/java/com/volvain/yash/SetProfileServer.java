package com.volvain.yash;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.volvain.yash.DAO.Database;

public class SetProfileServer extends Worker {
Context context;
    public SetProfileServer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;

    }

    @NonNull
    @Override
    public Result doWork() {
        Long id=new Database(context).getId();
        Log.i("gkk","1");
        String profession=getInputData().getString("profession");
        String professionDesc=getInputData().getString("professionDesc");
        Log.i("pro",profession);
        Log.i("pro",professionDesc);
        int i=new Server(context).setProfile(id,profession,professionDesc);
        if(i==0)return Result.failure();
        if(i==1)return Result.success();
        return null;
    }
}
