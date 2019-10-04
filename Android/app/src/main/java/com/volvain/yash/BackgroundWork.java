package com.volvain.yash;

import android.util.Log;

import androidx.work.BackoffPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class BackgroundWork {

    public static void sync(){
       // Log.i("gauravrmsc","backgriound sync called");
        PeriodicWorkRequest req=new PeriodicWorkRequest.Builder(SyncServer.class,10, TimeUnit.SECONDS)
                            .setBackoffCriteria(BackoffPolicy.LINEAR,1,TimeUnit.SECONDS)
                             .build();
        WorkManager.getInstance().enqueue(req);
    }

}
