package com.volvain.yash;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.volvain.yash.DAO.Database;


public class SyncServer extends Worker {
    Context context;
    public SyncServer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }




    @NonNull
    @Override
    public Result doWork() {
        if(Global.checkInternet()==0) {
            Database db = new Database(context);
            Long id = db.getId();
            new Server(context).sync(id);
        }

        return Result.retry();
    }
}
