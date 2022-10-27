package com.example.utapair;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText name,password,repassword;
    private TextView status;
    private CheckBox btncheck;
    private Button btn;
    private String URL = "http://192.168.182.113/RegisterLogin/register.php";
    private String username,spassword,srepassword,blind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.confirm_password);
        btn = findViewById(R.id.sign_up_btn);
        btncheck =findViewById(R.id.blind_mode_checkbox);
        username = spassword = srepassword = "";
        blind = "0";
    }


    public void signup(View view) {
        username = name.getText().toString().trim();
        spassword = password.getText().toString().trim();
        srepassword = repassword.getText().toString().trim();
        if(btncheck.isChecked()){
            blind = "1";
        }
        if(!spassword.equals(srepassword)){
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
        }
        else if(!username.equals("") && !spassword.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override

                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        btn.setClickable(false);
                    } else if (response.equals("failure")) {
                        Toast.makeText(RegisterActivity.this, "Something wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> data = new HashMap<>();
                    data.put("username",username);
                    data.put("password",spassword);
                    data.put("blind",blind);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}