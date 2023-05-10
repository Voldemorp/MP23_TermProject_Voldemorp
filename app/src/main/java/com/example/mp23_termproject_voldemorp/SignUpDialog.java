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
        Button back_to_login_btn = (Button) findViewById(R.id.backToLoginBtn);
        back_to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // 변경사항 적용해서 회원가입 완료시켜주는 버튼
        Button sign_up_confirm_btn = (Button) findViewById(R.id.signUpConfirmBtn);
        sign_up_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}