package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;



public class EditProfileActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // 취소 버튼
        Button cancleBtn = (Button) findViewById(R.id.cancleBtn);
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        // 확인 버튼
        Button setBtn = (Button) findViewById(R.id.setBtn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MyPageActivity.class);
                Toast.makeText(EditProfileActivity.this, "프로필 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        // 서버로부터 유저의 uid, 닉네임 받아옴
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference nickNameRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("nickName");

        //  ---- 사용자 닉네임 변경 ----
        EditText EditNicknameText = (EditText)findViewById(R.id.EditNicknameText);
        Button setNicknameBtn = (Button) findViewById(R.id.setNicknameBtn);


        setNicknameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editNickname = EditNicknameText.getText().toString().trim();
                nickNameRef.setValue(editNickname).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "닉네임이 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "닉네임이 변경되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });





    }




}