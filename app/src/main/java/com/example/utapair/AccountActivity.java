package com.example.utapair;

import static com.example.utapair.R.raw.sc;

import androidx.appcompat.app.AppCompatActivity;

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
                openRegisterActivity();
            }
        });

        /* set buttonLogin */
        buttonLogin = findViewById(R.id.login_btn);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonLogin start LoginActivity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                openLoginActivity();
            }
        });

        /* set buttonBack */
        buttonBack = findViewById(R.id.backward_btn);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click button go to previous activity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(accessibilityMode) {
                    onBackPressedAccessibility();
                }
                else {
                    onBackPressed();        /* go to previous activity */
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
                openScoreboardActivity();
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
                openSettingActivity();
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
        super.onDestroy();
        textToSpeech.shutdown();
        soundClick.stopMediaPlayer();
        soundClick.releaseMediaPlayer();
    }

    /* method to start RegisterActivity */
    public void openRegisterActivity(){
        /* create new intent RegisterActivity Class and Start Activity */
        if(accessibilityMode) {
            NewIntent.launchActivityAccessibility(RegisterActivity.class,this,textToSpeech,"double tap to go to Register",500);
        }
        else{
            NewIntent.launchActivity(RegisterActivity.class, this);
        }
    }

    /* method to start LoginActivity */
    public void openLoginActivity(){
        /* create new intent LoginActivity Class and Start Activity */
        if(accessibilityMode) {
            NewIntent.launchActivityAccessibility(LoginActivity.class,this,textToSpeech,"double tap to go to Login",500);
        }
        else{
            NewIntent.launchActivity(LoginActivity.class, this);
        }
    }

    /* method to start ScoreboardActivity */
    public void openScoreboardActivity(){
        /* create new intent ScoreboardActivity Class and Start Activity */
        if(accessibilityMode){
            NewIntent.launchActivityAccessibility(ScoreboardActivity.class,this,textToSpeech,"double tap to go to Scoreboard",500);
        }
        else {
            NewIntent.launchActivity(ScoreboardActivity.class, this);
        }
    }

    /* method to start SettingActivity */
    public void openSettingActivity(){
        /* create new intent SettingActivity Class and Start Activity */
        if (accessibilityMode){
            NewIntent.launchActivityAccessibility(SettingActivity.class,this,textToSpeech,"double tap to go to setting",500);
        }
        else {
            NewIntent.launchActivity(SettingActivity.class, this);
        }
    }

    @Override
    public void onBackPressed() {
        NewIntent.launchActivity(MainActivity.class, this);
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
}