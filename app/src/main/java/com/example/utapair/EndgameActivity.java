package com.example.utapair;

import static com.example.utapair.R.raw.sc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    private MediaPlayer mediaPlayer;
    private SoundClick soundClick;
    private int tapCount = 0;
    private String[] timeSplit;
    private String username;
    SharedPreferences sh;
    private String insertScoreURL = "https://uta-pair-api.herokuapp.com/insertScore.php";
    @Override
    /* this part will run when create this Activity */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);     /* menu page file layout */
        soundClick = new SoundClick(this);
        /* play sound when click */
        mediaPlayer = MediaPlayer.create(this, R.raw.correct);
        mediaPlayer.start();
        sh = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        username = sh.getString("SAVED_NAME","");
        /* get score from GameActivity */
        textViewScoreTime = findViewById(R.id.score_time_text);
        Intent receiverIntent = getIntent();
        String receiveValue = receiverIntent.getStringExtra("TIME_SCORE");
        String[] arrOfStr = receiveValue.split(" : ", 3);

        int[] score;
        score = new int[3];
        for(int i=0;i<arrOfStr.length;i++){
            try {
                score[i] = Integer.valueOf(arrOfStr[i]);
                /*System.out.println(score[i]);*/
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }
        int completeScore;
        completeScore = score[2]+score[1]*100+score[0]*6000;
        System.out.println(completeScore);
        /*Toast.makeText(this,completeScore, Toast.LENGTH_SHORT).show();*/
        /* เอา completeScore ไปใส่ใน database ได้เลย */

        /*System.out.println(receiveValue);*/
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
                    if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"||BlindMode.getInstance().getMode()=="BLIND") {
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
        buttonShare = findViewById(R.id.share_btn);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    shareScoreAccessibility();
                }
                else {
                    shareScore();
                }
            }
        });


        /* ButtonPlayAgain implement */
        buttonPlayAgain = findViewById(R.id.play_again_btn);
        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playSoundClick(); /* sound click */
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
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openMainAccessibility();
                }
                else {
                    openMainActivity();
                }
            }
        });
        if(checkLoginData()==1){
            insertScore(completeScore);
        }
        else;
    }

    /* method to share score */
    public void shareScore(){
        /* share */
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        String Body = "UTA is the best app";
//        String sub = "Application Link here !!"; /* link of webpage app */
//        intent.putExtra(Intent.EXTRA_SUBJECT,Body);
//        intent.putExtra(Intent.EXTRA_TEXT,sub);
//        startActivity(Intent.createChooser(intent,"Share using"));
        ShareScore.getInstance().setContext(this);
        File file = ShareScore.getInstance().saveImage();
        if(file != null){
            ShareScore.getInstance().sharePicture(file);
        }
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
                    String text = "double tap to go share";
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
        mediaPlayer.stop();
        mediaPlayer.release();
        soundClick.stopMediaPlayer();
        soundClick.releaseMediaPlayer();
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

    /* method to check if user is logged in */
    public int checkLoginData(){
        /* check the data in sharedPreference */
        sh = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        if(sh.contains("SAVED_NAME")){
            return 1 ; /* If have data in string key "SAVED_NAME"then return 1 */
        }
        else{
            return 0; /* If don't have data in string key "SAVED_NAME" then return 0 */
        }

    }

    public void insertScore(int score){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertScoreURL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                if (response.equals("CANT_FIND_THIS_USER")) {
                    /*System.out.println("CANT_FIND_THIS_USER");*/
                   /* Toast.makeText(EndgameActivity.this, "Can't find where to insert data", Toast.LENGTH_SHORT).show();*/
                } else if (response.equals("FAILURE")) {
                    /*System.out.println("Failure");*/
                    /*Toast.makeText(EndgameActivity.this, "Something wrong!. Please try again later", Toast.LENGTH_SHORT).show();*/
                } else if (response.equals("SUCCESS")) {
                   /* Toast.makeText(EndgameActivity.this, "Added to database", Toast.LENGTH_SHORT).show();*/
                    /*System.out.println("SUCCESS");*/
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EndgameActivity.this, "Server error. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("USERNAME", username);
                data.put("SCORE", String.valueOf(score));
                if(BlindMode.getInstance().getMode()=="BLIND"){
                    if(setTextMode()=="LEVEL - EASY")
                        data.put("LEVEL", "MAL01");
                    else if (setTextMode()=="LEVEL - NORMAL")
                        data.put("LEVEL", "MAL02");
                    else if(setTextMode()=="LEVEL - HARD")
                        data.put("LEVEL","MAL03");
                }
                else {
                    if(setTextMode()=="LEVEL - EASY")
                        data.put("LEVEL", "MAL04");
                    else if (setTextMode()=="LEVEL - NORMAL")
                        data.put("LEVEL", "MAL05");
                    else if(setTextMode()=="LEVEL - HARD")
                        data.put("LEVEL","MAL06");
                }
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
