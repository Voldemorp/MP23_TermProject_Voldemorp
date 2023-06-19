package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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

     //  *--자동 로그인--*  || 로그아웃 하지 않을시, 회원가입시 바로 로그인
    @Override
    public void onStart() {
        super.onStart();
        movePage(FirebaseAuth.getInstance().getCurrentUser());
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

        // 로그인 버튼 클릭 이벤트 -> 화면 전환
        Button loginBtn = (Button) findViewById(R.id.loginButton);

        //                       --- 로그인  ---
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // 화면 전환 시 왼쪽에서 오른쪽으로 밀듯이 나타나는 애니메이션 적용
//                overridePendingTransition(R.anim.slide_right_enter, R.anim.none);
                String email = loginEmailEditText.getText().toString().trim();
                String pwd = loginPasswordEditText.getText().toString().trim();

                //로그인 정보 다 입력하지 않았을 경우 예외처리
                if(email.isEmpty()||pwd.isEmpty())
                    Toast.makeText(getApplicationContext(),"로그인 정보를 확인해주세요",Toast.LENGTH_SHORT).show();
                else {
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // 로그인 성공시 화면 전환
                                    if (task.isSuccessful()) {
                                        movePage(FirebaseAuth.getInstance().getCurrentUser());
                                    } else { // 로그인 실패시 토스트 메시지 출력
                                        Toast.makeText(LoginActivity.this, "이메일 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }



    // *-- 로그인 성공 -> 유저정보 넘겨주고 화면 이동하는 함수 --*
    public void movePage(FirebaseUser user) {
        if (user != null) {
            checkIfAddressExists();

//             화면 전환 시 왼쪽에서 오른쪽으로 밀듯이 나타나는 애니메이션 적용
            overridePendingTransition(R.anim.slide_right_enter, R.anim.none);
//            finish();
        }
    }

    // *-- 로그인한 사용자의 주소 정보가 저장돼있는지 확인하는 함수 --*
    private void checkIfAddressExists() {
        // [서버] 현재 로그인한 사용자의 Userid를 사용하여 DB에서 해당 사용자의 주소 정보 가져옴
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("address1");

        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 주소 정보 "address1"(필수 주소)가 존재할 시
                if (dataSnapshot.exists()) {
                    // 주소 저장 되어 있으면 메인 화면으로 이동
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // 주소 저장이 되어있지 않으면 지역설정 화면으로 이동
                    Intent intent = new Intent(getApplicationContext(), SetLocationActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 에러 처리 로직을 여기에 작성
            }
        });

    }

}