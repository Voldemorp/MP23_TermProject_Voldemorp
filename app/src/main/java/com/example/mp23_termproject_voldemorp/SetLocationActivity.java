package com.example.mp23_termproject_voldemorp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SetLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        // 바텀 시트 화면에 뿌려줌
        SetLocationBottomSheet setLocationBottomSheet = new SetLocationBottomSheet();
        setLocationBottomSheet.show(getSupportFragmentManager(), "setLocationBottomSheet");


        // 로그인으로 돌아가기 버튼 눌렀을 때 이벤트 -> 로그인 화면으로 화면 전환
        Button backToLoginBtn = (Button) findViewById(R.id.setLocationBackToLoginBtn);
        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                // 화면 전환 시 오른쪽에서 왼쪽으로 밀듯이 나타나는 애니메이션 적용
                overridePendingTransition(R.anim.slide_left_enter, R.anim.none);
            }
        });

        // 눌렀을 때 바텀시트 올라오게 하는 버튼
        Button openBottomSheetBtn = (Button) findViewById(R.id.openBottomSheetBtn);
        openBottomSheetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}