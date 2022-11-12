package com.example.utapair;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextPassword, editTextRepassword;
    private TextView repasswordError, usernameError, passwordError;
    private CheckBox buttonCheck;
    private Button buttonSignUp;
    private ImageButton buttonBack;
    private String Username, Password, rePassword, blindMode;
    /* Connect server */
    private String URL = "https://dd07-183-88-63-158.ap.ngrok.io/RegisterLogin/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    /* Declare variable */
        editTextName = findViewById(R.id.name);
        editTextPassword = findViewById(R.id.password);
        editTextRepassword = findViewById(R.id.confirm_password);
        buttonSignUp = findViewById(R.id.sign_up_btn);
        buttonCheck =findViewById(R.id.blind_mode_checkbox);
        buttonBack =findViewById(R.id.register_backward_btn);
        repasswordError = findViewById(R.id.password_mismatch);
        usernameError = findViewById(R.id.username_errorText);
        passwordError = findViewById(R.id.password_errorText);
        Username = Password = rePassword = "";
        blindMode = "0";
        buttonBack = findViewById(R.id.register_backward_btn);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    /* when users click sign up button. */
    public void signup(View view) {
        Username = editTextName.getText().toString().trim();
        Password = editTextPassword.getText().toString().trim();
        rePassword = editTextRepassword.getText().toString().trim();
        /* Set background to custom_input (Drawable) */
        editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input));
        editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input));
        editTextRepassword.setBackground(getResources().getDrawable(R.drawable.custom_input));
        /* Set error text to default which is empty */
        usernameError.setText("");
        repasswordError.setText("");
        passwordError.setText("");
         /* If users send password not match with confirm password. */
         if(!Username.equals("") && !Password.equals("")) {
             /* If password and confirm-password doesn't match */
             if(!Password.equals(rePassword)){
                repasswordError.setText("Password do not match.");
                /* Set background to custom_input (Drawable) */
                editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                editTextRepassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            }
            /* If username and password is less than 16 character */
            else if ((Username.length() <= 16) && (editTextPassword.length() <= 16)) {
                /* If blind button is checked */
                if(buttonCheck.isChecked()){
                    blindMode = "1";
                }
                addData();
            }
            /* If username is unable to use */
            else if(Username.length()>16){
                /* appear Unable to use more than 16 characters username.
                 * below username text box and make username text box border to red */
                usernameError.setText("Unable to use more than 16 characters username.");
                editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Invalid username input", Toast.LENGTH_SHORT).show();
            }
            /* If password is unable to use */
            else {
                /* appear text under password to Unable to use more than 16 characters
                 * password and the text box border of password and confirm-password
                 * into red color */
                passwordError.setText("Unable to use more than 16 characters password.");
                editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                editTextRepassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Invalid password input", Toast.LENGTH_SHORT).show();
            }
        }
        /* If all fields are empty */
        else if(Username.equals("") && Password.equals("") && rePassword.equals("")){
               // use function that set the UI for all fields are empty
               checkAllempty();
        }
        /* If username field is empty */
        else if(Username.equals("")){
             checkNameempty();  /* use function that set the UI for username is empty */
        }
        /* If password field is empty */
        else if(Password.equals("")){
            checkPasswordempty();   /* use function that set the UI for password is empty */
        }
        /* If confirm password field is empty */
        else if(rePassword.equals("")){
            checkRepasswordempty(); /* use function that set the UI for confirm password is empty */
        }
    }

    public void addData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                if (response.equals("success")) {
                    /* Pop up Success */
                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    openAccountActivity();
                } else if (response.equals("failure")) {
                    /* If response is failure show pop up "Something wrong!. Please try again later" */
                    Toast.makeText(RegisterActivity.this, "Something wrong!. Please try again later", Toast.LENGTH_SHORT).show();
                } else if (response.equals("exist")) {
                    /* make the text below username text box be
                     * This username is already used by someone
                     * else and set the border of username text box be red */
                    usernameError.setText("This username is already used by someone else.");
                    editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
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
                data.put("username", Username);
                data.put("password", Password);
                data.put("blind", blindMode);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void openAccountActivity(){
        Intent intent=new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
    /* function use to set the UI when all fields are empty */
    public void checkAllempty(){
        /* set the name ,password and confirm-password
         * text box into red border and appear text
         * Fields can not be empty! */
        editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        editTextRepassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        usernameError.setText("Fields can not be empty!");
        passwordError.setText("Fields can not be empty!");
        repasswordError.setText("Fields can not be empty!");
        Toast.makeText(this, "All Fields can not be empty!", Toast.LENGTH_SHORT).show();
    }
    /* function use to set the UI when in the case which username is empty */
    public void checkNameempty(){
        editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        usernameError.setText("Fields can not be empty!");
        /* if username and password fields are empty */
        if(Password.equals("")){
            editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            passwordError.setText("Fields can not be empty!");
        }
        /* if username and confirm-password fields are empty */
        else if(rePassword.equals("")){
            editTextRepassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            repasswordError.setText("Fields can not be empty!");
        }
    }
    /* function use to set the UI when in the case which password is empty */
    public void checkPasswordempty(){
        editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        passwordError.setText("Fields can not be empty!");
        /* if password and username fields are empty */
        if(Username.equals("")){
            editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            usernameError.setText("Fields can not be empty!");
        }
        /* if password and confirm password fields are empty */
        else if(rePassword.equals("")){
            editTextRepassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            repasswordError.setText("Fields can not be empty!");
        }
    }
    /* function use to set the UI when in the case which confirm password is empty */
    public void checkRepasswordempty(){
        /* set the confirm-password text box into red border and appear text Fields can not be empty! */
        editTextRepassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        repasswordError.setText("Fields can not be empty!");
        /* if confirm password and username is empty */
        if(Username.equals("")){
            /* set the confirm-password and username text box into red border and appear text Fields can not be empty! */
            editTextName.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            usernameError.setText("Fields can not be empty!");
        }
        /* if confirm password and password is empty */
        else if(Password.equals("")){
            /* set the confirm-password and password text box into red border and appear text Fields can not be empty! */
            editTextPassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            passwordError.setText("Fields can not be empty!");
        }
    }

}