package com.example.utapair;

import static com.example.utapair.R.raw.sc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
    @Override
    /* this part will run when create this Activity */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);      /* set layout file */

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
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openRegisterActivityAccessibility();
                }
                else {
                    openRegisterActivity();
                }
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
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openLoginActivityAccessibility();
                }
                else {
                    openLoginActivity();
                }
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
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
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
                    String text = "Account";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }, 500);
        }
    }

    /* method to start RegisterActivity */
    public void openRegisterActivity(){
        /* create new intent RegisterActivity Class and Start Activity */
        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /* method to start RegisterActivity with AccessibilityMode */
    public void openRegisterActivityAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to go to Register";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start ScoreboardActivity */
                else if(tapCount==2){
                    openRegisterActivity();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    /* method to start LoginActivity */
    public void openLoginActivity(){
        /* create new intent LoginActivity Class and Start Activity */
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /* method to start LoginActivity with AccessibilityMode */
    public void openLoginActivityAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to go to Login";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time start ScoreboardActivity */
                else if(tapCount==2){
                    openLoginActivity();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    /* method to start ScoreboardActivity */
    public void openScoreboardActivity(){
        /* create new intent ScoreboardActivity Class and Start Activity */
        Intent intent=new Intent(this, ScoreboardActivity.class);
        finish();
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
        Intent intent=new Intent(this, SettingActivity.class);
        finish();
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