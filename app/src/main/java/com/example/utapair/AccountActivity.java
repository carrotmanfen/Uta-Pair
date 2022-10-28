package com.example.utapair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountActivity extends AppCompatActivity {

    Button regisbtn,loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        regisbtn = findViewById(R.id.register_btn);
        loginbtn = findViewById(R.id.login_btn);
        regisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActivity();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });
    }
    public void openRegisterActivity(){
        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    public void openLoginActivity(){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}