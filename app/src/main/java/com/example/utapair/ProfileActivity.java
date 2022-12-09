package com.example.utapair;

import static com.example.utapair.R.raw.sc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
    private TextView popupErrorText;
    private Button popupCancelButton , popupConfirmButton ;
    private ImageButton buttonScoreboard;
    private ImageButton buttonSetting;
    private ImageButton buttonHome;
    private String saveName,newUsername,buttonLevel,score;
    private int checkChange;
    private TextView textViewProfileName;
    private CheckBox buttonCheckbox;
    private TextToSpeech textToSpeech;
    private int tapCount = 0;
    private int sdCount = 0;
    private String textLevel;
    private String textBestPlace;
    private Button buttonLogout;
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private SoundClick soundClick;

    /* Connect Server */
    private String newNameURL = "https://uta-pair-api.herokuapp.com/checkNewName.php";
    private String scoreboardURL = "https://uta-pair-api.herokuapp.com/scoreboardProfile.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        soundClick = new SoundClick(this);
        /* create object textToSpeak and set the language */
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                /* if init success set language in US */
                if (i != TextToSpeech.ERROR)
                    textToSpeech.setLanguage(Locale.US);
            }
        });

        sh = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        editor = sh.edit();
        saveName = sh.getString("SAVED_NAME","");
        textViewProfileName = findViewById(R.id.changename);
        textViewProfileName.setText(saveName);

        /* set checkbox for BlindMode */
        buttonCheckbox = findViewById(R.id.modeblind_checkbox);
        buttonCheckbox.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("BLIND_PROFILE",false));
        buttonCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY")
                    if (buttonCheckbox.isChecked()) {
                        String text = "Checked mode blind";
                        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    }
                    else {
                        String text = "Checked off mode blind";
                        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    }
                    switch(buttonCheckbox.getId()) {
                        case R.id.modeblind_checkbox:
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                    .putBoolean("BLIND_PROFILE", buttonCheckbox.isChecked()).commit();
                            break;
                    }
                showScore();
            }
        });
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putBoolean("BLIND_PROFILE", buttonCheckbox.isChecked()).commit();

        /* set buttonLogout */
        buttonLogout = findViewById(R.id.logout_btn);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
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
                soundClick.playSoundClick(); /* sound click */
                /* Use function EditName() */
                EditName();
            }
        });

        /* set buttonScoreboard */
        buttonScoreboard = (ImageButton) findViewById(R.id.scoreboard_btn);
        buttonScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            /* set when click buttonScoreboard start ScoreboardActivity */
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
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
                soundClick.playSoundClick(); /* sound click */
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
                soundClick.playSoundClick(); /* sound click */
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
                    text = "level easy";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
                    if(PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).getBoolean("BLIND_PROFILE",false)){
                        text = "on blind Mode";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
                    }
                }
            }, 500);
        }
    }

    /* method to logout of user */
    public void logout(){
        editor.remove("SAVED_NAME");/* Clear data in sh SharedPreference */
        editor.remove("SAVED_PASSWORD");
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
        textToSpeech.shutdown();
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
        popupErrorText=popupView.findViewById(R.id.new_name_errorText);
        popupEditText = popupView.findViewById(R.id.new_name_edittext);
        if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
            popupEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    String text = "type new name";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    return false;
                }
            });
        }
        popupCancelButton = popupView.findViewById(R.id.cancel_popup_btn);
        popupConfirmButton = popupView.findViewById(R.id.confirm_popup_btn);
        sh = getSharedPreferences("MY_SHARED_PREF",MODE_PRIVATE);
        editor = sh.edit();
        saveName = sh.getString("SAVED_NAME","");
        popupEditText.setText(saveName);
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();      /* show popup */
        popupErrorText.setText("");
        popupEditText.setBackground(getResources().getDrawable(R.drawable.custom_input));
        if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
            String text = "edit profile name";
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
        popupConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
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
                                    /* set red border on the editable text box */
                                    popupEditText.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                                    /* set a error text to nothing change due to same as old username*/
                                    popupErrorText.setText("nothing change due to same as old username");
                                    checkChange = 0;
                                    sayFailed();
                                }
                                /* if the new input is more than available size in database */
                                else if (newUsername.length()>16){
                                    /* set red border on the editable text box */
                                    popupEditText.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                                    /* set a error text to Unable to create username more than 16 character */
                                    popupErrorText.setText("Unable to create username more than 16 character");
                                    checkChange = 0;
                                    sayFailed();
                                }
                                /* if the new input is empty */
                                else if (newUsername.length()==0){
                                    /* set red border on the editable text box */
                                    popupEditText.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                                    /* set a error text to Username can't be empty */
                                    popupErrorText.setText("Username can't be empty");
                                    checkChange = 0;
                                    sayFailed();
                                }
                                /* the new input is possible to change the username */
                                else {
                                    changeUsername();
                                }
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
                        /* set red border on the editable text box */
                        popupEditText.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                        /* set a error text to nothing change due to same as old username */
                        popupErrorText.setText("nothing change due to same as old username");
                       /* Toast.makeText(ProfileActivity.this, " nothing change ",Toast.LENGTH_SHORT).show(); */
                        checkChange = 0;
                    }
                    /* if the new input is more than available size in database */
                    else if (newUsername.length()>16){
                        /* set red border on the editable text box */
                        popupEditText.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                        /* set a error text to Unable to create username more than 16 character */
                        popupErrorText.setText("Unable to create username more than 16 character");
                        /*Toast.makeText(ProfileActivity.this," cannot have username more than 16 character  ",Toast.LENGTH_SHORT).show();*/
                        checkChange = 0;
                    }
                    /* if the new input is empty */
                    else if (newUsername.length()==0){
                        /* set red border on the editable text box */
                        popupEditText.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                        /* set a error text to Username can't be empty */
                        popupErrorText.setText("Username can't be empty");
                       /* Toast.makeText(ProfileActivity.this," username cannot be empty ",Toast.LENGTH_SHORT).show();*/
                        checkChange = 0;
                    }
                    /* the new input is possible to change the username */
                    else {
                        changeUsername();
                    }
                }

            }
        });

        popupCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
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
                    Toast.makeText(ProfileActivity.this, "There is no record of your gameplay", Toast.LENGTH_SHORT).show();
                    if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String text = "There is no record of your gameplay";
                                textToSpeech.speak(text,TextToSpeech.QUEUE_ADD,null);
                                text = "Lets play some uta pair !";
                                textToSpeech.speak(text,TextToSpeech.QUEUE_ADD,null);
                            }
                        }, 500);
                    }
                }
                else{
                    try {
                        profileUserList.clear();     /* clear data */
                        JSONArray products = new JSONArray(response);
                        int count = 0;
                        for(int i=0;i<products.length();i++){   /* dor loop to collect data from database */
                            JSONObject productobject = products.getJSONObject(i);
                            Integer row_index = productobject.getInt("row_index");
                            Integer endTime = productobject.getInt("endTime");
                            String minute = String.valueOf(endTime/6000);
                            String second = String.valueOf((endTime/100)%60);
                            String mSecond = String.valueOf(endTime%100);
                            System.out.println(second.length());
                            /* If length of "second" less 2 (0:0:00) */
                            if(second.length()<2){
                                /* If length of "mSecond" less 2 (0:0:0) */
                                if(mSecond.length()<2){
                                    score = minute+":"+"0"+second+":"+"0"+mSecond;
                                }
                                /* else length of "mSecond" not less 2 (0:0:00) */
                                else{
                                    score = minute+":"+"0"+second+":"+mSecond;
                                }
                            }
                            /* If length of "mSecond" less 2 (0:00:0) */
                            else if(mSecond.length()<2){
                                /* If length of "second" less 2 (0:0:0) */
                                if(second.length()<2){
                                    score = minute+":"+"0"+second+":"+"0"+mSecond;
                                }
                                /* else length of "second" not less 2 (0:00:0) */
                                else{
                                    score = minute+":"+second+":"+"0"+mSecond;
                                }
                            }
                            /* else length of "second" not less 2 (0:00:00) */
                            else{
                                score = minute+":"+second+":"+mSecond;
                            }
                            if(count==0) {
                                textBestPlace = "your best place is " + row_index + " and your score is " + minute + "minute" + second + "second" + mSecond + "millisecond";
                                count++;
                            }
                            profileUserList.add(new ProfileUser(i+1,row_index,score));       /* add data from database */
                            setAdapter();       /* show in recyclerView */
                        }
                        /* If AccessibilityMode on speak and delay more than speak in method onStart */
                        if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    textToSpeech.speak(textBestPlace,TextToSpeech.QUEUE_ADD,null);
                                }
                            }, 600);
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
                data.put("LEVEL", buttonLevel);     /* put level to database */
               if(checkChange==1){
                    data.put("USERNAME", newUsername);      /* put username to database */
               }
                else{
                   data.put("USERNAME", saveName);
               }
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    /* method to speak change name failed */
    public void sayFailed(){
        if (AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
            String text = "change name failed";
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        textLevel = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),textLevel,Toast.LENGTH_SHORT).show();

        /* count for do not speak when this activity is start */
        if(sdCount<=1)
            sdCount++;
        if (sdCount>1) {
            soundClick.playSoundClick();
            /* speak when AccessibilityMode on */
            if (AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
                String text = "Select level " + textLevel;
                textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                if(PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).getBoolean("BLIND_PROFILE",false)){
                    text = "on Blind Mode";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_ADD,null);
                }
            }
        }
        showScore();

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
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void onCheckboxClicked(View view) {
    }
    @Override
    public void onBackPressed() {

        /* If still have logged in data */
        if (sh.contains("SAVED_NAME")) {
            Intent intent = new Intent(this, MainActivity.class);
            finishAffinity();
            startActivity(intent);
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
                    dialog.dismiss();
                }
                /* If the php response is ABLEFAILURE */
                else if (response.equals("ABLEFAILURE")) {
                    checkChange = 0;
                    /* Set red border on the editable text box */
                    popupEditText.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                    /* Set a error text to Something wrong!. Please try again later */
                    popupErrorText.setText("Something wrong!. Please try again later");
                    /* Pop up Something wrong!. Please try again later */
                    /* Toast.makeText(ProfileActivity.this, "Something wrong!. Please try again later", Toast.LENGTH_SHORT).show(); */
                    sayFailed();
                }
                /* If the php response is EXIST */
                else if (response.equals("EXIST")) {
                    checkChange =0;
                    /* Set red border on the editable text box */
                    popupEditText.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                    /* Set a error text to This username used by someone else */
                    popupErrorText.setText("This username used by someone else");
                    /* Pop up This username used by someone else */
                   /* Toast.makeText(ProfileActivity.this,"This username used by someone else ",Toast.LENGTH_SHORT).show();*/
                    sayFailed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkChange =0;
                /* set red border on the editable text box */
                popupEditText.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                /* Set a error text to This username used by someone else */
                popupErrorText.setText("Server error. Please try again later");
                /* When the error on response "Server pop up error. Please try again later" */
                /* Toast.makeText(ProfileActivity.this, "Server error. Please try again later", Toast.LENGTH_LONG).show(); */
                sayFailed();

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