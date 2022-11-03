package com.example.utapair;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private String URL = "https://b-183-88-60-244.ap.ngrok.io/RegisterLogin/checkLogin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Declare Variable
        username = spassword = "";
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.login_btn);
        usernameempty = findViewById(R.id.login_username_errorText);
        passwordempty = findViewById(R.id.login_password_errorText);
    }



    public void openProfileActivity(){
        Intent intent=new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    //    when users click sign up button.
    public void login(View view) {
        username = name.getText().toString().trim();
        spassword = password.getText().toString().trim();
        //Set ค่า background ให้เป็น ค่าเดิมทุกครั้งที่กด register แล้วค่อยเข้าเงื่อนไข
        name.setBackground(getResources().getDrawable(R.drawable.custom_input));
        password.setBackground(getResources().getDrawable(R.drawable.custom_input));
        usernameempty.setText("");
        passwordempty.setText("");
        if((username.equals(""))&&(spassword.equals(""))){
            usernameempty.setText("Username cannot be empty");
            //Set ค่า background ให้เป็นสีแดง
            name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            passwordempty.setText("Password cannot be empty");
            password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        }
        else if(username.equals("")){
            usernameempty.setText("Username cannot be empty");
            //Set ค่า background ให้เป็นสีแดง
            name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        }
        else if(spassword.equals("")){
            passwordempty.setText("Password cannot be empty");
            password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));

        }
        else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override

                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Toast.makeText(LoginActivity.this, "Success.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.equals("failure")) {
                        name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                        usernameempty.setText("Invalid Login Name/Password.");
                        password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                        passwordempty.setText("Invalid Login Name/Password.");
                        //Toast.makeText(LoginActivity.this, "Invalid Login Name/Password.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Server error. Please try again later", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(LoginActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
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
    }
}