package com.example.utapair;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ArrayList<ProfileUser> profileUserList;
    private RecyclerView recyclerView;
    private ImageButton editProfileButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popupEditText;
    private Button popupCancelButton , popupConfirmButton ;
    private ImageButton buttonScoreboard;
    private ImageButton buttonSetting;
    private ImageButton buttonBack;
    private String saveName,sample,newUsername;
    private TextView profileName;
    Button buttonLogout;
    SharedPreferences sh;
    SharedPreferences.Editor editor;
    private String URL = "https://dd07-183-88-63-158.ap.ngrok.io/RegisterLogin/editname.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sh = getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
        editor = sh.edit();
        saveName = sh.getString("saved_Name","");
        profileName = findViewById(R.id.changename);
        profileName.setText(saveName);
        buttonLogout = findViewById(R.id.logout_btn);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
 /* sample = sh.getString("saved_Name",""); */
                if(sh.contains("saved_Name")){
                    Toast.makeText(ProfileActivity.this, "Logout unsuccessfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ProfileActivity.this, "Logout Successfully ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        Spinner spinner = findViewById(R.id.level_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.level,R.layout.spinner_text_select);
        adapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        recyclerView = findViewById(R.id.profile_recycler_view);

        profileUserList = new ArrayList<>();
        setUserInfo();
        setAdapter();

        editProfileButton = findViewById(R.id.editname_btn);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditName();
            }
        });

        buttonScoreboard = (ImageButton) findViewById(R.id.scoreboard_btn);
        buttonScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScoreboardActivity();
            }
        });

        buttonSetting = (ImageButton) findViewById(R.id.setting_btn);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingActivity();
            }
        });

        buttonBack = findViewById(R.id.backward_btn);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void openScoreboardActivity(){
        Intent intent=new Intent(this, ScoreboardActivity.class);
        finish();
        startActivity(intent);
    }

    public void openSettingActivity(){
        Intent intent=new Intent(this, SettingActivity.class);
        finish();
        startActivity(intent);
    }

    public void EditName(){
        dialogBuilder = new AlertDialog.Builder(this,R.style.dialog);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_edit_name,null);
        popupEditText = popupView.findViewById(R.id.new_name_edittext);
        popupCancelButton = popupView.findViewById(R.id.cancel_popup_btn);
        popupConfirmButton = popupView.findViewById(R.id.confirm_popup_btn);

        sh = getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
        editor = sh.edit();
        saveName = sh.getString("saved_Name","");
        popupEditText.setText(saveName);

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        popupConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUsername = popupEditText.getText().toString();
            /*  editor.putString("saved_Name",newUsername);
                editor.commit();*/
               if(newUsername == saveName ){
                   Toast.makeText(ProfileActivity.this,"Nothing change",Toast.LENGTH_SHORT).show();
               }
               else if (newUsername.length()>16){
                   Toast.makeText(ProfileActivity.this," cannot have username more than 16 character  ",Toast.LENGTH_SHORT).show();
               }
               else if (newUsername.length()==0){
                   Toast.makeText(ProfileActivity.this," username cannot be empty ",Toast.LENGTH_SHORT).show();
               }
               else {
                   Toast.makeText(ProfileActivity.this,saveName,Toast.LENGTH_SHORT).show();
                   changeUsername();
               }
            dialog.dismiss();
            }
        });

        popupCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void setAdapter() {
        ProfileRecyclerAdapter profileRecyclerAdapter = new ProfileRecyclerAdapter(profileUserList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(profileRecyclerAdapter);
    }

    private void setUserInfo() {
        profileUserList.add(new ProfileUser(1,10,"3:00"));
        profileUserList.add(new ProfileUser(2,12,"3:00"));
        profileUserList.add(new ProfileUser(3,18,"3:00"));
        profileUserList.add(new ProfileUser(4,21,"3:00"));
        profileUserList.add(new ProfileUser(5,23,"3:00"));
        profileUserList.add(new ProfileUser(6,28,"3:00"));
        profileUserList.add(new ProfileUser(7,30,"3:00"));

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String level = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),level,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void onCheckboxClicked(View view) {
    }
    @Override
    public void onBackPressed(){
        if(sh.contains("saved_Name")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();}
        else{
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void changeUsername(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                if (response.equals("success")) {
                    /* Pop up Success */
                    popupEditText.setText(newUsername);
                    Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                else if (response.equals("failure")) {
                    /* Pop up Something wrong!. Please try again later */
                    Toast.makeText(ProfileActivity.this, "Something wrong!. Please try again later", Toast.LENGTH_SHORT).show();
                } else if (response.equals("exist")) {
                    // make the text below username text box be This username is already used by someone else and set the border of username text box be red
                    Toast.makeText(ProfileActivity.this," This username used by someone else  ",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // When the error on response "Server pop up error. Please try again later"
                Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("newUsername", newUsername);
                data.put("oldUsername", saveName);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}