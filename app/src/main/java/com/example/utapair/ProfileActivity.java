package com.example.utapair;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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
/* this class is about ProfileActivity
* that user can see about name and their score */
public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /* Declare variables */
    private ArrayList<ProfileUser> profileUserList;
    private RecyclerView recyclerView;
    private ImageButton editProfileButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popupEditText;
    private Button popupCancelButton , popupConfirmButton ;
    private ImageButton buttonScoreboard;
    private ImageButton buttonSetting;
    private ImageButton buttonBack;
    private String saveName,newUsername,buttonLevel;
    private int checkChange;
    private TextView textViewProfileName;
    private CheckBox buttonCheckbox;
    private TextToSpeech textToSpeech;
    private int tapCount = 0;
    private int sdCount = 0;
    private String textLevel;
    Button buttonLogout;
    SharedPreferences sh;
    SharedPreferences.Editor editor;

    /* Connect Server */
    private String newNameURL = "https://ffc8-2001-fb1-b3-7432-95f5-7962-a3fc-37b5.ap.ngrok.io/RegisterLogin/checkNewName.php";
    private String scoreboardURL = "https://ffc8-2001-fb1-b3-7432-95f5-7962-a3fc-37b5.ap.ngrok.io/RegisterLogin/scoreboardProfile.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* create object textToSpeak and set the language */
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                /* if init success set language in US */
                if (i != TextToSpeech.ERROR)
                    textToSpeech.setLanguage(Locale.US);
            }
        });

        sh = getSharedPreferences("MYSHAREDPREF", Context.MODE_PRIVATE);
        editor = sh.edit();
        saveName = sh.getString("SAVED_NAME","");
        textViewProfileName = findViewById(R.id.changename);
        textViewProfileName.setText(saveName);

        /* set checkbox for BlindMode */
        buttonCheckbox = findViewById(R.id.modeblind_checkbox);
        buttonCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                    if (buttonCheckbox.isChecked()) {
                        String text = "Checked mode blind";
                        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                        showScore();
                    }
                    else {
                        String text = "Checked off mode blind";
                        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                        showScore();
                    }
                }
            }
        });

        /* set buttonLogin */
        buttonLogout = findViewById(R.id.logout_btn);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    logoutAccessibility();
                }
                else {
                    logout();
                }

            }
        });

        Spinner spinner = findViewById(R.id.level_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.level,R.layout.spinner_text_select);
        adapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        recyclerView = findViewById(R.id.profile_recycler_view);

        profileUserList = new ArrayList<>();

        editProfileButton = findViewById(R.id.editname_btn);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Use function Editname() */
                EditName();
            }
        });

        /* set buttonScoreboard */
        buttonScoreboard = (ImageButton) findViewById(R.id.scoreboard_btn);
        buttonScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonScoreboard start ScoreboardActivity */
            public void onClick(View view) {
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
                    String text = "profile";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }, 500);
        }
    }

    /* method to logout of user */
    public void logout(){
        editor.clear(); /* Clear data in sh SharedPreference */
        editor.commit(); /* Commit change to sh SharedPreference */
        /* sh SharedPreference doesn't clear the data */
        if(sh.contains("SAVED_NAME")){
            /* Pop up logout unsuccessfully */
            Toast.makeText(ProfileActivity.this, "Logout unsuccessfully", Toast.LENGTH_SHORT).show();
        }
        /* sh SharedPreference data is cleared */
        else {
            /* Pop up logout successfully */
            Toast.makeText(ProfileActivity.this, "Logout Successfully ", Toast.LENGTH_SHORT).show();
            /* Navigate to MainActivity page */
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /* method to logout with AccessibilityMode */
    public void logoutAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to logout";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time logout */
                else if(tapCount==2){
                    logout();
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
    /* This function use to pop up interface for user to editname */
    public void EditName(){
        /* Declare variables */
        dialogBuilder = new AlertDialog.Builder(this,R.style.dialog);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_edit_name,null);
        popupEditText = popupView.findViewById(R.id.new_name_edittext);
        popupEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String text = "type new name";
                textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                return false;
            }
        });
        popupCancelButton = popupView.findViewById(R.id.cancel_popup_btn);
        popupConfirmButton = popupView.findViewById(R.id.confirm_popup_btn);
        sh = getSharedPreferences("MYSHAREDPREF", Context.MODE_PRIVATE);
        editor = sh.edit();
        saveName = sh.getString("SAVED_NAME","");
        popupEditText.setText(saveName);
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();      /* show popup */
        if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
            String text = "edit profile name";
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
        popupConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                    tapCount++; /* when tap button count in tapCount */
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /* if a tap play sound */
                            if (tapCount==1){
                                String text = "double tap to confirm new name";
                                textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                            }
                            /* if double tap in time change name */
                            else if(tapCount==2){
                                /* get new username input from text box */
                                newUsername = popupEditText.getText().toString();
                                /* if the new input is match with the username that user use to logged in */
                                if(newUsername.equals(saveName)){
                                    Toast.makeText(ProfileActivity.this, " nothing change ",Toast.LENGTH_SHORT).show();
                                    checkChange = 0;
                                    sayFailed();
                                }
                                /* if the new input is more than available size in database */
                                else if (newUsername.length()>16){
                                    Toast.makeText(ProfileActivity.this," cannot have username more than 16 character  ",Toast.LENGTH_SHORT).show();
                                    checkChange = 0;
                                    sayFailed();
                                }
                                /* if the new input is empty */
                                else if (newUsername.length()==0){
                                    Toast.makeText(ProfileActivity.this," username cannot be empty ",Toast.LENGTH_SHORT).show();
                                    checkChange = 0;
                                    sayFailed();
                                }
                                /* the new input is possible to change the username */
                                else {
                                    changeUsername();
                                }
                                dialog.dismiss();
                            }
                            tapCount = 0;   /* reset tapCount */
                        }
                    },500);     /* in 500 millisecond */
                }
                else{
                    /* get new username input from text box */
                    newUsername = popupEditText.getText().toString();
                    /* if the new input is match with the username that user use to logged in */
                    if(newUsername.equals(saveName)){
                        Toast.makeText(ProfileActivity.this, " nothing change ",Toast.LENGTH_SHORT).show();
                        checkChange = 0;
                    }
                    /* if the new input is more than available size in database */
                    else if (newUsername.length()>16){
                        Toast.makeText(ProfileActivity.this," cannot have username more than 16 character  ",Toast.LENGTH_SHORT).show();
                        checkChange = 0;
                    }
                    /* if the new input is empty */
                    else if (newUsername.length()==0){
                        Toast.makeText(ProfileActivity.this," username cannot be empty ",Toast.LENGTH_SHORT).show();
                        checkChange = 0;
                    }
                    /* the new input is possible to change the username */
                    else {
                        changeUsername();
                    }
                    dialog.dismiss();
                }

            }
        });

        popupCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    String text = "change name cancel";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
                dialog.dismiss();
            }
        });

    }

    private void setAdapter() {
        ProfileRecyclerAdapter profileRecyclerAdapter = new ProfileRecyclerAdapter(profileUserList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(profileRecyclerAdapter);
    }



    private void setUserInfo(String buttonLevel) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, scoreboardURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /* If response from database is FAILURE */
                if(response.equals("FAILURE")){
                    profileUserList.clear();    /* clear data */
                    setAdapter();   /* show in recyclerView */
                    Toast.makeText(ProfileActivity.this, "Don't have data", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        profileUserList.clear();     /* clear data */
                        JSONArray products = new JSONArray(response);
                        for(int i=0;i<products.length();i++){   /* dor loop to collect data from database */
                            JSONObject productobject = products.getJSONObject(i);
                            Integer row_index = productobject.getInt("row_index");
                            String endTime = productobject.getString("endTime");
                            profileUserList.add(new ProfileUser(i+1,row_index,endTime));       /* add data from database */
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
                Toast.makeText(ProfileActivity.this, "Server error. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            /* get data that use in database */
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("Level", buttonLevel);     /* put level to database */
               if(checkChange==1){
                    data.put("username", newUsername);      /* put username to database */
               }
                else{
                   data.put("username", saveName);
               }
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    /* method to speak failed register */
    public void sayFailed(){
        if (AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
            String text = "change name failed";
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    /* method to show score */
    public void showScore(){
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
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void onCheckboxClicked(View view) {
    }
    @Override
    public void onBackPressed(){
        /* If still have logged in data */
        if(sh.contains("SAVED_NAME")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();}
        else{
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void changeUsername(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newNameURL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                /* If the php response is ABLESUCCESS */
                if (response.equals("ABLESUCCESS")) {
                    editor.putString("SAVED_NAME",newUsername); /* Put the new input into SharedPreferences sh */
                    editor.commit(); /* Commit SharedPreferences sh change */
                    textViewProfileName.setText(newUsername); /* put the new username into interface in profile page */
                    popupEditText.setText(newUsername); /* put the new username into text box on edit name pop up */
                    Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show(); /* Pop up Success */
                    checkChange =1;
                    if (AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                        String text = "change name success";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                /* If the php response is ABLEFAILURE */
                else if (response.equals("ABLEFAILURE")) {
                    checkChange = 0;
                    /* Pop up Something wrong!. Please try again later */
                    Toast.makeText(ProfileActivity.this, "Something wrong!. Please try again later", Toast.LENGTH_SHORT).show();
                    sayFailed();
                }
                /* If the php response is EXIST */
                else if (response.equals("EXIST")) {
                    checkChange =0;
                    /* Pop up This username used by someone else */
                    Toast.makeText(ProfileActivity.this," This username used by someone else  ",Toast.LENGTH_SHORT).show();
                    sayFailed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkChange =0;
                /* When the error on response "Server pop up error. Please try again later" */
                Toast.makeText(ProfileActivity.this, "Server pop up error. Please try again later", Toast.LENGTH_LONG).show();

            }
        }) { /* Pass data to php file */
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("NEW_USERNAME", newUsername);
                data.put("OLD_USERNAME", saveName);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

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