package com.volvain.yash;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SetProfileServer extends Worker {
Context context;
    public SetProfileServer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Long id=0l;//TODO get id
        String profession=getInputData().getString("profession");
        String professionDesc=getInputData().getString("professionDesc");
        new Server(context).setProfile(id,profession,professionDesc);
        return null;
    }
}
