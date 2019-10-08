package com.volvain.yash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
        Long id=new Database(context).getId();
        String result=new Server(context).getUserLoc(id);
        try {if(!result.equals("")){
            ArrayList lst=(ArrayList) new JSONParser().parse(result);
            new Database(context).insertLngLng(lst);
            return Result.success();}

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }
}
