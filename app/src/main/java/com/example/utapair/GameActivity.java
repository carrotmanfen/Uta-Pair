package com.example.utapair;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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
/* this class is about GameActivity
* that we can play game in any mode
* this have all method that we use */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private int numberOfElements;
    private MemoryButton[] button; // ปุ่มที่จะเอาขึ้นไปวางไว้บน GridLayout
    private int[] buttonGraphicLocation; // ตำแหน่ง Graphic ของ Button แต่ละตัวที่จะนำไป drawable
    private int[] buttonGraphic; // เก็บภาพจาก drawable มาใส่ array ไว้
    private String[] buttonGraphicTexts;
    private MemoryButton buttonSelected1;
    private MemoryButton buttonSelected2;
    private int numRows;
    private int numColumns;
    private Boolean itWasBusy = false;

    /* variable for constructor */
    private int mode;
    private int layoutId;
    private int gridId;

    private ImageButton buttonPause;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button buttonResume, buttonRestart, buttonHome;
    private TextView textViewTimer;
    private Timer timer;
    private TimerTask timerTask;
    private int time = 0;
    private TextToSpeech textToSpeech;
    private int tapCount = 0;

    private MediaPlayer mediaPlayer;

    @Override
    /* this part will run when create this Activity */
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        /* keep data from inputExtra to variable */
        Bundle bundle = getIntent().getExtras();
        mode = bundle.getInt("MODE");
        layoutId = bundle.getInt("LAYOUT_ID");
        gridId = bundle.getInt("GRID_ID");

        setContentView(layoutId);    /* set layout */
        GridLayout gridLayout = (GridLayout) findViewById(gridId);     /* set grid layout */
        /* set column and row to grid */
        numColumns = gridLayout.getColumnCount();
        numRows = gridLayout.getRowCount();

        gridLayout.setUseDefaultMargins(true);    /* use grid default if want to special must implement*/
        numberOfElements = numRows * numColumns;   /* set number of all element pair_item */

        setMusic();

        button = new MemoryButton[numberOfElements];    /* create MemoryButton objects same number of element */

        /* set graphic half of element because 1 picture have 2 pair item to matched */
        buttonGraphic = new int[numberOfElements/2];
        buttonGraphicTexts = new String[numberOfElements/2];

        /* set language for Accessibility ion this case we use US */
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        setButtonGraphic();     /* generate graphic in any level */

        buttonGraphicLocation = new int[numberOfElements];      /* create array to keep data of location */

        shuffleButtonGraphics();        /* random location */

        /* set button in to location */
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numColumns; c++){
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphic[ buttonGraphicLocation[r * numColumns+c] ]);
                tempButton.setSymbol(buttonGraphicTexts[ buttonGraphicLocation[r * numColumns+c] ]);
                tempButton.setPosition(r+1,c+1);
                tempButton.setId(View.generateViewId());    /* create id for matched */
                tempButton.setOnClickListener(this);
                button[r * numColumns+c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }

        /* set button pause */
        buttonPause = findViewById(R.id.pause_btn);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            /* when click pause the game */
            public void onClick(View view) {
                pauseGame();
            }
        });

        /* set textViewTimer */
        textViewTimer = findViewById(R.id.time_text);
        /* timer and keep score */
        timer = new Timer();
        startTimer();

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
                    /* play sound follow level */
                    if(mode==-1){
                        String text = "Start level easy";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else if(mode==0){
                        String text = "Start level normal";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else{
                        String text = "Start level hard";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }, 500);
        }
    }

    /* method to start timer and setText */
    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    /* when timer is counting continuous running time and change text */
                    public void run() {
                        textViewTimer.setText(getTimerText());      /* change text */
                        time++;     /* running time */
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1);       /* set speed od timerTask */
    }

    /* method to change time to millisecond, second and minute */
    public String getTimerText() {
        /* Change unite and put in to formatTime that we want */
        int milliSec = time%100;
        int second = (time/1000)%60;
        int minute = (time/1000)/60;
        return formatTime(milliSec,second,minute);
    }

    /* method get time and set in to string format that we want */
    public  String formatTime(int milliSec,int second, int minute){
        /* set string format */
        return String.format("%02d",minute)+" : "+ String.format("%02d",second)+" : "+ String.format("%02d",milliSec);
    }

    /* method to pause game */
    public void pauseGame(){

        /* stop music */
        mediaPlayer.stop();

        /* set id of dialog */
        dialogBuilder = new AlertDialog.Builder(this,R.style.dialog);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_game_pause,null);     /* set layout of dialog */

        /* build dialog and show it */
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();


        timerTask.cancel(); /* stop timer */

        /* set buttonResume */
        buttonResume = popupView.findViewById(R.id.resume_popup_btn);
        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            /* when click resume game and continuous timer */
            public void onClick(View view) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    tapCount++;     /* when tap button count in tapCount */
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /* if a tap play sound */
                            if (tapCount==1){
                                String text = "double tap to resume";
                                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                            }
                            /* if double tap in time go to MainActivity */
                            else if(tapCount==2){
                                startTimer();       /* continuous timer */
                                dialog.dismiss();       /* close dialog */
                                mediaPlayer.start();   /* continue music */

                            }
                            tapCount = 0;   /* reset tapCount */
                        }
                    },500);     /* in 500 millisecond */
                }
                else {
                    startTimer();       /* continuous timer */
                    dialog.dismiss();       /* close dialog */
                    mediaPlayer.start(); /* continue music */
                }
            }
        });

        /* set buttonRestart */
        buttonRestart = popupView.findViewById(R.id.restart_popup_btn);
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            /* when click restart game */
            public void onClick(View v) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    playAgainAccessibility();
                }
                else {
                    playAgain();    /* restart game */
                }
            }
        });

        /* set buttonHome */
        buttonHome = popupView.findViewById(R.id.home_popup_btn);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            /* when click go to MainActivity */
            public void onClick(View v) {
                /* use method follow AccessibilityMode */
                if(AccessibilityMode.getInstance().getMode()=="ACCESSIBILITY") {
                    openMainAccessibility();
                }
                else {
                    openMainActivity();
                }
            }
        });
    }
    /* set music when user play a game */
    public void setMusic(){
        mediaPlayer = MediaPlayer.create(this, R.raw.in_game); //*** put the music here !!!
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }


    /* method go to MainActivity */
    public void openMainActivity(){
        mediaPlayer.stop();
        Intent intent = new Intent(this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    /* method to MainActivity with AccessibilityMode */
    public void openMainAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to go to home";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time go to MainActivity */
                else if(tapCount==2){
                    openMainActivity();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    /* method to restart game */
    public void playAgain(){
        mediaPlayer.stop();
        finish();
        startActivity(getIntent());
    }

    /* method to playAgain with AccessibilityMode */
    public void playAgainAccessibility(){
        tapCount++;     /* when tap button count in tapCount */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* if a tap play sound */
                if (tapCount==1){
                    String text = "double tap to restart game";
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
                /* if double tap in time to restart */
                else if(tapCount==2){
                    playAgain();
                }
                tapCount = 0;   /* reset tapCount */
            }
        },500);     /* in 500 millisecond */
    }

    /* method to random location */
    public void shuffleButtonGraphics(){
        Random rand = new Random();
        for (int i = 0 ; i < numberOfElements ; i++){
            /* mod by unique value because it have different picture (3 picture and  6 pair) */
            buttonGraphicLocation[i] = i % (numberOfElements/2);
        }
        for(int i = 0; i < numberOfElements ; i++){
            int temp = buttonGraphicLocation[i];
            int randIndex = rand.nextInt(6); /* 0-5 random int number */

            /* swap number of buttonGraphic */
            buttonGraphicLocation[i] = buttonGraphicLocation[randIndex];
            buttonGraphicLocation[randIndex] = temp;

        }
    }

    /* method to check all pair item match (end game) */
    public boolean checkAllMatched(){
        boolean checkAllMatched = true;
        /* for loop all pair item and check matched all of them*/
        for(int r = 0; r < numRows ; r++){
            for(int c = 0 ; c < numColumns ; c++){
                /* if have at least 1 pair that not match return false */
                if( button[r * numColumns + c].isMatched == false){
                    checkAllMatched = false;
                }
            }
        }
        /* if all pair item matched return true and that mean end game */
        return checkAllMatched;
    }

    /* set buttonGraphic level easy */
    public void setButtonGraphicLevelEasy(){
        /* set drawable each buttonGraphic */
        buttonGraphic[0] = R.drawable.custom_pair_brownies;
        buttonGraphic[1] = R.drawable.custom_pair_cake;
        buttonGraphic[2] = R.drawable.custom_pair_candy;
        /* if AccessibilityMode on set buttonGraphicTexts */
        if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
            /* set text each buttonGraphic */
            buttonGraphicTexts[0] = "brownies";
            buttonGraphicTexts[1] = "cakes";
            buttonGraphicTexts[2] = "candy";
        }
    }

    /* set buttonGraphic level normal */
    public void setButtonGraphicLevelNormal(){
        /* set drawable each buttonGraphic */
        buttonGraphic[0] = R.drawable.custom_pair_brownies;
        buttonGraphic[1] = R.drawable.custom_pair_cake;
        buttonGraphic[2] = R.drawable.custom_pair_candy;
        buttonGraphic[3] = R.drawable.custom_pair_chocolate;
        buttonGraphic[4] = R.drawable.custom_pair_cookie;
        buttonGraphic[5] = R.drawable.custom_pair_donut;
        /* if AccessibilityMode on set buttonGraphicTexts */
        if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
            /* set text each buttonGraphic */
            buttonGraphicTexts[0] = "brownies";
            buttonGraphicTexts[1] = "cakes";
            buttonGraphicTexts[2] = "candy";
            buttonGraphicTexts[3] = "chocolate";
            buttonGraphicTexts[4] = "cookies";
            buttonGraphicTexts[5] = "donut";
        }
    }

    /* set buttonGraphic level hard */
    public void setButtonGraphicLevelHard(){
        /* set drawable each buttonGraphic */
        buttonGraphic[0] = R.drawable.custom_pair_brownies;
        buttonGraphic[1] = R.drawable.custom_pair_cake;
        buttonGraphic[2] = R.drawable.custom_pair_candy;
        buttonGraphic[3] = R.drawable.custom_pair_chocolate;
        buttonGraphic[4] = R.drawable.custom_pair_cookie;
        buttonGraphic[5] = R.drawable.custom_pair_donut;
        buttonGraphic[6] = R.drawable.custom_pair_icecream;
        buttonGraphic[7] = R.drawable.custom_pair_macaron;
        buttonGraphic[8] = R.drawable.custom_pair_pancake;
        /* if AccessibilityMode on set buttonGraphicTexts */
        if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
            /* set text each buttonGraphic */
            buttonGraphicTexts[0] = "brownies";
            buttonGraphicTexts[1] = "cakes";
            buttonGraphicTexts[2] = "candy";
            buttonGraphicTexts[3] = "chocolate";
            buttonGraphicTexts[4] = "cookies";
            buttonGraphicTexts[5] = "donut";
            buttonGraphicTexts[6] = "ice cream";
            buttonGraphicTexts[7] = "macaroon";
            buttonGraphicTexts[8] = "pancake";
        }
    }

    /* method set buttonGraphic all level */
    public void setButtonGraphic(){
        switch (mode){
            case -1: /* easy mode */
                setButtonGraphicLevelEasy();
                break;
            case 0: /* normal mode */
                setButtonGraphicLevelNormal();
                break;
            case 1: /* hard mode */
                setButtonGraphicLevelHard();
                break;
        }
    }

    /* method random word to speech when matched and in AccessibilityMode on */
    public String matchedWord(){
        /* create some String */
        String[] matchedWord = { "awesome", "amazing", "excellent", "great", "matched", "incredible", "outstanding"};
        /* random and return it */
        Random rand = new Random();
        int randIndex = rand.nextInt(matchedWord.length);
        return matchedWord[randIndex];
    }

    /* method to speech */
    public void speak(String text){
        Handler handler = new Handler();
        /* if speaking not speak */
        if(textToSpeech.isSpeaking()){
            return;
        }
        else {  /* if note speaking make it speak */
            itWasBusy = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    itWasBusy = false;
                    textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
                }
            }, 500);
        }
    }

    @Override
    /* override onClick method */
    public void onClick(View v) {
        /* if speaking do nothing */
        if(itWasBusy || textToSpeech.isSpeaking()){
            return;
        }
        /* create MemoryButton */
        MemoryButton button = (MemoryButton) v;
        if(button.isMatched) {
            return;
        }
        /* if click adnt busy and not matched then check it have data in bttn 1 yet */
        if(buttonSelected1 == null ){
            buttonSelected1 = button;
            buttonSelected1.flipped();      /* flipped button */
            /* if AccessibilityMode on speak symbol and position */
            if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
                speak(buttonSelected1.getSymbol() + " " +buttonSelected1.getPosition());
            }
            return;
        }
        /* make click same button again to do nothing */
        if(buttonSelected1.getId() == button.getId()){
            return;
        }
        if(buttonSelected1.getFrontDrawableId() == button.getFrontDrawableId() ){ /* matched */
            /* flipped the second one to user */
            button.flipped();
            /* speak if in Accessibility mode */
            if(AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY"){
                speak(button.getSymbol() + " " + button.getPosition());
                speak(matchedWord());
            }
            /* tell it that was matched */
            buttonSelected1.setMatched(true);
            button.setMatched(true);
            /* when matched disable background */
            Drawable visible = getDrawable(R.drawable.custom_pair_item_disable);
            buttonSelected1.setBackGroundButton(visible);
            button.setBackGroundButton(visible);
            /* when match disable button to can not click */
            buttonSelected1.setEnabled(false);
            button.setEnabled(false);
            buttonSelected1 = null; /* set to null for getting new data */

            /* if all button matched */
            if(checkAllMatched() == true ){
                /* end game and go to EndgameActivity */
                textToSpeech.shutdown();
                mediaPlayer.stop();
                Intent intent = new Intent(this, EndgameActivity.class);
                intent.putExtra("TIME_SCORE",getTimerText());
                intent.putExtra("MODE",mode);
                finish();
                startActivity(intent);
            }
        }
        else {  /* not matched */
            if(textToSpeech.isSpeaking()) {
                return;
            }
            else {
                buttonSelected2 = button;
                buttonSelected2.flipped();      /* flipped button */
                /* if AccessibilityMode on speak not matched */
                if (AccessibilityMode.getInstance().getMode() == "ACCESSIBILITY") {
                    speak(buttonSelected2.getSymbol() + " " + buttonSelected2.getPosition());
                    /* speak not matched */
                    speak("not matched");
                }
                itWasBusy = true;       /* set to busy */
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    /* flipped back and set buttonSelect to null for getting new data */
                    public void run() {
                        buttonSelected1.flipped();
                        buttonSelected2.flipped();
                        buttonSelected1 = null;
                        buttonSelected2 = null;
                        itWasBusy = false;  /* end of delay */
                    }
                }, 500);
            }
        }

    }

}
