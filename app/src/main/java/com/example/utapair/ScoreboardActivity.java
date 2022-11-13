package com.example.utapair;

import static java.lang.String.valueOf;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
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
    private ImageButton buttonBack;
    private CheckBox buttonCheckbox;
    private String buttonLevel;
    private TextToSpeech textToSpeech;
    private int tapCount = 0;
    private String URL = "https://fc55-202-28-7-117.ap.ngrok.io/RegisterLogin/scoreboard.php";

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

        /* set checkbox for BlindMode */
        buttonCheckbox = findViewById(R.id.blind_mode_checkbox);

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
        buttonBack = findViewById(R.id.backward_btn);
        buttonBack.setOnClickListener(new View.OnClickListener() {
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

    /* method to start AccountActivity */
    public void openAccountActivity(){
        /* create new intent AccountActivity Class and Start Activity */
        Intent intent=new Intent(this, AccountActivity.class);
        finish();       /* finish this Activity */
        startActivity(intent);
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
                    String text = "double tap to go to profile";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
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

    /* method to add data from database */
    public void setUserInfo(String buttonLevel){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    scoreboardUserList.clear();     /* clear data */
                    JSONArray products = new JSONArray(response);
                    for(int i=0;i<products.length();i++){   /* dor loop to collect data from database */
                        JSONObject productobject = products.getJSONObject(i);
                        String username = productobject.getString("username");
                        String endTime = productobject.getString("endTime");
                        scoreboardUserList.add(new ScoreboardUser(i+1,username,endTime));       /* add data from database */
                        setAdapter();       /* show in recyclerView */
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
                data.put("Level", buttonLevel);     /* put level to database */
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
        String level = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),level,Toast.LENGTH_SHORT).show();
        /* set button level follow mode and level to put this data in to database */
        if(buttonCheckbox.isChecked()){
            if(level.equals("EASY")){
                buttonLevel="MAL01";
            }
            else if(level.equals("NORMAL")){
                buttonLevel="MAL02";
            }
            else if(level.equals("HARD")){
                buttonLevel="MAL03";
            }
        }
        else{
            if(level.equals("EASY")){
                buttonLevel="MAL04";
            }
            else if(level.equals("NORMAL")){
                buttonLevel="MAL05";
            }
            else if(level.equals("HARD")){
                buttonLevel="MAL06";
            }
        }
        /* collect data from database */
        setUserInfo(buttonLevel);
    }

    @Override
    /* implement because implement interface */
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

}