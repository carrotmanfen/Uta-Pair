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
    private boolean blindMode,accessibilityMode,musicMode;
    SharedPreferences sh;
    @Override
    /* this part will run when create this Activity */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);      /* set layout file */

        /* set mode from share preference */
        blindMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("BLIND_MODE",false);
        accessibilityMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("ACCESSIBILITY_MODE",false);
        musicMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("MUSIC_MODE",false);

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
                /* use NewIntent.openNextActivity to create Intent and start Activity follow AccessibilityMode and pass argument that need */
                NewIntent.openNextActivity(MainActivity.class,SettingActivity.this,textToSpeech,"double tap to go back",500,accessibilityMode);
            }
        });

        /* set buttonProfile */
        buttonProfile = (ImageButton) findViewById(R.id.profile_btn);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonProfile start AccountActivity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                if(sh.contains("SAVED_NAME")){  /* if login go to profile otherwise go to account */
                    /* use NewIntent.openNextActivity to create Intent and start Activity follow AccessibilityMode and pass argument that need */
                    NewIntent.openNextActivity(ProfileActivity.class,SettingActivity.this,textToSpeech,"double tap to go to Profile",500,accessibilityMode);
                }
                else{
                    /* use NewIntent.openNextActivity to create Intent and start Activity follow AccessibilityMode and pass argument that need */
                    NewIntent.openNextActivity(AccountActivity.class,SettingActivity.this,textToSpeech,"double tap to go to Account",500,accessibilityMode);
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
                /* use NewIntent.openNextActivity to create Intent and start Activity follow AccessibilityMode and pass argument that need */
                NewIntent.openNextActivity(ScoreboardActivity.class,SettingActivity.this,textToSpeech,"double tap to go to scoreboard",500,accessibilityMode);
            }
        });

        /* set checkBoxMusicMode */
        checkBoxMusicMode = findViewById(R.id.music_checkbox);
        checkBoxMusicMode.setChecked(musicMode);       /* set state checkbox */
        checkBoxMusicMode.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click CheckBox set MusicMode */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use setMusicMode method follow AccessibilityMode */
                if(accessibilityMode) {
                    setMusicModeAccessibility();
                }
                else {
                    setMusicMode();
                }
            }
        });

        /* set checkBoxAccessibilityMode */
        checkBoxAccessibilityMode = findViewById(R.id.accessibility_checkbox);
        checkBoxAccessibilityMode.setChecked(accessibilityMode);       /* set state checkbox */
        checkBoxAccessibilityMode.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click Checkbox set AccessibilityMode */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use setAccessibilityMode method follow AccessibilityMode */
                if(accessibilityMode) {
                   setAccessibilityModeAccessibility();
                }
                else {
                    setAccessibilityMode();
                }
            }
        });

        /* set checkBoxBlindMode */
        checkBoxBlindMode = (CheckBox) findViewById(R.id.blind_mode_checkbox);
        checkBoxBlindMode.setChecked(blindMode);       /* set state checkbox */
        checkBoxBlindMode.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click Checkbox set AccessibilityMode */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use setBlindMode method follow AccessibilityMode */
                if(accessibilityMode) {
                    setBlindModeAccessibility();
                }
                else {
                    setBlindMode();
                }
            }
        });

        /*  this 3  line is setting remember mode when open for first time */

        /* set MusicMode to shared preference */
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putBoolean("MUSIC_MODE", checkBoxMusicMode.isChecked()).commit();

        /* set BlindMode to shared preference */
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putBoolean("BLIND_MODE", checkBoxBlindMode.isChecked()).commit();


        /* set AccessibilityMode to shared preference */
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putBoolean("ACCESSIBILITY_MODE", checkBoxAccessibilityMode.isChecked()).commit();
    }

    /* this part will run when this Activity start */
    protected void onStart() {
        super.onStart();
        checkBoxAccessibilityMode = findViewById(R.id.accessibility_checkbox);
        /* if AccessibilityMode on when this activity start play sound */
        if(accessibilityMode) {
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
    /* this part is about when exit this activity */
    protected void onDestroy() {
        /* when destroy shutdown and turn off everything */
        super.onDestroy();
        textToSpeech.shutdown();
        soundClick.stopMediaPlayer();
        soundClick.releaseMediaPlayer();
    }

    /* method set MusicMode and share preference */
    public void setMusicMode(){
        /* set MusicMode to shared preference */
        switch(checkBoxMusicMode.getId()) {
            case R.id.music_checkbox:
                musicMode=checkBoxMusicMode.isChecked();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("MUSIC_MODE", checkBoxMusicMode.isChecked()).commit();
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
        /* set BlindMode to shared preference */
        switch(checkBoxBlindMode.getId()) {
            case R.id.blind_mode_checkbox:
                blindMode=checkBoxBlindMode.isChecked();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("BLIND_MODE", checkBoxBlindMode.isChecked()).commit();
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
            String text = "Accessibility mode on";
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
        /* set AccessibilityMode to shared preference */
        switch(checkBoxAccessibilityMode.getId()) {
            case R.id.accessibility_checkbox:
                accessibilityMode=checkBoxAccessibilityMode.isChecked();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("ACCESSIBILITY_MODE", checkBoxAccessibilityMode.isChecked()).commit();
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
        NewIntent.launchActivity(MainActivity.class, this);
    }

}