package com.example.utapair;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button buttonLogin;
    private String URL = "http://192.168.182.50/RegisterLogin/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = spassword = "";
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.login_btn);
    }



    public void openProfileActivity(){
        Intent intent=new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        username = name.getText().toString().trim();
        spassword = password.getText().toString().trim();
        if(!username.equals("") && !spassword.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override

                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Toast.makeText(LoginActivity.this, "Success.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.equals("failure")) {
                        Toast.makeText(LoginActivity.this, "Invalid Login Name/Password.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
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
        else{
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }
}