package com.example.yummlyteam.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import java.io.InputStream;


public class Util {
    //Should be in a different class, but did not want to create a whole new flag for a singular flag considering the scope of this project
    public static boolean areMocksEnabled = false;

    public static boolean isNetworkConnectionAvailable(Context context) {
        boolean isNetworkConnectionAvailable = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            isNetworkConnectionAvailable = (activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED);
        }
        return isNetworkConnectionAvailable;
    }

    public static String timeFormatter(Integer timeInSeconds) {
        if(timeInSeconds==null) {
            return "--";
        }
        double min = ((double)timeInSeconds) / 60.0;
        if (min >= 60) {
            double hour = min/60;
            return Math.round(hour) + "h";
        }
        return Math.round(min) + "m";
    }

    public static String getJsonFromAssets(String filename) {
        try {
            InputStream inputStream = Util.class.getClassLoader().getResourceAsStream(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
