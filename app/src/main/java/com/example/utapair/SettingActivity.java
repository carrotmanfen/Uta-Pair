package com.example.utapair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

public class SettingActivity extends AppCompatActivity {
    private ImageButton buttonProfile;
    private ImageButton buttonScoreboard;
    private ImageButton backButton;
    //private CheckBox blindModeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backButton = findViewById(R.id.backward_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

        CheckBox soundClickCheckBox = findViewById(R.id.sound_click_checkbox);
        CheckBox blindModeCheckBox = (CheckBox) findViewById(R.id.blind_mode_checkbox);

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
        soundClickCheckBox.setChecked(checkedSoundClick);
        soundClickCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkedSoundClick = ((CheckBox) view).isChecked();

                if(checkedSoundClick){
                    SoundClickMode.getInstance().setMode("SOUND");
                }
                else {
                    SoundClickMode.getInstance().setMode("NOT_SOUND");
                }
                switch(view.getId()) {
                    case R.id.sound_click_checkbox:
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putBoolean("SOUND_CHECKBOX", checkedSoundClick).commit();
                        break;
                }
            }
        });
        blindModeCheckBox.setChecked(checkedBlindMode);
        blindModeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean checkedBlindMode = ((CheckBox) view).isChecked();

                if(checkedBlindMode){
                    BlindMode.getInstance().setMode("BLIND");
                }
                else{
                    BlindMode.getInstance().setMode("NOT_BLIND");
                }

                switch(view.getId()) {
                    case R.id.blind_mode_checkbox:
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putBoolean("BLIND_CHECKBOX", checkedBlindMode).commit();
                        break;
                }
            }
        });
    }

    public void openAccountActivity(){
        Intent intent=new Intent(this, AccountActivity.class);
        finish();
        startActivity(intent);
    }

    public void openScoreboardActivity(){
        Intent intent=new Intent(this, ScoreboardActivity.class);
        finish();
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {
    }
}