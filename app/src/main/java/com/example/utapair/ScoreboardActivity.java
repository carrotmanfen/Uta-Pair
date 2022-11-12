package com.example.utapair;

import static java.lang.String.valueOf;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ArrayList<ScoreboardUser> scoreboardUserList;
    private RecyclerView recyclerView;
    private ImageButton buttonProfile;
    private ImageButton buttonSetting;
    private ImageButton buttonBack;
    private CheckBox buttonCheckbox;
    private String buttonLevel,checkboxBlind;

    private String URL = "https://dd07-183-88-63-158.ap.ngrok.io/RegisterLogin/scoreboard.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        Spinner spinner = findViewById(R.id.level_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.level,R.layout.spinner_text_select);
        adapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        buttonCheckbox = findViewById(R.id.blind_mode_checkbox);
        buttonLevel="MAL04";
        checkboxBlind="0";

        recyclerView = findViewById(R.id.scoreboard_recycler_view);

        scoreboardUserList = new ArrayList<>();
        setUserInfo(buttonLevel);
        setAdapter();




        buttonProfile = (ImageButton) findViewById(R.id.profile_btn);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountActivity();
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

    public void openAccountActivity(){
        Intent intent=new Intent(this, AccountActivity.class);
        finish();
        startActivity(intent);
    }

    public void openSettingActivity(){
        Intent intent=new Intent(this, SettingActivity.class);
        finish();
        startActivity(intent);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String level = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),level,Toast.LENGTH_SHORT).show();
        if(buttonCheckbox.isChecked()){
            checkboxBlind = "true";
        }else{
            checkboxBlind = "false";
        }
        buttonLevel="";
        if(checkboxBlind.equals("true")){
            if(level.equals("EASY")){
                buttonLevel="MAL01";
            }
            else if(level.equals("NORMAL")){
                buttonLevel="MAL02";
            }
            else if(level.equals("HARD")){
                buttonLevel="MAL03";
            }
        }
        else if(checkboxBlind.equals("false")){
            if(level.equals("EASY")){
                buttonLevel="MAL04";
            }
            else if(level.equals("NORMAL")){
                buttonLevel="MAL05";
            }
            else if(level.equals("HARD")){
                buttonLevel="MAL06";
            }
        }

        //setUserInfo(buttonLevel);
        //setAdapter();
        //scoreboardUserList.clear();
    }

    private void setAdapter() {
        ScoreboardRecyclerAdapter scoreboardRecyclerAdapter = new ScoreboardRecyclerAdapter(scoreboardUserList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(scoreboardRecyclerAdapter);
    }

    public void setUserInfo(String buttonLevel){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                try {
                    JSONArray products = new JSONArray(response);
                    Toast.makeText(ScoreboardActivity.this, response, Toast.LENGTH_SHORT).show();
                    for(int i=0;i<products.length();i++){
                        JSONObject productobject = products.getJSONObject(i);
                        String username = productobject.getString("username");
                        String endTime = productobject.getString("endTime");
                        scoreboardUserList.add(new ScoreboardUser(i+1,username,endTime));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScoreboardActivity.this, "Server error. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("Level", buttonLevel);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}