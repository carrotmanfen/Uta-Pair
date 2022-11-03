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
    private TextView mismatch,usernameerror,passworderror;
    private CheckBox btncheck;
    private Button btn;
    //ต้องเปิด Xampp กับ ngrok ใหม่ตลอด
    private String URL = "https://63eb-2001-fb1-b0-1fe4-b95d-5622-c48-e857.ap.ngrok.io/RegisterLogin/register.php";
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
        mismatch = findViewById(R.id.password_mismatch);
        usernameerror = findViewById(R.id.username_errorText);
        passworderror = findViewById(R.id.password_errorText);
        username = spassword = srepassword = "";
        blind = "0";
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
//        If users select blind in the checkbox blind has a value equal to 1.
        if(btncheck.isChecked()){
            blind = "1";
        }
//        If users send password not equal with confirm password.
        if(!spassword.equals(srepassword)){
            mismatch.setText("Password do not match.");
//            Set ค่า background ให้เป็นสีแดง
            password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            //Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
        }
        else if(!username.equals("") && !spassword.equals("")) {
            if ((username.length() <= 16) && (password.length() <= 16)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            openAccountActivity();
                        } else if (response.equals("failure")) {
                            Toast.makeText(RegisterActivity.this, "Something wrong!", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("exist")) {
                            usernameerror.setText("This username is already used by someone else.");
                            name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                            //Toast.makeText(RegisterActivity.this, "This username is already used by someone else.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
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
            else if(username.length()>16){
                usernameerror.setText("Please enter at least 6 - 16 characters.");
                name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Invalid username input", Toast.LENGTH_SHORT).show();
            }
            else {
                passworderror.setText("Please enter at least 6 - 16 characters.");
                password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Invalid password input", Toast.LENGTH_SHORT).show();
            }
        }
        else {
                name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }


    public void openAccountActivity(){
        Intent intent=new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
}