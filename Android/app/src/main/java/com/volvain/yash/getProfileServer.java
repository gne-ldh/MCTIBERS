package com.volvain.yash;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class getProfileServer extends Worker {
    Context context;
    public getProfileServer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork() {
        //new Server(context).getProfile();
        //TODO get id

        return null;
    }
}
