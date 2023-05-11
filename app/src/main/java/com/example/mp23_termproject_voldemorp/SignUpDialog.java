package com.example.mp23_termproject_voldemorp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SignUpDialog extends Dialog {

    public SignUpDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.activity_sign_up);

        // 변경사항 적용 없이 로그인 창으로 돌아가는 버튼
        Button backToLoginBtn = (Button) findViewById(R.id.backToLoginBtn);
        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // 변경사항 적용해서 회원가입 완료시켜주는 버튼
        Button signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}