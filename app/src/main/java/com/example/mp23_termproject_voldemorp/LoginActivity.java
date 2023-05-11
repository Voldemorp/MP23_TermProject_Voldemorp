package com.example.mp23_termproject_voldemorp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private SignUpDialog signUpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // 회원 가입 버튼 클릭 이벤트 -> 화면 전환
        Button sign_up_btn = (Button) findViewById(R.id.signUpBtn);
        sign_up_btn.setOnClickListener(view -> {
            signUpDialog = new SignUpDialog(this);
            signUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
            signUpDialog.show();
        });

        Button login_btn = (Button) findViewById(R.id.loginButton);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                startActivity(intent);

                // 화면 전환 시 왼쪽에서 오른쪽으로 밀듯이 나타나는 애니메이션 적용
                overridePendingTransition(R.anim.slide_right_enter, R.anim.none);
            }
        });

    }
}