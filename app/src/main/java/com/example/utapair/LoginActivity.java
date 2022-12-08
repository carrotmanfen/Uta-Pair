package com.example.utapair;
import static com.example.utapair.R.raw.sc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



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
/* this class is about LoginActivity
* that user can login to the database
* and keep the application login
* and get and send data between app and database */
public class LoginActivity extends AppCompatActivity {
    private EditText editTextName, editTextPassword;
    private String username,sPassword;
    private TextView TextViewUsernameError,TextViewPasswordError;
    private ImageButton buttonBack;
    private Button buttonLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    private TextToSpeech textToSpeech;
    private int tapCount = 0;
    private SoundClick soundClick;

    /* Connect Server */
    private String loginURL = "https://uta-pair-api.herokuapp.com/checkLogin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        /*  Declare Variable */
        sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        username = sPassword = "";
        editTextName = findViewById(R.id.login_name_textbox);
        editTextName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            /* when touch speak text */
            public boolean onTouch(View view, MotionEvent motionEvent) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                    String text = "Type your name";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                return false;
            }
        });
        editTextPassword = findViewById(R.id.login_password_textbox);
        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            /* when touch speak text */
            public boolean onTouch(View view, MotionEvent motionEvent) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                    String text = "Type your password";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                return false;
            }
        });
        TextViewUsernameError = findViewById(R.id.login_username_errorText);
        TextViewPasswordError = findViewById(R.id.login_password_errorText);

        /* set buttonBack */
        buttonBack = findViewById(R.id.backward_btn);
        buttonBack.setOnClickListener(new View.OnClickListener() {
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

        buttonLogin = findViewById(R.id.login_btn);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    loginAccessibility();
                }
                else {
                    login();
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
                    String text = "login";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }, 500);
        }
    }

    /* method to speak failed register */
    public void sayFailed(){
        if (AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
            String text = "login failed";
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    /* when users click Login button. */
    public void login (){
        username = editTextName.getText().toString().trim();
        sPassword = editTextPassword.getText().toString().trim();
        /* Set background to custom_input (Drawable) */
        editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input));
        editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input));
        /* Set Textview to default */
        TextViewUsernameError.setText("");
        TextViewPasswordError.setText("");
        /* If editText username and password is empty */
        if ((username.equals("")) && (sPassword.equals(""))) {
            /* Set background to custom_input_error (Drawable)
             * and set text to error Message */
            TextViewUsernameError.setText("Username cannot be empty");
            editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            TextViewPasswordError.setText("Password cannot be empty");
            editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            sayFailed();
        }
        /* If editText username is only empty field */
        else if (username.equals("")) {
            /* Set background to custom_input_error (Drawable)
             * and set text to error Message */
            TextViewUsernameError.setText("Username cannot be empty");
            editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            sayFailed();
        }
        /* If editText password is only empty field */
        else if (sPassword.equals("")) {
            /* Set background to custom_input_error (Drawable)
             * and set text to error Message */
            TextViewPasswordError.setText("Password cannot be empty");
            editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            sayFailed();
        } else {
            /* Call method userLogin to allow users to access the application. */
            userLogin();

        }
    }

    /* method to sign up with AccessibilityMode */
    public void loginAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to login";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time to sign up */
                else if(tapCount==2){
                    login();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    public void userLogin () {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginURL, new Response.Listener<String>() {
            @Override
            /* when request have response to application */
            public void onResponse(String response) {
                /* If response is success */
                if (response.equals("SUCCESS")) {
                    String loginName = editTextName.getText().toString();
                    String loginPassword = editTextPassword.getText().toString();
                    myEdit.putString("SAVED_NAME", loginName);
                    myEdit.putString("SAVED_PASSWORD", loginPassword);
                    myEdit.commit();

                    /* This page alert "Success" and open ProfileActivity.class */
                    Toast.makeText(LoginActivity.this, "Success.", Toast.LENGTH_SHORT).show();
                    if (sharedPreferences.contains("SAVED_NAME")) {
                        if (AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                            String text = "login success";
                            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        openProfileActivity();

                    }
                }
                /* if response is failure */
                else if (response.equals("FAILURE")) {
                    /* This page alert "Invalid login Name/Password." and users can try again and red border on both text box*/
                    editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                    editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                    Toast.makeText(LoginActivity.this, "Invalid login Name/Password.", Toast.LENGTH_SHORT).show();
                    sayFailed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            /* When application have problem not to connect to database */
            public void onErrorResponse(VolleyError error) {
                /* this page alert "Server error. Please try again later" */
                Toast.makeText(LoginActivity.this, "Server error. Please try again later", Toast.LENGTH_SHORT).show();
                sayFailed();
            }
        }) {
            @Nullable
            @Override
            /* This method is use for put data from editText to check with database */
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("USERNAME", username);
                data.put("PASSWORD", sPassword);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    /* Method for using open profile page */
    public void openProfileActivity () {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
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
