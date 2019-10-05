package com.volvain.yash;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.volvain.yash.DAO.Database;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class GetUserLocServer extends Worker {
    Context context;
    public GetUserLocServer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("userData","3");
        Long id=new Database(context).getId();
        String result=new Server(context).getUserLoc(id);
        try {if(!result.equals("")){
            ArrayList lst=(ArrayList) new JSONParser().parse(result);
            Log.i("datalistusr",lst.toString());
            new Database(context).insertLngLng(lst);
            Log.i("datalistusr",lst.toString());
            return Result.success();}

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }
}
