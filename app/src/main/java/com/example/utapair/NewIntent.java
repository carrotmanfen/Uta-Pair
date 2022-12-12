package com.example.utapair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import java.util.Map;
import java.util.Set;

public class NewIntent {
    public static int tapCount;

    /* this method is create new intent */
    public static void launchActivity(Class<? extends Activity> nextActivityClass, Activity currentActivity) {
        Intent launchIntent = new Intent(currentActivity, nextActivityClass);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        currentActivity.startActivity(launchIntent);
        currentActivity.finish();
    }

    /* this method is create new intent and have inputExtra */
    public static void launchActivity(Class<? extends Activity> nextActivityClass, Activity currentActivity, Map<String, Integer> extrasMap) {
        Intent launchIntent = new Intent(currentActivity, nextActivityClass);
        if (extrasMap != null && extrasMap.size() > 0) {
            Set<String> keys = extrasMap.keySet();
            for (String key : keys) {
                launchIntent.putExtra(key, extrasMap.get(key));
            }
        }
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        currentActivity.startActivity(launchIntent);
        currentActivity.finish();
    }

    /* this method is create new intent and have inputExtra */
    public static void launchActivityNotFinish(Class<? extends Activity> nextActivityClass, Activity currentActivity, Map<String, Integer> extrasMap) {
        Intent launchIntent = new Intent(currentActivity, nextActivityClass);
        if (extrasMap != null && extrasMap.size() > 0) {
            Set<String> keys = extrasMap.keySet();
            for (String key : keys) {
                launchIntent.putExtra(key, extrasMap.get(key));
            }
        }
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        currentActivity.startActivity(launchIntent);
    }

    /* this method is create new intent with accessibility mode */
    public static void launchActivityAccessibility(Class<? extends Activity> nextActivityClass, Activity currentActivity
            ,TextToSpeech textToSpeech,String text , int delay){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start Activity */
                else if(tapCount==2){
                    textToSpeech.stop();
                    launchActivity(nextActivityClass,currentActivity);
                }
                tapCount = 0;   /* reset tapCount */
            }
        },delay);     /* in millisecond */
    }

    /* this method is create new intent and have inputExtra with accessibility mode */
    public static void launchActivityAccessibility(Class<? extends Activity> nextActivityClass, Activity currentActivity, Map<String, Integer> extrasMap
            ,TextToSpeech textToSpeech,String text , int delay){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start Activity */
                else if(tapCount==2){
                    textToSpeech.stop();
                    launchActivity(nextActivityClass,currentActivity,extrasMap);
                }
                tapCount = 0;   /* reset tapCount */
            }
        },delay);     /* in millisecond */
    }

    /* this method is create new intent and have inputExtra with accessibility mode */
    public static void launchActivityAccessibilityNotFinish(Class<? extends Activity> nextActivityClass, Activity currentActivity, Map<String, Integer> extrasMap
            ,TextToSpeech textToSpeech,String text , int delay){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start Activity */
                else if(tapCount==2){
                    textToSpeech.stop();
                    launchActivityNotFinish(nextActivityClass,currentActivity,extrasMap);
                }
                tapCount = 0;   /* reset tapCount */
            }
        },delay);     /* in millisecond */
    }

}
