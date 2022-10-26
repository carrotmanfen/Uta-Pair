//package com.example.utapair;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class MenuActivity extends AppCompatActivity {
//
//    private Button easyButton; // dimension = ? 2 rows , 3 cols
//    private Button mediumButton; // dimension = ?
//    private Button HardButton; // dimension = ?
//
//    @Override
//    protected void onCreate(Bundle saveInstanceState){
//        super.onCreate(saveInstanceState);
//        setContentView(R.layout.activity_select_level); // หน้า menu file layout
//
//        easyButton = (Button) findViewById(R.id.easy_btn);
//        // if you wanna try new method just learn it
//
//        easyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//}
