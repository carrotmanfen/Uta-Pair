package com.example.utapair;

import static com.example.utapair.R.raw.sc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.telephony.PreciseDataConnectionState;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.utapair.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.Locale;

/* This file is about MainActivity
* it keep all method that use
* in Home page */
public class MainActivity extends AppCompatActivity {
    private Button buttonPlay;
    private ImageButton buttonProfile;
    private ImageButton buttonScoreboard;
    private ImageButton buttonSetting;
    private TextToSpeech textToSpeech;
    private SoundClick soundClick;
    private int tapCount = 0;
    private boolean firstStart;
    SharedPreferences sh;
    @Override
    /* this part will run when create this Activity */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     /* set layout file */
        soundClick= new SoundClick(this);
        /* create object textToSpeak and set the language */
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                /* if init success set language in US */
                if (i != TextToSpeech.ERROR)
                    textToSpeech.setLanguage(Locale.US);
            }
        });
        /* set SharedPreference */
        sh = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        firstStart =sh.getBoolean("FIRST_USE_PREF",true);
        /* set buttonPlay */
        buttonPlay = (Button) findViewById(R.id.play_btn);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonPlay start SelectLevelActivity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openSelectLevelActivityAccessibility();
                }
                else{
                    openSelectLevelActivity();
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
                    if (AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY")
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

        /* set buttonSetting */
        buttonSetting = (ImageButton) findViewById(R.id.setting_btn);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonSetting start SettingActivity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                    openSettingActivityAccessibility();
                }
                else {
                    openSettingActivity();
                }
            }
        });
        if(firstStart){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String text = "All setting is on and you change it on bottom right of the app";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    startFirstDialog();
                    startFirstSetting();
                }
            },500);     /* in 500 millisecond */
        }
        else{
            /* set all setting in first page */
            settingAll();
        }
    }

    /* this part will run when this Activity start */
    protected void onStart() {
        super.onStart();
        /* if AccessibilityMode on when this activity start play sound */
        if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String text = "Home";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }, 500);
        }

    }

    /* method to set all setting */
    public void settingAll(){
        boolean checkedMusicMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("MUSIC_CHECKBOX",false);    /* get MusicMode from SharedPreferences */
        boolean checkedAccessibilityMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("ACCESSIBILITY_CHECKBOX",false);    /* get AccessibilityMode from SharedPreferences */
        boolean checkedBlindMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("BLIND_CHECKBOX", false);   /* get BlindMode from SharedPreference */
        /* set MusicMode */
        if(checkedMusicMode){
            MusicMode.getInstance().setMode("MUSIC");
        }
        else{
            MusicMode.getInstance().setMode("NOT_MUSIC");
        }
        /* set AccessibilityMode */
        if(checkedAccessibilityMode){
            AccessibilityMode.getInstance().setMode("ACCESSIBILITY");
        }
        else{
            AccessibilityMode.getInstance().setMode("NOT_ACCESSIBILITY");
        }
        /* set BlindMode */
        if(checkedBlindMode){
            BlindMode.getInstance().setMode("BLIND");
        }
        else{
            BlindMode.getInstance().setMode("NOT_BLIND");
        }
    }

    /* method to start SelectLevelActivity */
    public void openSelectLevelActivity(){
        /* create new intent SelectLevelActivity Class and Start Activity */
        Intent intent = new Intent(this, SelectLevelActivity.class);
        startActivity(intent);
    }

    /* method to start SelectLevelActivity with AccessibilityMode */
    public void openSelectLevelActivityAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to play";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start SelectLevelActivity */
                else if(tapCount==2){
                    openSelectLevelActivity();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    /* method to start AccountActivity */
    public void openAccountActivity(){
        /* if user has logged in then go to Profile page */
        if(sh.contains("SAVED_NAME")){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        /* if user hasn't logged in then go to account page */
        else {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }
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
                    String text = "double tap to go to Account";
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
        finish();       /* finish this Activity */
        startActivity(intent);
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
                /* if double tap in time start AccountActivity */
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
        Intent intent = new Intent(this, ScoreboardActivity.class);
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

    /* method to start SettingActivity */
    public void openSettingActivity(){
        /* create new intent SettingActivity Class and Start Activity */
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    /* method to start SettingActivity with AccessibilityMode */
    public void openSettingActivityAccessibility(){
        tapCount++; /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to go to setting";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start SettingActivity */
                else if(tapCount==2){
                    openSettingActivity();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }
    private int checkLoginData(){
        sh = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        if(sh.contains("SAVED_NAME")){
            return 1 ;
        }
        else{
            return 0;
        }

    }
    /* This function use to appear alert dialog for first time use */
    private void startFirstDialog() {
        new AlertDialog.Builder(this)
                .setTitle("One time Dialog") /* set title */
                .setMessage("All setting is on and you change it on " +
                        "bottom right of the app")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        soundClick.playSoundClick(); /* sound click */
                        String text = "Home";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
        SharedPreferences.Editor shEditor = sh.edit();
        shEditor.putBoolean("FIRST_USE_PREF",false);
        shEditor.apply();

    }
    /* this function set all setting to checked */
    private void startFirstSetting() {
        MusicMode.getInstance().setMode("MUSIC");
        AccessibilityMode.getInstance().setMode("ACCESSIBILITY");
        BlindMode.getInstance().setMode("BLIND");
    }

}