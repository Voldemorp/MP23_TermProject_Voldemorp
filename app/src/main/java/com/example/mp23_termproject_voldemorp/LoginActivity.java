package com.example.mp23_termproject_voldemorp;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

// 파이어베이스 연동 관련 부분
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private SignUpDialog signUpDialog;
    Button loginBtn;
    EditText loginEmailEditText;
    EditText loginPasswordEditText;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;

    // 화면 터치시 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (view != null) {

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    // 로그아웃 하지 않을 시 자동 로그인, 회원가입시 바로 로그인 됨
    @Override
    public void onStart() {
        super.onStart();
        moveMainPage(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 파이어 베이스 인증 객체 선언 및 버튼 등록
        firebaseAuth = FirebaseAuth.getInstance();
        loginBtn = (Button) findViewById(R.id.loginButton);
        loginEmailEditText = findViewById(R.id.editTextEmailAddress);
        loginPasswordEditText = findViewById(R.id.editTextPassword);


        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // 회원 가입 버튼 화면 전환
        Button signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(view -> {
            signUpDialog = new SignUpDialog(this);
            signUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
            signUpDialog.show();
        });


        //                       --- 로그인  ---

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = loginEmailEditText.getText().toString().trim();
                String pwd = loginPasswordEditText.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // 로그인 성공시 메인화면으로 넘어감
                                if (task.isSuccessful()) {
                                    moveMainPage(FirebaseAuth.getInstance().getCurrentUser());
                                } else { // 로그인 실패시 토스트 메시지 출력
                                    Toast.makeText(LoginActivity.this, "이메일 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        });
    }



    // 로그인 성공 -> 유저정보 넘겨주고 메인 화면 호출하는 함수
    public void moveMainPage(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(intent);

            // 화면 전환 시 왼쪽에서 오른쪽으로 밀듯이 나타나는 애니메이션 적용
            overridePendingTransition(R.anim.slide_right_enter, R.anim.none);

            finish();
        }
    }
}