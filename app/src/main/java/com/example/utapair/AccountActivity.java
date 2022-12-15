package com.example.utapair;

import static com.example.utapair.R.raw.sc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Locale;

/* This file is about AccountActivity
 * it keep all method that use
 * in Account manage page */
public class AccountActivity extends AppCompatActivity {
    private Button buttonRegister;
    private Button buttonLogin;
    private ImageButton buttonScoreboard;
    private ImageButton buttonSetting;
    private ImageButton buttonBack;
    private TextToSpeech textToSpeech;
    private SoundClick soundClick;
    private int tapCount = 0;
    private boolean accessibilityMode;
    @Override
    /* this part will run when create this Activity */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);      /* set layout file */

        /* set mode from share preference */
        accessibilityMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("ACCESSIBILITY_MODE",false);

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

        /* set buttonRegister */
        buttonRegister = findViewById(R.id.register_btn);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonRegister start RegisterActivity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use NewIntent.openNextActivity to create Intent and start Activity follow AccessibilityMode and pass argument that need */
                NewIntent.openNextActivity(RegisterActivity.class,AccountActivity.this,textToSpeech,"double tap to go to register",500,accessibilityMode);
            }
        });

        /* set buttonLogin */
        buttonLogin = findViewById(R.id.login_btn);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonLogin start LoginActivity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use NewIntent.openNextActivity to create Intent and start Activity follow AccessibilityMode and pass argument that need */
                NewIntent.openNextActivity(LoginActivity.class,AccountActivity.this,textToSpeech,"double tap to go to login",500,accessibilityMode);
            }
        });

        /* set buttonBack */
        buttonBack = findViewById(R.id.backward_btn);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click button go to previous activity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use NewIntent.openNextActivity to create Intent and start Activity follow AccessibilityMode and pass argument that need */
                NewIntent.openNextActivity(MainActivity.class,AccountActivity.this,textToSpeech,"double tap to go back",500,accessibilityMode);
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
                NewIntent.openNextActivity(ScoreboardActivity.class,AccountActivity.this,textToSpeech,"double tap to go to scoreboard",500,accessibilityMode);
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
                NewIntent.openNextActivity(SettingActivity.class,AccountActivity.this,textToSpeech,"double tap to go to setting",500,accessibilityMode);
            }
        });
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
                    String text = "Account";
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

    /* method when pres back button */
    @Override
    public void onBackPressed() {
        NewIntent.launchActivity(MainActivity.class, this);
    }

}