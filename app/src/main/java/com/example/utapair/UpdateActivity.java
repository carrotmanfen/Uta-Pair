package com.example.utapair;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {
    private Button update;
    private TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        update = (Button) findViewById(R.id.confirm_popup_btn);
        username = (TextView) findViewById(R.id.username_textview);
        StrictMode.enableDefaults(); /* this is enable tread policy to call internet service with */
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
