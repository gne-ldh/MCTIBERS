package com.volvain.yash;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.volvain.yash.DAO.Database;

import java.util.ArrayList;

public class getProfileServer extends Worker {
   static String Profession="";
    static String ProfessionDesc="";
    Context context;
    public getProfileServer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Long id=new Database(context).getId();
        Log.i("gkm","1");
        ArrayList profileDetailse=new Server(context).getProfile(id);
Profession=profileDetailse.get(0).toString();
ProfessionDesc=profileDetailse.get(1).toString();
if(Profession=="")return Result.failure();
        return Result.success();
    }
}
