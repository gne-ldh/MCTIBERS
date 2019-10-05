package com.volvain.yash;

import android.content.Context;

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
        Long id=new Database(context).getId();
        String result=new Server(context).getUserLoc(id);
        try {
            ArrayList lst=(ArrayList) new JSONParser().parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
