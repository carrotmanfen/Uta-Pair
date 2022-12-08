package com.example.utapair;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
/* this class is about RegisterActivity
* when user register it will
* update data in database */
public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextPassword, editTextRePassword;
    private TextView textViewRePasswordError, textViewUsernameError, textViewPasswordError;
    private CheckBox buttonCheck;
    private Button buttonSignUp;
    private ImageButton buttonBack;
    private String username, password, comfirmedPassword, blindMode;
    private TextToSpeech textToSpeech;
    private int tapCount = 0;
    private SoundClick soundClick;
    /* Connect server */
    private String registerURL = "https://uta-pair-api.herokuapp.com/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

        /* Declare variable */
        editTextName = findViewById(R.id.name);
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

        editTextPassword = findViewById(R.id.password);
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

        editTextRePassword = findViewById(R.id.confirm_password);
        editTextRePassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            /* when touch speak text */
            public boolean onTouch(View view, MotionEvent motionEvent) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                    String text = "Type confirm your password";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                return false;
            }
        });

        buttonSignUp = findViewById(R.id.sign_up_btn);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    signUpAccessibility();
                }
                else {
                    checkRegisterInput();
                }
            }
        });
        buttonCheck =findViewById(R.id.blind_mode_checkbox);
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundClick.playSoundClick(); /* sound click */
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY"){
                    if (buttonCheck.isChecked()) {
                        String text = "Checked you are blind person";
                        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    }
                    else {
                        String text = "Unchecked you are not blind person";
                        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
            }
        });
        textViewRePasswordError = findViewById(R.id.password_mismatch);
        textViewUsernameError = findViewById(R.id.username_errorText);
        textViewPasswordError = findViewById(R.id.password_errorText);

        username = password = comfirmedPassword = "";
        blindMode = "0";

        /* set buttonBack */
        buttonBack = findViewById(R.id.register_backward_btn);
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
                    String text = "register";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }, 500);
        }
    }

    /* when users click sign up button. */
    public void checkRegisterInput() {
        username = editTextName.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        comfirmedPassword = editTextRePassword.getText().toString().trim();
        /* Set background to custom_input (Drawable) */
        editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input));
        editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input));
        editTextRePassword.setBackground(getResources().getDrawable(R.drawable.custom_input));
        /* Set error text to default which is empty */
        textViewUsernameError.setText("");
        textViewRePasswordError.setText("");
        textViewPasswordError.setText("");
         /* If users send password not match with confirm password. */
         if(!username.equals("") && !password.equals("")) {
             /* If password and confirm-password doesn't match */
             if(!password.equals(comfirmedPassword)){
                textViewRePasswordError.setText("Password do not match.");
                /* Set background to custom_input (Drawable) */
                editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                editTextRePassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                sayFailed();
            }
            /* If username and password is less than 16 character */
            else if ((username.length() <= 16) && (editTextPassword.length() <= 16)) {
                /* If blind button is checked */
                if(buttonCheck.isChecked()){
                    blindMode = "1";
                }
                addData();
            }
            /* If username is unable to use */
            else if(username.length()>16){
                /* appear Unable to use more than 16 characters username.
                 * below username text box and make username text box border to red */
                textViewUsernameError.setText("Unable to use more than 16 characters username.");
                editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Invalid username input", Toast.LENGTH_SHORT).show();
                sayFailed();
            }
            /* If password is unable to use */
            else {
                /* appear text under password to Unable to use more than 16 characters
                 * password and the text box border of password and confirm-password
                 * into red color */
                textViewPasswordError.setText("Unable to use more than 16 characters password.");
                editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                editTextRePassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Invalid password input", Toast.LENGTH_SHORT).show();
                sayFailed();
            }
        }
        /* If all fields are empty */
        else if(username.equals("") && password.equals("") && comfirmedPassword.equals("")){
               // use function that set the UI for all fields are empty
               checkAllEmpty();
               sayFailed();
        }
        /* If username field is empty */
        else if(username.equals("")){
             checkNameEmpty();  /* use function that set the UI for username is empty */
             sayFailed();
        }
        /* If password field is empty */
        else if(password.equals("")){
             checkPasswordEmpty();   /* use function that set the UI for password is empty */
             sayFailed();
        }
        /* If confirm password field is empty */
        else if(comfirmedPassword.equals("")){
             checkRePasswordEmpty(); /* use function that set the UI for confirm password is empty */
             sayFailed();
        }
    }

    /* method to speak failed register */
    public void sayFailed(){
        if (AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
            String text = "register failed";
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    /* method to sign up with AccessibilityMode */
    public void signUpAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to register";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time to sign up */
                else if(tapCount==2){
                    checkRegisterInput();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    public void addData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, registerURL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                if (response.equals("SUCCESS")) {
                    /* Pop up Success */
                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    if (AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                        String text = "register success";
                        if(buttonCheck.isChecked()){
                            AccessibilityMode.getInstance().setMode("ACCESSIBILITY");
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                    .putBoolean("ACCESSIBILITY_CHECKBOX", true).commit();
                        }
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    openAccountActivity();
                } else if (response.equals("FAILURE")) {
                    /* If response is FAILURE show pop up "Something wrong!. Please try again later" */
                    Toast.makeText(RegisterActivity.this, "Something wrong!. Please try again later", Toast.LENGTH_SHORT).show();
                    sayFailed();
                } else if (response.equals("EXIST")) {
                    /* make the text below username text box be
                     * This username is already used by someone
                     * else and set the border of username text box be red */
                    textViewUsernameError.setText("This username is already used by someone else.");
                    editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                    sayFailed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /* When the error on response "Server pop up error. Please try again later" */
                Toast.makeText(RegisterActivity.this, "Server error. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /* Put data to check with php */
                Map<String, String> data = new HashMap<>();
                data.put("USERNAME", username);
                data.put("PASSWORD", password);
                data.put("BLIND", blindMode);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void openAccountActivity(){
        Intent intent=new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }
    /* function use to set the UI when all fields are empty */
    public void checkAllEmpty(){
        /* set the name ,password and confirm-password
         * text box into red border and appear text
         * Fields can not be empty! */
        editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        editTextRePassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        textViewUsernameError.setText("Fields can not be empty!");
        textViewPasswordError.setText("Fields can not be empty!");
        textViewRePasswordError.setText("Fields can not be empty!");
        Toast.makeText(this, "All Fields can not be empty!", Toast.LENGTH_SHORT).show();
    }
    /* function use to set the UI when in the case which username is empty */
    public void checkNameEmpty(){
        editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        textViewUsernameError.setText("Fields can not be empty!");
        /* if username and password fields are empty */
        if(password.equals("")){
            editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            textViewPasswordError.setText("Fields can not be empty!");
        }
        /* if username and confirm-password fields are empty */
        else if(comfirmedPassword.equals("")){
            editTextRePassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            textViewRePasswordError.setText("Fields can not be empty!");
        }
    }
    /* function use to set the UI when in the case which password is empty */
    public void checkPasswordEmpty(){
        editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        textViewPasswordError.setText("Fields can not be empty!");
        /* if password and username fields are empty */
        if(username.equals("")){
            editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            textViewUsernameError.setText("Fields can not be empty!");
        }
        /* if password and confirm password fields are empty */
        else if(comfirmedPassword.equals("")){
            editTextRePassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            textViewRePasswordError.setText("Fields can not be empty!");
        }
    }
    /* function use to set the UI when in the case which confirm password is empty */
    public void checkRePasswordEmpty(){
        /* set the confirm-password text box into red border and appear text Fields can not be empty! */
        editTextRePassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        textViewRePasswordError.setText("Fields can not be empty!");
        /* if confirm password and username is empty */
        if(username.equals("")){
            /* set the confirm-password and username text box into red border and appear text Fields can not be empty! */
            editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            textViewUsernameError.setText("Fields can not be empty!");
        }
        /* if confirm password and password is empty */
        else if(password.equals("")){
            /* set the confirm-password and password text box into red border and appear text Fields can not be empty! */
            editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            textViewPasswordError.setText("Fields can not be empty!");
        }
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