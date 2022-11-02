package com.example.utapair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class EndgameActivity extends Activity {
    private TextView scoreTimeText;
    private TextView modeTextId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        scoreTimeText = findViewById(R.id.score_time_text);
        Intent receiverIntent = getIntent();
        String receiveValue = receiverIntent.getStringExtra("TIME_SCORE");
        scoreTimeText.setText(receiveValue);

        String modeText = setTextMode();
        modeTextId = findViewById(R.id.mode_text);
        modeTextId.setText(modeText);


    }

    public String setTextMode(){
        Bundle bundle = getIntent().getExtras();
        int mode = bundle.getInt("MODE");

        switch (mode){
            case -1:
                return "LEVEL - EASY";
            case 0:
                return "LEVEL - NORMAL";
            case 1:
                return "LEVEL - HARD";
        }
        return "Error!";
    }
//    @Override
//    public void onBackPressed(){
//
//    }
}
