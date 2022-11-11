package com.example.utapair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;


public class EndgameActivity extends Activity {
    private TextView scoreTimeText;
    private TextView modeTextId;
    private Button playAgainButton2;
    private ImageButton homeButton, playAgainButton;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);


        scoreTimeText = findViewById(R.id.score_time_text);
        Intent receiverIntent = getIntent();
        String receiveValue = receiverIntent.getStringExtra("TIME_SCORE");
        scoreTimeText.setText(receiveValue);

        // if Accessibility Open
        if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY") {
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != textToSpeech.ERROR){
                        textToSpeech.setLanguage(Locale.US);
                    }
                }
            });
            textToSpeech.speak("your time score is " + receiveValue, TextToSpeech.QUEUE_FLUSH, null);
        }

        String modeText = setTextMode();
        modeTextId = findViewById(R.id.mode_text);
        modeTextId.setText(modeText);

        // share button implements

        ImageButton shareButton1 = findViewById(R.id.share_btn_1);
        Button shareButton2 = findViewById(R.id.share_btn_2);

        shareButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String Body = "UTA is the best app";
                String sub = "Application Link here !!"; // link of webpage app
                intent.putExtra(Intent.EXTRA_SUBJECT,Body);
                intent.putExtra(Intent.EXTRA_TEXT,sub);
                startActivity(Intent.createChooser(intent,"Share using"));
            }
        });
        shareButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String Body = "UTA is the best app";
                String sub = "Application Link here !!"; // link of webpage app
                intent.putExtra(Intent.EXTRA_SUBJECT,Body);
                intent.putExtra(Intent.EXTRA_TEXT,sub);
                startActivity(Intent.createChooser(intent,"Share using"));
            }
        });

        // end share button

        playAgainButton = findViewById(R.id.play_again_btn_1);
        playAgainButton2 = findViewById(R.id.play_again_btn_2);
        homeButton = findViewById(R.id.home_btn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
            }
        });
        playAgainButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
            }
        });

    }

    public void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    public void playAgain(){
        Intent intent = new Intent(this,GameActivity.class);
        Bundle bundle = getIntent().getExtras();
        int mode = bundle.getInt("MODE");
            if (mode==-1){
                intent.putExtra("mode", -1);
                intent.putExtra("layout_id", R.layout.activity_game_easy2);
                intent.putExtra("grid_id", R.id.GridLayout_easy);
                finish();
                startActivity(intent);
            }
            else if (mode==0) {
                intent.putExtra("mode", 0);
                intent.putExtra("layout_id", R.layout.activity_game_normal);
                intent.putExtra("grid_id", R.id.GridLayout_meduim);
                finish();
                startActivity(intent);
            }
            else if (mode==1) {
                intent.putExtra("mode", 1);
                intent.putExtra("layout_id", R.layout.activity_game_hard);
                intent.putExtra("grid_id", R.id.GridLayout_hard);
                finish();
                startActivity(intent);
            }

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
