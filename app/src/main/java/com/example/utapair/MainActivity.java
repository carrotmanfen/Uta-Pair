package com.example.utapair;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
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

public class MainActivity extends AppCompatActivity {
    private Button buttonPlay;
    private ImageButton buttonProfile;
    private ImageButton buttonScoreboard;
    private ImageButton buttonSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean checkedSoundClick = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("SOUND_CHECKBOX",false);
        boolean checkedBlindMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("BLIND_CHECKBOX", false);
        if(checkedSoundClick){
            SoundClickMode.getInstance().setMode("SOUND");
        }
        else{
            SoundClickMode.getInstance().setMode("NOT_SOUND");
        }
        if(checkedBlindMode){
            BlindMode.getInstance().setMode("BLIND");
        }
        else{
            BlindMode.getInstance().setMode("NOT_BLIND");
        }


        final MediaPlayer buttonSoundClick = MediaPlayer.create(this,R.raw.correct);

        buttonPlay = (Button) findViewById(R.id.play_btn);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SoundClickMode.getInstance().getMode()=="SOUND") {
                    buttonSoundClick.start();
                }
                openSelectLevelActivity();
            }
        });

        buttonProfile = (ImageButton) findViewById(R.id.profile_btn);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountActivity();
            }
        });

        buttonScoreboard = (ImageButton) findViewById(R.id.scoreboard_btn);
        buttonScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScoreboardActivity();
            }
        });

        buttonSetting = (ImageButton) findViewById(R.id.setting_btn);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingActivity();
            }
        });
    }

    public void openSelectLevelActivity(){
        Intent intent=new Intent(this, SelectLevelActivity.class);
        startActivity(intent);
    }

    public void openAccountActivity(){
        Intent intent=new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void openScoreboardActivity(){
        Intent intent=new Intent(this, ScoreboardActivity.class);
        startActivity(intent);
    }

    public void openSettingActivity(){
        Intent intent=new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

}