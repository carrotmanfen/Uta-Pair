package com.example.utapair;

import static com.example.utapair.R.raw.sc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.Locale;

/* This file is about SettingActivity
 * it keep all method that use
 * in Setting page */
public class SettingActivity extends AppCompatActivity {
    private ImageButton buttonProfile;
    private ImageButton buttonScoreboard;
    private ImageButton buttonHome;
    private TextToSpeech textToSpeech;
    private CheckBox checkBoxMusicMode;
    private CheckBox checkBoxAccessibilityMode;
    private CheckBox checkBoxBlindMode;
    private SoundClick soundClick;
    private int tapCount = 0;
    SharedPreferences sh;
    @Override
    /* this part will run when create this Activity */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);      /* set layout file */
        soundClick = new SoundClick(this);
        /* create object textToSpeak and set the language */
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                /* if init success set language in US */
                if (i != TextToSpeech.ERROR)
                    textToSpeech.setLanguage(Locale.US);
            }
        });

        /* set buttonBack */
        buttonHome = findViewById(R.id.home_btn);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click button go to previous activity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    onBackPressedAccessibility();
                }
                else {
                    onBackPressed();        /* go to previous activity */
                }
            }
        });

        /* set buttonProfile */
        buttonProfile = (ImageButton) findViewById(R.id.profile_btn);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonProfile start AccountActivity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if (checkLoginData()==1) {
                    if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY")

                        openProfileActivityAccessibility();
                    else
                        openProfileActivity();
                }
                else {
                    if (AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY")
                        openAccountActivityAccessibility();
                    else
                        openAccountActivity();
                    }
                }
        });

        /* set buttonScoreboard */
        buttonScoreboard = (ImageButton) findViewById(R.id.scoreboard_btn);
        buttonScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonScoreboard start ScoreboardActivity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openScoreboardActivityAccessibility();
                }
                else {
                    openScoreboardActivity();
                }
            }
        });

        /* set checkBoxMusicMode */
        checkBoxMusicMode = findViewById(R.id.music_checkbox);
        checkBoxMusicMode.setChecked(MusicMode.getInstance().getMode()=="MUSIC");       /* set state checkbox */
        checkBoxMusicMode.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click CheckBox set MusicMode */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use setMusicMode method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    setMusicModeAccessibility();
                }
                else {
                    setMusicMode();
                }
            }
        });

        /* set checkBoxAccessibilityMode */
        checkBoxAccessibilityMode = findViewById(R.id.accessibility_checkbox);
        checkBoxAccessibilityMode.setChecked(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY");       /* set state checkbox */
        checkBoxAccessibilityMode.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click Checkbox set AccessibilityMode */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use setAccessibilityMode method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                   setAccessibilityModeAccessibility();
                }
                else {
                    setAccessibilityMode();
                }
            }
        });

        /* set checkBoxBlindMode */
        checkBoxBlindMode = (CheckBox) findViewById(R.id.blind_mode_checkbox);
        checkBoxBlindMode.setChecked(BlindMode.getInstance().getMode()=="BLIND");       /* set state checkbox */
        checkBoxBlindMode.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click Checkbox set AccessibilityMode */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use setBlindMode method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    setBlindModeAccessibility();
                }
                else {
                    setBlindMode();
                }
            }
        });

        /* set MusicMode to shared preference */
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putBoolean("MUSIC_CHECKBOX", checkBoxMusicMode.isChecked()).commit();

        /* set BlindMode to shared preference */
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putBoolean("BLIND_CHECKBOX", checkBoxBlindMode.isChecked()).commit();


        /* set AccessibilityMode to shared preference */
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putBoolean("ACCESSIBILITY_CHECKBOX", checkBoxAccessibilityMode.isChecked()).commit();
    }

    /* this part will run when this Activity start */
    protected void onStart() {
        super.onStart();
        checkBoxAccessibilityMode = findViewById(R.id.accessibility_checkbox);
        /* if AccessibilityMode on when this activity start play sound */
        if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String text = "setting";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }, 500);
        }
    }

    /* method set MusicMode and share preference */
    public void setMusicMode(){
        /* set MusicMode follow Checkbox */
        if(checkBoxMusicMode.isChecked()){
            MusicMode.getInstance().setMode("MUSIC");
        }
        else{
            MusicMode.getInstance().setMode("NOT_MUSIC");
        }
        /* set MusicMode to shared preference */
        switch(checkBoxMusicMode.getId()) {
            case R.id.music_checkbox:
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("MUSIC_CHECKBOX", checkBoxMusicMode.isChecked()).commit();
                break;
        }
    }
    /* method set MusicMode and share preference with AccessibilityMode */
    public void setMusicModeAccessibility(){
        /* keep previous state of checkbox */
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String text;
                /* if a tap play sound */
                if (tapCount==1){
                    /* keep state of checkbox */
                    if(checkBoxMusicMode.isChecked()) {
                        checkBoxMusicMode.setChecked(false);     /* set checkbox to uncheck */
                        text = "double tap to set music mode on";
                    }
                    else{
                        checkBoxMusicMode.setChecked(true);     /* set checkbox to check */
                        text = "double tap to set music mode off";
                    }
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time set MusicMode */
                else if(tapCount==2){
                    /* change state of checkbox */
                    if(checkBoxMusicMode.isChecked()) {
                        checkBoxMusicMode.setChecked(false);     /* set checkbox to uncheck */
                        text = "music mode off";
                    }
                    else{
                        checkBoxMusicMode.setChecked(true);     /* set checkbox to check */
                        text = "music mode on";
                    }
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    setMusicMode();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */

    }

    /* method set BlindMode and share preference */
    public void setBlindMode(){
        /* set BlindMode follow blindModeCheckbox */
        if(checkBoxBlindMode.isChecked()){
            BlindMode.getInstance().setMode("BLIND");
        }
        else{
            BlindMode.getInstance().setMode("NOT_BLIND");
        }
        /* set BlindMode to shared preference */
        switch(checkBoxBlindMode.getId()) {
            case R.id.blind_mode_checkbox:
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("BLIND_CHECKBOX", checkBoxBlindMode.isChecked()).commit();
                break;
        }
    }

    /* method set BlindMode and share preference with AccessibilityMode */
    public void setBlindModeAccessibility(){
        /* keep previous state of checkbox */
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String text;
                /* if a tap play sound */
                if (tapCount==1){
                    /* keep state of checkbox */
                    if(checkBoxBlindMode.isChecked()) {
                        checkBoxBlindMode.setChecked(false);     /* set checkbox to uncheck */
                        text = "double tap to set blind mode on";
                    }
                    else{
                        checkBoxBlindMode.setChecked(true);     /* set checkbox to check */
                        text = "double tap to set blind mode off";
                    }
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time set BlindMode */
                else if(tapCount==2){
                    /* change state of checkbox */
                    if(checkBoxBlindMode.isChecked()) {
                        checkBoxBlindMode.setChecked(false);     /* set checkbox to uncheck */
                        text = "blind mode off";
                    }
                    else{
                        checkBoxBlindMode.setChecked(true);     /* set checkbox to check */
                        text = "blind mode on";
                    }
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    setBlindMode();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */

    }

    /* method set AccessibilityMode and share preference */
    public  void setAccessibilityMode(){
        /* set AccessibilityMode follow Checkbox */
        if(checkBoxAccessibilityMode.isChecked()){
            AccessibilityMode.getInstance().setMode("ACCESSIBILITY");
            String text = "Accessibility mode on";
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
        else {
            AccessibilityMode.getInstance().setMode("NOT_ACCESSIBILITY");
        }
        /* set AccessibilityMode to shared preference */
        switch(checkBoxAccessibilityMode.getId()) {
            case R.id.accessibility_checkbox:
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("ACCESSIBILITY_CHECKBOX", checkBoxAccessibilityMode.isChecked()).commit();
                break;
        }
    }

    /* method to set AccessibilityMode and share preference with AccessibilityMode */
    public void setAccessibilityModeAccessibility(){
        checkBoxAccessibilityMode.setChecked(true);     /* set checkbox to check */
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to set accessibility mode off";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time set AccessibilityMode */
                else if(tapCount==2){
                    checkBoxAccessibilityMode.setChecked(false);        /* set checkbox to uncheck */
                    setAccessibilityMode();
                    String text = "Accessibility mode off";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */

    }

    /* method to start AccountActivity */
    public void openAccountActivity(){
        /* create new intent AccountActivity Class and Start Activity */
            Intent intent=new Intent(this, AccountActivity.class);
            finish();       /* finish this Activity */
            startActivity(intent);
    }

    /* method to start AccountActivity with AccessibilityMode */
    public void openAccountActivityAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to go to account";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start AccountActivity */
                else if(tapCount==2){
                    openAccountActivity();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */

    }
    /* method to start ProfileActivity */
    public void openProfileActivity(){
        /* create new intent ProfileActivity Class and Start Activity */
            Intent intent=new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();       /* finish this Activity */
    }

    /* method to start ProfileActivity with AccessibilityMode */
    public void openProfileActivityAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to go to profile";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start ProfileActivity */
                else if(tapCount==2){
                    openProfileActivity();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */

    }

    /* method to start ScoreboardActivity */
    public void openScoreboardActivity(){
        /* create new intent ScoreboardActivity Class and Start Activity */
        Intent intent=new Intent(this, ScoreboardActivity.class);
        finish();       /* finish this Activity */
        startActivity(intent);
    }

    /* method to start ScoreboardActivity with AccessibilityMode */
    public void openScoreboardActivityAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to go to scoreboard";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start ScoreboardActivity */
                else if(tapCount==2){
                    openScoreboardActivity();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    /* method to go to previous activity with AccessibilityMode */
    public void onBackPressedAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to go back";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start GameActivity */
                else if(tapCount==2){
                    onBackPressed();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }
    /* method to check if user is logged in */
    public int checkLoginData(){
        /* check the data in sharedPreference */
        sh = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        if(sh.contains("SAVED_NAME")){
            return 1 ; /* If have data in string key "SAVED_NAME"then return 1 */
        }
        else{
            return 0; /* If don't have data in string key "SAVED_NAME" then return 0 */
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        finishAffinity();
        startActivity(intent);
    }

}