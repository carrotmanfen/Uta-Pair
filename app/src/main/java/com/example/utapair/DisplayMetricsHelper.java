package com.example.utapair;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayMetricsHelper extends AppCompatActivity {
    private static DisplayMetricsHelper instance;
    private int width;
    private int height;

    public static DisplayMetricsHelper getInstance(){
        if (instance == null)
            instance = new DisplayMetricsHelper();
        return instance;
    }

    public void setWidthPixels(Context context){
        this.width = context.getResources().getDisplayMetrics().widthPixels;
    }
    public void setHeightPixels(Context context){
        this.height = context.getResources().getDisplayMetrics().heightPixels;
    }

    public int getWidthPixels(){
        return this.width;
    }
    public int getHeightPixels(){
        return this.height;
    }
    /* method for convert dp to pixel */
    public float getPixelsFromDp(int dp, Context context){
        float px = dp * (context.getResources().getDisplayMetrics().densityDpi / 160);
        return  Math.round(px);
    }

}
