package com.example.utapair;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText name,password;
    private String username,spassword;
    private TextView usernameempty,passwordempty;
    private Button buttonLogin;
    private ImageButton buttonBack;
//  Connect Server
    private String URL = "https://d806-183-88-35-84.ap.ngrok.io/RegisterLogin/checkLogin.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//  Declare Variable
        username = spassword = "";
        name = findViewById(R.id.login_name_textbox);
        password = findViewById(R.id.login_password_textbox);
        buttonLogin = findViewById(R.id.login_btn);
        usernameempty = findViewById(R.id.login_username_errorText);
        passwordempty = findViewById(R.id.login_password_errorText);

        buttonBack = findViewById(R.id.backward_btn);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

//    when users click Login button.
    public void login(View view) {
        username = name.getText().toString().trim();
        spassword = password.getText().toString().trim();
//    Set background to custom_input (Drawable)
        name.setBackground(getResources().getDrawable(R.drawable.custom_input));
        password.setBackground(getResources().getDrawable(R.drawable.custom_input));
//    Set Textview to default
        usernameempty.setText("");
        passwordempty.setText("");
//        If editText username and password is empty
        if((username.equals(""))&&(spassword.equals(""))){
            //Set background to custom_input_error (Drawable) and set text to error Message
            usernameempty.setText("Username cannot be empty");
            name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            passwordempty.setText("Password cannot be empty");
            password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        }
//        If editText username is only empty field
        else if(username.equals("")){
//       Set background to custom_input_error (Drawable) and set text to error Message
            usernameempty.setText("Username cannot be empty");
            name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        }
//        If editText password is only empty field
        else if(spassword.equals("")){
//       Set background to custom_input_error (Drawable) and set text to error Message
            passwordempty.setText("Password cannot be empty");
            password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));

        }
        else {
//          Call method userLogin to allow users to access the application.
            userLogin();
        }
    }

    public void userLogin(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
//          when request have response to application
            public void onResponse(String response) {
//              If response is success
                if (response.equals("success")) {
//                  This page alert "Success" and open ProfileActivity.clss
                    Toast.makeText(LoginActivity.this, "Success.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
//                if respone is failure
                else if (response.equals("failure")) {
//                  This page alert "Invalid login Name/Password." and users can try again
                    Toast.makeText(LoginActivity.this, "Invalid login Name/Password.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
//            When applition have problem not to connect to database
            public void onErrorResponse(VolleyError error) {
//              this page alert "Server error. Please try again later"
                Toast.makeText(LoginActivity.this, "Server error. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
//          This method is use for put data from editText to check with database
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("username",username);
                data.put("password",spassword);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
//    Method for using open profile page
    public void openProfileActivity(){
        Intent intent=new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}