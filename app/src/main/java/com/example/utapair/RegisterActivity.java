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

    private EditText name,password,repassword;
    private TextView mismatch,usernameerror,passworderror;
    private CheckBox btncheck;
    private Button btn;
    private ImageButton buttonBack;
    //ต้องเปิด Xampp กับ ngrok ใหม่ตลอด
    private String URL = "https://9f47-202-28-7-98.ap.ngrok.io/RegisterLogin/register.php";
    private String username,spassword,srepassword,blind;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        Declare variable
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.confirm_password);
        btn = findViewById(R.id.sign_up_btn);
        btncheck =findViewById(R.id.blind_mode_checkbox);
        buttonBack =findViewById(R.id.register_backward_btn);
        mismatch = findViewById(R.id.password_mismatch);
        usernameerror = findViewById(R.id.username_errorText);
        passworderror = findViewById(R.id.password_errorText);
        username = spassword = srepassword = "";
        blind = "0";

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
//    when users click sign up button.
    public void signup(View view) {
        username = name.getText().toString().trim();
        spassword = password.getText().toString().trim();
        srepassword = repassword.getText().toString().trim();
//        Set ค่า background ให้เป็น ค่าเดิมทุกครั้งที่กด register แล้วค่อยเข้าเงื่อนไข
        name.setBackground(getResources().getDrawable(R.drawable.custom_input));
        password.setBackground(getResources().getDrawable(R.drawable.custom_input));
        repassword.setBackground(getResources().getDrawable(R.drawable.custom_input));

        usernameerror.setText("");
        mismatch.setText("");
        passworderror.setText("");

//        If users send password not equal with confirm password.
         if(!username.equals("") && !spassword.equals("")) {
            if(!spassword.equals(srepassword)){
                mismatch.setText("Password do not match.");
//            Set ค่า background ให้เป็นสีแดง
                password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            }
            else if ((username.length() <= 16) && (password.length() <= 16)) {
                if(btncheck.isChecked()){
                    blind = "1";
                }
                addData();
            }
            else if(username.length()>16){
                usernameerror.setText("Unable to use more than 16 characters username.");
                name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Invalid username input", Toast.LENGTH_SHORT).show();
            }
            else {
                passworderror.setText("Unable to use more than 16 characters password.");
                password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Invalid password input", Toast.LENGTH_SHORT).show();
            }
        }
        else if(username.equals("") && spassword.equals("") && srepassword.equals("")){
               checkAllempty();
        }
        else if(username.equals("")){
             checkNameempty();
        }
        else if(spassword.equals("")){
            checkPasswordempty();
        }
        else if(srepassword.equals("")){
            checkRepasswordempty();
        }
    }

    public void addData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                if (response.equals("success")) {
                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    openAccountActivity();
                } else if (response.equals("failure")) {
                    Toast.makeText(RegisterActivity.this, "Something wrong!. Please try again later", Toast.LENGTH_SHORT).show();
                } else if (response.equals("exist")) {
                    usernameerror.setText("This username is already used by someone else.");
                    name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Server error. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("password", spassword);
                data.put("blind", blind);
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

    public void checkAllempty(){
        name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        usernameerror.setText("Fields can not be empty!");
        passworderror.setText("Fields can not be empty!");
        mismatch.setText("Fields can not be empty!");
        Toast.makeText(this, "All Fields can not be empty!", Toast.LENGTH_SHORT).show();
    }
    public void checkNameempty(){
        name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        usernameerror.setText("Fields can not be empty!");
        if(spassword.equals("")){
            password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            passworderror.setText("Fields can not be empty!");
        }
        else if(srepassword.equals("")){
            repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            mismatch.setText("Fields can not be empty!");
        }
    }
    public void checkPasswordempty(){
        password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        passworderror.setText("Fields can not be empty!");
        if(username.equals("")){
            name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            usernameerror.setText("Fields can not be empty!");
        }
        else if(srepassword.equals("")){
            repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            mismatch.setText("Fields can not be empty!");
        }
    }
    public void checkRepasswordempty(){
        repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        mismatch.setText("Fields can not be empty!");
        if(username.equals("")){
            name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            usernameerror.setText("Fields can not be empty!");
        }
        else if(spassword.equals("")){
            password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            passworderror.setText("Fields can not be empty!");
        }
    }

}