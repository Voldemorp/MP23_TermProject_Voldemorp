package com.example.mp23_termproject_voldemorp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnScreen1, btnScreen2, btnScreen3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setContentView(R.layout.activity_toolbar);
//        btnScreen1 = findViewById(R.id.btnScreen1);
//        btnScreen2 = findViewById(R.id.btnScreen2);
//        btnScreen3 = findViewById(R.id.btnScreen3);
//        btnScreen1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showScreen1();
//            }
//        });
//
//        btnScreen2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showScreen2();
//            }
//        });
//
//        btnScreen3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showScreen3();
//            }
//        });
    }
    private void showScreen1() {
        Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
        startActivity(intent);
    }

    private void showScreen2() {
        // Screen 2를 띄우는 로직 작성
    }

    private void showScreen3() {
        // Screen 3을 띄우는 로직 작성

    }
}