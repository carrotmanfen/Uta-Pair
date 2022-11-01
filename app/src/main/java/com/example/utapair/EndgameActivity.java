package com.example.utapair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class EndgameActivity extends Activity {
    private TextView scoreTimeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        scoreTimeText = findViewById(R.id.score_time_text);
        Intent receiverIntent = getIntent();
        String receiveValue = receiverIntent.getStringExtra("TIME_SCORE");
        scoreTimeText.setText(receiveValue);
    }
}
