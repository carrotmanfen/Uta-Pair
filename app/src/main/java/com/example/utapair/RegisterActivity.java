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
    private String URL = "https://6acd-2001-fb1-b3-7432-2dee-b5c2-f14d-cdb0.ap.ngrok.io/RegisterLogin/register.php";
    private String username,spassword,srepassword,blind;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    //Declare variable
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

        buttonBack = findViewById(R.id.register_backward_btn);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    //when users click sign up button.
    public void signup(View view) {
        username = name.getText().toString().trim();
        spassword = password.getText().toString().trim();
        srepassword = repassword.getText().toString().trim();
        //Set สี border ให้เป็น ค่าเดิมทุกครั้งที่กด register แล้วค่อยเข้าเงื่อนไข
        name.setBackground(getResources().getDrawable(R.drawable.custom_input));
        password.setBackground(getResources().getDrawable(R.drawable.custom_input));
        repassword.setBackground(getResources().getDrawable(R.drawable.custom_input));
        //Set error text to default which is empty
        usernameerror.setText("");
        mismatch.setText("");
        passworderror.setText("");
         // If users send password not match with confirm password.
         if(!username.equals("") && !spassword.equals("")) {
            //If password and confirm-password doesn't match
             if(!spassword.equals(srepassword)){
                mismatch.setText("Password do not match.");
                //  Set ค่า border ของ password and confirm password text box ให้เป็นสีแดง
                password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            }
            // If username and password is usable
            else if ((username.length() <= 16) && (password.length() <= 16)) {
             // If blind button is checked
                if(btncheck.isChecked()){
                    blind = "1";
                }
                addData();
            }
            // If username is unable to use
            else if(username.length()>16){
                //appear Unable to use more than 16 characters username. below username text box and make username text box border to red
                usernameerror.setText("Unable to use more than 16 characters username.");
                name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Invalid username input", Toast.LENGTH_SHORT).show();
            }
            // If password is unable to use
            else {
                //appear text under password to Unable to use more than 16 characters password and the text box border of password and confirm-password into red color
                passworderror.setText("Unable to use more than 16 characters password.");
                password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                Toast.makeText(this, "Invalid password input", Toast.LENGTH_SHORT).show();
            }
        }
        //If all fields are empty
        else if(username.equals("") && spassword.equals("") && srepassword.equals("")){
               // use function that set the UI for all fields are empty
               checkAllempty();
        }
        //If username field is empty
        else if(username.equals("")){
             // use function that set the UI for username is empty
             checkNameempty();
        }
        //If password field is empty
        else if(spassword.equals("")){
            // use function that set the UI for password is empty
            checkPasswordempty();
        }
        //If confirm password field is empty
        else if(srepassword.equals("")){
            // use function that set the UI for confirm password is empty
            checkRepasswordempty();
        }
    }

    public void addData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                if (response.equals("success")) {
                    // Pop up Success
                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    openAccountActivity();
                } else if (response.equals("failure")) {
                    // Pop up Something wrong!. Please try again later
                    Toast.makeText(RegisterActivity.this, "Something wrong!. Please try again later", Toast.LENGTH_SHORT).show();
                } else if (response.equals("exist")) {
                    // make the text below username text box be This username is already used by someone else and set the border of username text box be red
                    usernameerror.setText("This username is already used by someone else.");
                    name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // When the error on response "Server pop up error. Please try again later"
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
    // function use to set the UI when all fields are empty
    public void checkAllempty(){
        // set the name ,password and confirm-password  text box into red border and appear text Fields can not be empty!
        name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        usernameerror.setText("Fields can not be empty!");
        passworderror.setText("Fields can not be empty!");
        mismatch.setText("Fields can not be empty!");
        Toast.makeText(this, "All Fields can not be empty!", Toast.LENGTH_SHORT).show();
    }
    // function use to set the UI when in the case which username is empty
    public void checkNameempty(){
        name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        usernameerror.setText("Fields can not be empty!");
        // if username and password fields are empty
        if(spassword.equals("")){
            password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            passworderror.setText("Fields can not be empty!");
        }
        // if username and confirm-password fields are empty
        else if(srepassword.equals("")){
            repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            mismatch.setText("Fields can not be empty!");
        }
    }
    // function use to set the UI when in the case which password is empty
    public void checkPasswordempty(){
        password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        passworderror.setText("Fields can not be empty!");
        // if password and username fields are empty
        if(username.equals("")){
            name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            usernameerror.setText("Fields can not be empty!");
        }
        // if password and confirm password fields are empty
        else if(srepassword.equals("")){
            repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            mismatch.setText("Fields can not be empty!");
        }
    }
    // function use to set the UI when in the case which confirm password is empty
    public void checkRepasswordempty(){
        // set the confirm-password text box into red border and appear text Fields can not be empty!
        repassword.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
        mismatch.setText("Fields can not be empty!");
        // if confirm password and username is empty
        if(username.equals("")){
            // set the confirm-password and username text box into red border and appear text Fields can not be empty!
            name.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            usernameerror.setText("Fields can not be empty!");
        }
        // if confirm password and password is empty
        else if(spassword.equals("")){
            // set the confirm-password and password text box into red border and appear text Fields can not be empty!
            password.setBackground(getResources().getDrawable(R.drawable.custom_input_error));
            passworderror.setText("Fields can not be empty!");
        }
    }

}