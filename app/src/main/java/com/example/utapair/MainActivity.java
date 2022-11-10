package com.example.utapair;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button buttonPlay;
    private ImageButton buttonProfile;
    private ImageButton buttonScoreboard;
    private ImageButton buttonSetting;
    private TextToSpeech textToSpeak;
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingAll();

        textToSpeak = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    textToSpeak.setLanguage(Locale.US);
            }
        });

        buttonPlay = (Button) findViewById(R.id.play_btn);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openSelectLevelActivityAccessibility();
                }
                else{
                    openSelectLevelActivity();
                }
            }
        });

        buttonProfile = (ImageButton) findViewById(R.id.profile_btn);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openAccountActivityAccessibility();
                }
                else{
                    openAccountActivity();
                }
            }
        });

        buttonScoreboard = (ImageButton) findViewById(R.id.scoreboard_btn);
        buttonScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openScoreboardActivityAccessibility();
                }
                else {
                    openScoreboardActivity();
                }
            }
        });

        buttonSetting = (ImageButton) findViewById(R.id.setting_btn);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                    openSettingActivityAccessibility();
                }
                else {
                    openSettingActivity();
                }
            }
        });
    }

    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String text = "Home";
                textToSpeak.speak(text,TextToSpeech.QUEUE_FLUSH,null);
            }
        },500);
    }

    public void settingAll(){
        boolean checkedAccessibilityMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("ACCESSIBILITY_CHECKBOX",false);
        boolean checkedBlindMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("BLIND_CHECKBOX", false);
        if(checkedAccessibilityMode){
            AccessibilityMode.getInstance().setMode("ACCESSIBILITY");
        }
        else{
            AccessibilityMode.getInstance().setMode("NOT_ACCESSIBILITY");
        }
        if(checkedBlindMode){
            BlindMode.getInstance().setMode("BLIND");
        }
        else{
            BlindMode.getInstance().setMode("NOT_BLIND");
        }
    }

    public void openSelectLevelActivity(){
        Intent intent=new Intent(this, SelectLevelActivity.class);
        startActivity(intent);
    }

    public void openSelectLevelActivityAccessibility(){
        i++;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i==1){
                    String text = "double tap to play";
                    textToSpeak.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                else if(i==2){
                    openSelectLevelActivity();
                }
                i=0;
            }
        },500);
    }

    public void openAccountActivity(){
        Intent intent=new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void openAccountActivityAccessibility(){
        i++;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i==1){
                    String text = "double tap to go to profile";
                    textToSpeak.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                else if(i==2){
                    openAccountActivity();
                }
                i=0;
            }
        },500);
    }

    public void openScoreboardActivity(){
        Intent intent=new Intent(this, ScoreboardActivity.class);
        startActivity(intent);
    }

    public void openScoreboardActivityAccessibility(){
        i++;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i==1){
                    String text = "double tap to go to scoreboard";
                    textToSpeak.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                else if(i==2){
                    openScoreboardActivity();
                }
                i=0;
            }
        },500);
    }

    public void openSettingActivity(){
        Intent intent=new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void openSettingActivityAccessibility(){
        i++;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i==1){
                    String text = "double tap to go to setting";
                    textToSpeak.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                else if(i==2){
                    openSettingActivity();
                }
                i=0;
            }
        },500);
    }

}