package com.volvain.yash;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class Global {
    public static int checkInternet(){
        int i=1;
        Runtime r=Runtime.getRuntime();
        try {
            Process p=   r.exec("ping -c 1 google.com");
             i=p.waitFor();

        } catch (IOException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        return i;
    }
}
