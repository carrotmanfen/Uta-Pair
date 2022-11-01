package com.example.utapair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AccountActivity extends AppCompatActivity {

    Button regisbtn,loginbtn;
    private ImageButton buttonScoreboard;
    private ImageButton buttonSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        regisbtn = findViewById(R.id.register_btn);
        loginbtn = findViewById(R.id.login_btn);
        regisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActivity();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
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
    public void openRegisterActivity(){
        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    public void openLoginActivity(){
        Intent intent=new Intent(this, LoginActivity.class);
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