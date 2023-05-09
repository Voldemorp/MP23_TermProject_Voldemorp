package com.example.mp23_termproject_voldemorp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // 변경사항 적용 없이 로그인 창으로 돌아가는 버튼
        Button back_to_login_btn = (Button) findViewById(R.id.backToLoginBtn);
        back_to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                // 화면 전환 시 회원가입 창이 내려가는 애니매이션 적용... 하고싶은데......
                overridePendingTransition(R.anim.none, R.anim.slide_down_exit);
            }
        });


    }
}