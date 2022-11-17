package com.example.utapair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/* this class is about EndgameActivity
* when game is end it will show score
* and you can play again and share */
public class EndgameActivity extends Activity {
    private TextView textViewScoreTime;
    private TextView textViewModeId;
    private Button buttonPlayAgain;
    private Button buttonShare;
    private ImageButton imageButtonHome;
    private ImageButton imageButtonPlayAgain;
    private ImageButton imageButtonShare;
    private TextToSpeech textToSpeech;
    private int tapCount = 0;
    private String[] timeSplit;

    @Override
    /* this part will run when create this Activity */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);     /* menu page file layout */

        /* get score from GameActivity */
        textViewScoreTime = findViewById(R.id.score_time_text);
        Intent receiverIntent = getIntent();
        String receiveValue = receiverIntent.getStringExtra("TIME_SCORE");
        String[] arrOfStr = receiveValue.split(":", 5);
        int[] score;
        score = new int[3];
        for(int i=0;i<arrOfStr.length;i++){
            score[i]=Integer.parseInt(arrOfStr[i]);
            System.out.println(receiveValue);
        }
        int completeScore;
        completeScore = score[2]+score[1]*100+score[0]*6000;
        /* เอา completeScore ไปใส่ใน database ได้เลย */
        System.out.println(receiveValue);
        textViewScoreTime.setText(receiveValue);
        String modeText = setTextMode();
        textViewModeId = findViewById(R.id.mode_text);
        textViewModeId.setText(modeText);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                /* if init success set language in US */
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                    /* if Accessibility Open */
                    if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY") {
                        /* speak score */
                        timeSplit = new String[3];
                        timeSplit = receiveValue.trim().replaceAll("\\s","").split(":");
                        String text = "congratulation all item is matched and your time score is " + timeSplit[0] + " minutes " + timeSplit[1]+ " seconds "+ timeSplit[2] + " milliseconds";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }

            }
        });

        /* share button implements */
        imageButtonShare = findViewById(R.id.share_btn_1);
        imageButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    shareScoreAccessibility();
                }
                else {
                    shareScore();
                }
            }
        });

        /* share button implements */
        buttonShare = findViewById(R.id.share_btn_2);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    shareScoreAccessibility();
                }
                else {
                    shareScore();
                }
            }
        });

        /* imageButtonPlayAgain implement */
        imageButtonPlayAgain = findViewById(R.id.play_again_btn_1);
        imageButtonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    playAgainAccessibility();
                }
                else {
                    playAgain();
                }
            }
        });

        /* ButtonPlayAgain implement */
        buttonPlayAgain = findViewById(R.id.play_again_btn_2);
        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    playAgainAccessibility();
                }
                else {
                    playAgain();
                }
            }
        });

        /* imageButtonHome implement */
        imageButtonHome = findViewById(R.id.home_btn);
        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openMainAccessibility();
                }
                else {
                    openMainActivity();
                }
            }
        });

    }

    /* method to share score */
    public void shareScore(){
        /* share */
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String Body = "UTA is the best app";
        String sub = "Application Link here !!"; /* link of webpage app */
        intent.putExtra(Intent.EXTRA_SUBJECT,Body);
        intent.putExtra(Intent.EXTRA_TEXT,sub);
        startActivity(Intent.createChooser(intent,"Share using"));
    }

    /* method to share score with AccessibilityMode */
    public void shareScoreAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to go to home";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time to share score */
                else if(tapCount==2){
                    shareScore();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    /* method to go to MainActivity */
    public void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    /* method to MainActivity with AccessibilityMode */
    public void openMainAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to go to home";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time go to MainActivity */
                else if(tapCount==2){
                    openMainActivity();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    /* method to playAgain in same level */
    public void playAgain(){
        Intent intent = new Intent(this,GameActivity.class);
        Bundle bundle = getIntent().getExtras();
        int mode = bundle.getInt("MODE");
            if (mode==-1){      /* level easy */
                intent.putExtra("MODE", -1);
                intent.putExtra("LAYOUT_ID", R.layout.activity_game_easy2);
                intent.putExtra("GRID_ID", R.id.GridLayout_easy);
                finish();
                startActivity(intent);
            }
            else if (mode==0) {     /* level normal */
                intent.putExtra("MODE", 0);
                intent.putExtra("LAYOUT_ID", R.layout.activity_game_normal);
                intent.putExtra("GRID_ID", R.id.GridLayout_meduim);
                finish();
                startActivity(intent);
            }
            else if (mode==1) {     /* level hard */
                intent.putExtra("MODE", 1);
                intent.putExtra("LAYOUT_ID", R.layout.activity_game_hard);
                intent.putExtra("GRID_ID", R.id.GridLayout_hard);
                finish();
                startActivity(intent);
            }

    }

    /* method to play again with AccessibilityMode */
    public void playAgainAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to play again";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time to play again */
                else if(tapCount==2){
                    playAgain();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    /* method to set text mode in layout */
    public String setTextMode(){
        Bundle bundle = getIntent().getExtras();
        int mode = bundle.getInt("MODE");
        switch (mode){
            case -1:
                return "LEVEL - EASY";
            case 0:
                return "LEVEL - NORMAL";
            case 1:
                return "LEVEL - HARD";
        }
        return "Error!";
    }

}
