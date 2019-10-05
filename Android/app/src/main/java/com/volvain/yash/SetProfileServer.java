package com.volvain.yash;

import android.content.Context;

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
        String profession=getInputData().getString("profession");
        String professionDesc=getInputData().getString("professionDesc");
        int i=new Server(context).setProfile(id,profession,professionDesc);
        if(i==0)return Result.failure();
        if(i==1)return Result.success();
        return null;
    }
}
