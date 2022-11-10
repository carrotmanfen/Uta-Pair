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

        CheckBox accessibilityCheckBox = findViewById(R.id.accessibility_checkbox);
        CheckBox blindModeCheckBox = (CheckBox) findViewById(R.id.blind_mode_checkbox);

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
        accessibilityCheckBox.setChecked(checkedAccessibilityMode);
        accessibilityCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkedSoundClick = ((CheckBox) view).isChecked();

                if(checkedSoundClick){
                    AccessibilityMode.getInstance().setMode("ACCESSIBILITY");
                }
                else {
                    AccessibilityMode.getInstance().setMode("NOT_ACCESSIBILITY");
                }
                switch(view.getId()) {
                    case R.id.accessibility_checkbox:
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putBoolean("ACCESSIBILITY_CHECKBOX", checkedSoundClick).commit();
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