package com.volvain.yash;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class HelpReqServer extends Worker {
    Context context;
    public HelpReqServer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }
    @NonNull
    @Override
    public Result doWork() {
        int m=-1;
        int n=getInputData().getInt("no",-1);
        if(n==0){
        Long id=getInputData().getLong("id",-1);
        String name=getInputData().getString("name");
        Double longitude=getInputData().getDouble("longitude",360);
        Double latitude=getInputData().getDouble("latitude",360);
        String message=getInputData().getString("message");
        if(!(id==-1||longitude==360||latitude==360)){

             m=new Server(context).firstHelpRequest(id,name,longitude,latitude,message);
            Data out=new Data.Builder()
                    .putInt("message",m)
                    .build();

            if(m==1) {

                return Result.success(out);}
            else return Result.retry();
        }
         }
        else{
            Long id=getInputData().getLong("id",-1);
            Double longitude=getInputData().getDouble("longitude",360);
            Double latitude=getInputData().getDouble("latitude",360);
            if(!(id==-1||longitude==360||latitude==360)){
                m=new Server(context).subsequentHelpRequest(id,longitude,latitude);
                Data out=new Data.Builder()
                        .putInt("message",m)
                        .build();
                if(m==1) return Result.success(out);
                else return Result.retry();
            }
        }
     return Result.retry();
    }
}
