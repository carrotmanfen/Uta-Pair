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
    private boolean blindMode,accessibilityMode,musicMode;
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
                /* use NewIntent.openNextActivity to create Intent and start Activity follow AccessibilityMode and pass argument that need */
                NewIntent.openNextActivity(SelectLevelActivity.class,MainActivity.this,textToSpeech,"double tap to play",500,accessibilityMode);
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
                    NewIntent.openNextActivity(ProfileActivity.class,MainActivity.this,textToSpeech,"double tap to go to Profile",500,accessibilityMode);
                }
                else{
                    /* use NewIntent.openNextActivity to create Intent and start Activity follow AccessibilityMode and pass argument that need */
                    NewIntent.openNextActivity(AccountActivity.class,MainActivity.this,textToSpeech,"double tap to go to Account",500,accessibilityMode);
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
                NewIntent.openNextActivity(ScoreboardActivity.class,MainActivity.this,textToSpeech,"double tap to go to Scoreboard",500,accessibilityMode);

            }
        });

        /* set buttonSetting */
        buttonSetting = (ImageButton) findViewById(R.id.setting_btn);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonSetting start SettingActivity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use NewIntent.openNextActivity to create Intent and start Activity follow AccessibilityMode and pass argument that need */
                NewIntent.openNextActivity(SettingActivity.class,MainActivity.this,textToSpeech,"double tap to go to setting",500,accessibilityMode);
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
        if(accessibilityMode) {
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
    /* this part is about when exit this activity */
    protected void onDestroy() {
        /* when destroy shutdown and turn off everything */
        super.onDestroy();
        textToSpeech.shutdown();
        soundClick.stopMediaPlayer();
        soundClick.releaseMediaPlayer();
    }

    /* method to set all setting */
    public void settingAll(){
        /* set mode from share preference */
        blindMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("BLIND_MODE",false);
        accessibilityMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("ACCESSIBILITY_MODE",false);
        musicMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("MUSIC_MODE",false);
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
        /* set all mode on */
        musicMode=true;
        accessibilityMode=true;
        blindMode=true;
        /* save mode in share preference */
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("MUSIC_MODE", true).commit();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("ACCESSIBILITY_MODE", true).commit();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("BLIND_MODE", true).commit();
    }

}