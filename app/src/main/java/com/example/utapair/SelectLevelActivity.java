package com.example.utapair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectLevelActivity extends AppCompatActivity {

    private Button easyButton; // dimension =  2 rows , 3 cols
    private Button normalButton; // dimension = 4 rows, 3 cols
    private Button hardButton; // dimension = 6 rows, 3 cols

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_select_level); // หน้า menu file layout

        // define level button
        easyButton = (Button) findViewById(R.id.easy_btn);
        normalButton = (Button) findViewById(R.id.normal_btn);
        hardButton = (Button) findViewById(R.id.hard_btn);


        // if you wanna try new method just learn it

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEasyGameActivity();
            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNormalGameActivity();
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHardGameActivity();
            }
        });

    }

    public void openEasyGameActivity(){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("mode", -1);
        intent.putExtra("layout_id", R.layout.activity_game_easy2);
        intent.putExtra("grid_id", R.id.GridLayout_easy);
        startActivity(intent);
    }
    public void openNormalGameActivity(){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("mode", 0);
        intent.putExtra("layout_id", R.layout.activity_game_normal);
        intent.putExtra("grid_id",R.id.GridLayout_meduim);
        startActivity(intent);
    }
    public void openHardGameActivity(){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("mode", 1);
        intent.putExtra("layout_id", R.layout.activity_game_hard);
        intent.putExtra("grid_id",R.id.GridLayout_hard);
        startActivity(intent);
    }

}