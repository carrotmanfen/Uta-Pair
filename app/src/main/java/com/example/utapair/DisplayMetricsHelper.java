package com.example.utapair;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DisplayMetricsHelper {

    public static int getWidthScreen(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if(activity != null){
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
        return  displayMetrics.widthPixels;
    }
    public static int getHeightScreen(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if(activity != null){
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
        return  displayMetrics.heightPixels;
    }

    public static double getScreenSize(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double xdpi = Math.pow(getWidthScreen(activity)/displayMetrics.xdpi,2);
        double ydpi = Math.pow(getHeightScreen(activity)/displayMetrics.ydpi,2);
        double screenSize = Math.sqrt(xdpi + ydpi);
        return (double) Math.round(screenSize * 10) / 10;
    }
}
