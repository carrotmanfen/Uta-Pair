package com.example.utapair;

import static java.lang.String.valueOf;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/* this class is about ScoreboardActivity
* that show all score in database
* follow level and mode */
public class ScoreboardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ArrayList<ScoreboardUser> scoreboardUserList;
    private RecyclerView recyclerView;
    private ImageButton buttonProfile;
    private ImageButton buttonSetting;
    private ImageButton buttonHome;
    private CheckBox buttonCheckbox;
    private String saveName,buttonLevel,textLevel,score;
    private TextToSpeech textToSpeech;
    private int tapCount = 0;
    private int sdCount = 0;
    private String scoreboardURL = "https://b4fd-2001-fb1-b0-8f9d-e1c7-b113-b636-154a.ap.ngrok.io/RegisterLogin/scoreboard.php";
    private String bestPlaceURL = "https://b4fd-2001-fb1-b0-8f9d-e1c7-b113-b636-154a.ap.ngrok.io/RegisterLogin/scoreboardShowBestScore.php";
    SharedPreferences sh;
    @Override
    /* this part will run when create this Activity */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard); /* set layout */
        /* set spinner for select level */
        Spinner spinner = findViewById(R.id.level_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.level,R.layout.spinner_text_select);
        adapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        sh = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        saveName = sh.getString("SAVED_NAME","");
        /* set checkbox for BlindMode */
        buttonCheckbox = findViewById(R.id.blind_mode_checkbox);
        buttonCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                    if (buttonCheckbox.isChecked()) {
                        String text = "Checked mode blind";
                        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    }
                    else {
                        String text = "Checked off mode blind";
                        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
                showScore();
            }
        });

        /* for keep data and show in recyclerView */
        recyclerView = findViewById(R.id.scoreboard_recycler_view);

        /* create Arraylist fot keep data in recyclerView */
        scoreboardUserList = new ArrayList<>();

        /* create object textToSpeak and set the language */
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                /* if init success set language in US */
                if (i != TextToSpeech.ERROR)
                    textToSpeech.setLanguage(Locale.US);
            }
        });

        /* set buttonProfile */
        buttonProfile = (ImageButton) findViewById(R.id.profile_btn);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonProfile start AccountActivity */
            public void onClick(View view) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openAccountActivityAccessibility();
                }
                else{
                    openAccountActivity();
                }
            }
        });

        /* set buttonSetting */
        buttonSetting = (ImageButton) findViewById(R.id.setting_btn);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonSetting start SettingActivity */
            public void onClick(View view) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                    openSettingActivityAccessibility();
                }
                else {
                    openSettingActivity();
                }
            }
        });

        /* set buttonBack */
        buttonHome = findViewById(R.id.home_btn);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click button go to previous activity */
            public void onClick(View view) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    onBackPressedAccessibility();
                }
                else {
                    onBackPressed();        /* go to previous activity */
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
            Intent intent = new Intent(this, MainActivity.class);
            finishAffinity();
            startActivity(intent);
    }

    /* this part will run when this Activity start */
    protected void onStart() {
        super.onStart();
        sdCount = 0 ;
        /* if AccessibilityMode on when this activity start play sound */
        if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String text = "Scoreboard";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }, 500);
        }
    }

    /* method to start AccountActivity */
    public void openAccountActivity(){
        /* create new intent AccountActivity Class and Start Activity */
        if(checkLoginData()==1){
            Intent intent=new Intent(this, ProfileActivity.class);
            finish();       /* finish this Activity */
            startActivity(intent);}
        else{
            Intent intent=new Intent(this, AccountActivity.class);
            finish();
            startActivity(intent);

        }
    }

    /* method to start AccountActivity with AccessibilityMode */
    public void openAccountActivityAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    if(checkLoginData()==1) {
                        String text = "double tap to go to profile";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else {
                        String text = "double tap to go to account";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                /* if double tap in time start AccountActivity */
                else if(tapCount==2){
                    openAccountActivity();
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

    /* method to set data in layout */
    private void setAdapter() {
        /* set recyclerView and put data in it with layout */
        ScoreboardRecyclerAdapter scoreboardRecyclerAdapter = new ScoreboardRecyclerAdapter(scoreboardUserList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(scoreboardRecyclerAdapter);
    }

    public void showScore (){
        /* set button level follow mode and level to put this data in to database */
        if(buttonCheckbox.isChecked()){
            if(textLevel.equals("EASY")){
                buttonLevel="MAL01";
            }
            else if(textLevel.equals("NORMAL")){
                buttonLevel="MAL02";
            }
            else if(textLevel.equals("HARD")){
                buttonLevel="MAL03";
            }
        }
        else{
            if(textLevel.equals("EASY")){
                buttonLevel="MAL04";
            }
            else if(textLevel.equals("NORMAL")){
                buttonLevel="MAL05";
            }
            else if(textLevel.equals("HARD")){
                buttonLevel="MAL06";
            }
        }
        /* collect data from database */
        setUserInfo(buttonLevel);
        /* show best place user */
        showBestPlace(buttonLevel);
    }

    /* method to add data from database */
    public void setUserInfo(String buttonLevel){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, scoreboardURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /* If response from database is FAILURE */
                if(response.equals("FAILURE")){
                    scoreboardUserList.clear();     /* clear data */
                    setAdapter();       /* show in recyclerView */
                    Toast.makeText(ScoreboardActivity.this, "Don't have data", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        scoreboardUserList.clear();     /* clear data */
                        JSONArray products = new JSONArray(response);
                            for(int i=0;i<products.length();i++){   /* dor loop to collect data from database */
                                JSONObject productobject = products.getJSONObject(i);
                                String username = productobject.getString("username");
                                Integer endTime = productobject.getInt("endTime");
                                String secord = String.valueOf((endTime/100)%60);
                                String msecord = String.valueOf(endTime%100);
                                System.out.println(secord.length());
                                /* If length of "secord" less 2 (0:0:00) */
                                if(secord.length()<2){
                                    /* If length of "msecord" less 2 (0:0:0) */
                                    if(msecord.length()<2){
                                        score = endTime/6000+":"+"0"+(endTime/100)%60+":"+"0"+endTime%100;
                                    }
                                    /* else length of "msecord" not less 2 (0:0:00) */
                                    else{
                                        score = endTime/6000+":"+"0"+(endTime/100)%60+":"+endTime%100;
                                    }
                                }
                                /* If length of "msecord" less 2 (0:00:0) */
                                else if(msecord.length()<2){
                                    /* If length of "secord" less 2 (0:0:0) */
                                    if(secord.length()<2){
                                        score = endTime/6000+":"+"0"+(endTime/100)%60+":"+"0"+endTime%100;
                                    }
                                    /* else length of "secord" not less 2 (0:00:0) */
                                    else{
                                        score = endTime/6000+":"+(endTime/100)%60+":"+"0"+endTime%100;
                                    }
                                }
                                /* else length of "secord" not less 2 (0:00:00) */
                                else{
                                    score = endTime/6000+":"+(endTime/100)%60+":"+endTime%100;
                                }
                                scoreboardUserList.add(new ScoreboardUser(i+1,username,score));
                                setAdapter();       /* show in recyclerView */
                            }
                    } catch (JSONException e) {
                        e.printStackTrace();    /* if JSON error */
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    /* if error */
                Toast.makeText(ScoreboardActivity.this, "Server error. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            /* get data that use in database */
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("LEVEL", buttonLevel);     /* put level to database */
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    /* when select level */
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        /* change level and show with toast */
        textLevel = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),textLevel,Toast.LENGTH_SHORT).show();
        /* count for do not speak when this activity is start */
        sdCount++;
        if (sdCount>1) {
            /* speak when AccessibilityMode on */
            if (AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY") {
                String text = "Select level " + textLevel;
                textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
            }
        }
        showScore();
    }
    /* method show best place user*/
    public void showBestPlace(String buttonLevel){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bestPlaceURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                        JSONArray products = new JSONArray(response);
                        for(int i=0;i<products.length();i++){   /* dor loop to collect data from database */
                            JSONObject productobject = products.getJSONObject(i);
                            Integer row_index = productobject.getInt("row_index");
                            String text ="Congratulations! your best score is on "+ row_index + "th place.";
                            Toast.makeText(ScoreboardActivity.this,text , Toast.LENGTH_LONG).show();
                            /* If AccessibilityMode on speak and delay more than speak in method onStart */
                            if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        textToSpeech.speak(text,TextToSpeech.QUEUE_ADD,null);
                                    }
                                }, 600);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();    /* if JSON error */
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    /* if error */
                Toast.makeText(ScoreboardActivity.this, "Server error. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            /* get data that use in database */
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("LEVEL", buttonLevel);     /* put level to database */
                data.put("USERNAME", saveName);      /* put username to database */
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    @Override
    /* implement because implement interface */
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private int checkLoginData(){
        sh = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        if(sh.contains("SAVED_NAME")){
            return 1 ;
        }
        else{
            return 0;
        }

    }
}