package com.example.mp23_termproject_voldemorp;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {
    ArrayList<MyPagePortItem> portList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // 마이페이지에 있는 port list에 띄울 정보를 저장하는 배열
        portList = new ArrayList<>();

        //[서버] DB에서 사용자가 port한 식당 이름/몇번 port했는지 가져와서 portList에 append해야 함

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // for문 돌면서 추가
        for (int i = 0; i < portList.size(); i++) {
            // 추가할 레이아웃
            MyPageListLayout myPageListLayout = new MyPageListLayout(getApplicationContext(), portList.get(i));
            // 추가할 위치
            LinearLayout layout = findViewById(R.id.portView);
            // 추가 코드
            layout.addView(myPageListLayout);
        }

    }
}