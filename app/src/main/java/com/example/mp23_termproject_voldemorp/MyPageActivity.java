package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {
    private TextView nicknameTextView;
    private TextView nicknameTextViewBadge;
    private TextView nicknameTextViewPort;
    private TextView addressTextView;
    ArrayList<MyPagePortItem> portList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // 서버에서 사용자 정보 불러옴
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        // *-- 닉네임 표시 --*
        nicknameTextView = findViewById(R.id.nicknameTextview);
        nicknameTextViewBadge = findViewById(R.id.nicknameTextviewBadge);
        nicknameTextViewPort = findViewById(R.id.nicknameTextviewPort);
        userRef.child(userId).child("nickName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()) {
                    // 불러온 닉네임을 텍스트뷰에 설정
                    nicknameTextView.setText(dataSnapshot.getValue(String.class));
                    nicknameTextViewBadge.setText(dataSnapshot.getValue(String.class));
                    nicknameTextViewPort.setText(dataSnapshot.getValue(String.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // *-- 주소 표시 --*
        addressTextView = findViewById(R.id.addressTextview);
        userRef.child(userId).child("address1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()) {
                    // 불러온 주소를 텍스트뷰에 설정
                    addressTextView.setText(dataSnapshot.getValue(String.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // *-- 메인 뱃지 표시 --*

        // *-- 프로필 사진 표시 --*

        // 프로필편집 화면 전환
        Button editProfileBtn = (Button) findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

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