package com.example.utapair;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private int numberOfElements;

    private MemoryButton[] button; // ปุ่มที่จะเอาขึ้นไปวางไว้บน GridLayout

    private int[] buttonGraphicLocation; // ตำแหน่ง Graphic ของ Button แต่ละตัวที่จะนำไป drawable
    private int[] buttonGraphic; // เก็บภาพจาก drawable มาใส่ array ไว้
    private String[] buttonGraphictexts;

    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private int numRows;
    private int numColumns;

    private Boolean isBusy = false;

    // variable for constructor
    private int mode;
    private int layout_id;
    private int grid_id;

    private ImageButton pauseButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button resumeButton, restartButton, homeButton;

    private TextView timerText;
    private Timer timer;
    private TimerTask timerTask;
    private int time = 0;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        Bundle bundle = getIntent().getExtras();
        mode = bundle.getInt("mode");
        layout_id = bundle.getInt("layout_id");
        grid_id = bundle.getInt("grid_id");

        setContentView(layout_id); // หน้า ตอนเล่นเกม

        GridLayout gridLayout = (GridLayout) findViewById(grid_id); // ต้องไปเขียนหน้า layout ที่จะให้ grid อยู่ก็คือหน้าเล่นเกม (in-game)

        numColumns = gridLayout.getColumnCount(); // อย่าลืม ไป define หน้าเดียวกันกับบรรทัดที่ 28
        numRows = gridLayout.getRowCount();

        gridLayout.setUseDefaultMargins(true); // ตรงนี้ถ้าอยาก specifile เองก็อาจจะต้องเขียนโค้ด อันนี้ใช่ default
        numberOfElements = numRows * numColumns;

        button = new MemoryButton[numberOfElements];

        buttonGraphic = new int[numberOfElements/2];
        buttonGraphictexts = new String[numberOfElements/2];

        // set language for Accessibility
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        // generate รูปที่ต้องใช้ตามขนาดจะทำแบบไหน ? แบบนี้ fix ไว้ (2 x 3)/2 = 3 unique picture
        setButtonGraphic();
//        buttonGraphic[0] = R.drawable.bttn_1;
//        buttonGraphic[1] = R.drawable.bttn_2;
//        buttonGraphic[2] = R.drawable.bttn_3;


        // สร้าง array สำหรับเก็บรูปของตำแหน่งนั้นว่าเป็นอะไร
        buttonGraphicLocation = new int[numberOfElements];

        shuffleButtonGraphics();


        for(int r = 0; r < numRows; r++) {

            for(int c = 0; c < numColumns; c++){
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphic[ buttonGraphicLocation[r * numColumns+c] ]);
                tempButton.setSymbol(buttonGraphictexts[ buttonGraphicLocation[r * numColumns+c] ]);
                tempButton.setPosition(r+1,c+1);
                tempButton.setId(View.generateViewId()); // สร้าง id ไว้ตอน match
                tempButton.setOnClickListener(this);
                button[r * numColumns+c] = tempButton;
                gridLayout.addView(tempButton);
            }

        }

        pauseButton = findViewById(R.id.pause_btn);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseGame();
            }
        });

        timerText = findViewById(R.id.time_text);
        timer = new Timer();
        starTimer();

    }

    /* this part will run when this Activity start */
    protected void onStart() {
        super.onStart();
        /* if AccessibilityMode on when this activity start play sound */
        if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String text = "Start";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }, 500);
        }
    }

    private void starTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerText.setText(getTimerText());
                        time++;
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1);
    }

    public String getTimerText() {
        int round =(int) Math.round(time);
       // int second = ((round % 864000)%3600)%60;
        //int minute = ((round % 864000)%3600)/60;
        int miliSec = time%100;
        int second = (time/1000)%60;
        int minute = (time/1000)/60;
        return formatTime(miliSec,second,minute);
    }

    public  String formatTime(int miliSec,int second, int minute){
        return String.format("%02d",minute)+" : "+ String.format("%02d",second)+" : "+ String.format("%02d",miliSec);
    }

    public void pauseGame(){
        dialogBuilder = new AlertDialog.Builder(this,R.style.dialog);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_game_pause,null);
        resumeButton = popupView.findViewById(R.id.resume_popup_btn);
        restartButton = popupView.findViewById(R.id.restart_popup_btn);
        homeButton = popupView.findViewById(R.id.home_popup_btn);

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        timerTask.cancel();

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starTimer();
                dialog.dismiss();
            }
        });
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    public void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    public void playAgain(){
        finish();
        startActivity(getIntent());
    }

    public void shuffleButtonGraphics(){
        Random rand = new Random();
        for (int i = 0 ; i < numberOfElements ; i++){
            buttonGraphicLocation[i] = i % (numberOfElements/2); // mod by unique value เพราะมันมีภาพต่างกัน 3 ภาพ 6 คู่
        }
        for(int i = 0; i < numberOfElements ; i++){
            int temp = buttonGraphicLocation[i];
            int randindex = rand.nextInt(6); // 0-5 random int number

            // swap number of buttonGraphic
            buttonGraphicLocation[i] = buttonGraphicLocation[randindex];
            buttonGraphicLocation[randindex] = temp;

        }
    }

    public boolean checkAllMatched(){
        boolean checkAllMatched = true;
        for(int r = 0; r < numRows ; r++){
            for(int c = 0 ; c < numColumns ; c++){

                if( button[r * numColumns + c].isMatched == false){
                    checkAllMatched = false;
                }


            }
        }
        return checkAllMatched;
    }

    public void setButtonGraphic(){
        switch (mode){
            case -1: // easy mode
                buttonGraphic[0] = R.drawable.custom_pair_brownies;
                buttonGraphic[1] = R.drawable.custom_pair_cake;
                buttonGraphic[2] = R.drawable.custom_pair_candy;
                if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
                    buttonGraphictexts[0] = "brownies";
                    buttonGraphictexts[1] = "cakes";
                    buttonGraphictexts[2] = "candy";
                }
                break;
            case 0: // normal mode
                buttonGraphic[0] = R.drawable.custom_pair_brownies;
                buttonGraphic[1] = R.drawable.custom_pair_cake;
                buttonGraphic[2] = R.drawable.custom_pair_candy;
                buttonGraphic[3] = R.drawable.custom_pair_chocolate;
                buttonGraphic[4] = R.drawable.custom_pair_cookie;
                buttonGraphic[5] = R.drawable.custom_pair_donut;
                if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
                    buttonGraphictexts[0] = "brownies";
                    buttonGraphictexts[1] = "cakes";
                    buttonGraphictexts[2] = "candy";
                    buttonGraphictexts[3] = "chocolate";
                    buttonGraphictexts[4] = "cookies";
                    buttonGraphictexts[5] = "donut";
                }
                break;
            case 1: // hard mode
                buttonGraphic[0] = R.drawable.custom_pair_brownies;
                buttonGraphic[1] = R.drawable.custom_pair_cake;
                buttonGraphic[2] = R.drawable.custom_pair_candy;
                buttonGraphic[3] = R.drawable.custom_pair_chocolate;
                buttonGraphic[4] = R.drawable.custom_pair_cookie;
                buttonGraphic[5] = R.drawable.custom_pair_donut;
                buttonGraphic[6] = R.drawable.custom_pair_icecream;
                buttonGraphic[7] = R.drawable.custom_pair_macaron;
                buttonGraphic[8] = R.drawable.custom_pair_pancake;
                if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
                    buttonGraphictexts[0] = "brownies";
                    buttonGraphictexts[1] = "cakes";
                    buttonGraphictexts[2] = "candy";
                    buttonGraphictexts[3] = "chocolate";
                    buttonGraphictexts[4] = "cookies";
                    buttonGraphictexts[5] = "donut";
                    buttonGraphictexts[6] = "ice cream";
                    buttonGraphictexts[7] = "macaroon";
                    buttonGraphictexts[8] = "pancake";
                }
                break;
        }
    }
    public String matchedWord(){
        String[] matchedWord = { "awesome", "amazing", "excellent", "great", "matched", "incredible", "outstanding"};

        Random rand = new Random();
        int randIndex = rand.nextInt(matchedWord.length);

        return matchedWord[randIndex];
    }


    public void speak(String text){
        Handler handler = new Handler();
        if(textToSpeech.isSpeaking()){
            return;
        }
        else {
            isBusy = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBusy = false;
                    textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
                }
            }, 500);
        }
    }

    @Override
    public void onClick(View v) {
        if(isBusy || textToSpeech.isSpeaking()){ // มีการทำงานอยู่ไหม
            return;
        }
        MemoryButton button = (MemoryButton) v;
        if(button.isMatched) {
            return;
        }
        if(selectedButton1 == null ){ // ถ้ามันถูกกดแล้วไม่ busy และ ยังไม่ Mactch ให้มาเช็คว่ามีค่าใน bttn 1 หรือยัง
            selectedButton1 = button;
            selectedButton1.filpped();
            if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
                speak(selectedButton1.getSymbol() + " " +selectedButton1.getPosition());
            }
            return;
        }
        if(selectedButton1.getId() == button.getId()){
            return; // กดอันเดิมซ้ำๆมันก็ไม่ทำอะไร จนกว่าจะไปกดอันที่สอง
        }

        // วนเช็คว่า

        if(selectedButton1.getFrontDrawableId() == button.getFrontDrawableId() ){ // matched
            // flipped the second one to user
            button.filpped();
            // speak if in Accessibility mode
            if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
                speak(button.getSymbol() + " " + button.getPosition());
                speak(matchedWord());
            }


            selectedButton1.setMatched(true); // บอกว่ามันถูกจับคู่แล้ว
            button.setMatched(true);

            Drawable visible = getDrawable(R.drawable.custom_pair_item_disable);
            selectedButton1.setBackGroundButton(visible);
            button.setBackGroundButton(visible);


            selectedButton1.setEnabled(false); // ปิดปุ่มไม่ให้สามารถกดได้อีกต่อไป
            button.setEnabled(false);

            selectedButton1 = null; // ให้มันไปชี้ null เพื่อรอรับค่าใหม่

            // วนเช็คว่าทุก button == true แล้ว
            if(checkAllMatched() == true ){
                textToSpeech.shutdown();
                Intent intent = new Intent(this, EndgameActivity.class);
                intent.putExtra("TIME_SCORE",getTimerText());
                intent.putExtra("MODE",mode);
                finish();
                startActivity(intent);
            }


        }
        else {  // not matched

            if(textToSpeech.isSpeaking()) {
                return;
            }

            else {
                selectedButton2 = button;

                selectedButton2.filpped();
                // speak
                if (AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY") {
                    speak(selectedButton2.getSymbol() + " " + selectedButton2.getPosition());
                    // speak not matched
                    speak("not matched");
                }

                isBusy = true;

                final Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectedButton1.filpped();
                        selectedButton2.filpped();
                        selectedButton1 = null;
                        selectedButton2 = null;
                        isBusy = false;  // delay จบแล้ว
                    }
                }, 500);
            }
        }

    }
}
