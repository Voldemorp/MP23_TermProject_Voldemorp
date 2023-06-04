package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.List;

public class MyPageActivity extends AppCompatActivity {
    private TextView nicknameTextView;
    private TextView nicknameTextViewBadge;
    private TextView nicknameTextViewPort;
    private TextView addressTextView;
    private String userId;
    private DatabaseReference userPortRef;
    private List<MyPagePortItem> portList = new ArrayList<>();

    private ValueEventListener portValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // 데이터 변경 시 호출되는 로직을 작성합니다.
            // 예: 데이터 스냅샷에서 필요한 데이터를 가져와 처리합니다.
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // 취소되었을 때 호출되는 로직을 작성합니다.
            // 예: 오류 처리 로직을 수행합니다.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // 서버에서 사용자 정보 불러옴
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                    String nickname = dataSnapshot.getValue(String.class);
                    nicknameTextView.setText(nickname);
                    nicknameTextViewBadge.setText(nickname);
                    nicknameTextViewPort.setText(nickname);
                }
                else { System.out.println("none"); }
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
        Button editProfileBtn = findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });


        // *-- 프로필 메인뱃지 표시 --*
        ImageView profileImageView = findViewById(R.id.profileImageView);



        // 메인으로 가는 버튼 이벤트
        Button backToMainBtn = findViewById(R.id.backToMainBtn);
        backToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        // 마이페이지에 있는 port list에 띄울 정보를 저장하는 배열

        //[서버] DB에서 사용자가 port한 식당 이름/몇 번 port했는지 가져와서 portList에 append해야 함
        DatabaseReference userPortRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("restaurant");
        userPortRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot document : dataSnapshot.getChildren()) {
                        String restaurantName = document.getKey(); // 식당 이름 가져오기

                        // 가게 이름과 portNum을 가져와서 portList에 추가
                        DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("restaurant").child(restaurantName);
                        restaurantRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    int portNum = dataSnapshot.child("portNum").getValue(Integer.class); // 가게의 portNum 가져오기

                                    // portList에 추가
                                    MyPagePortItem portItem = new MyPagePortItem(restaurantName, portNum);
                                    portList.add(portItem);

                                    // 추가한 식당 정보를 레이아웃에 추가
                                    addPortListToLayout();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // 오류 처리
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 오류 처리
            }
        });

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void addPortListToLayout() {
        LinearLayout layout = findViewById(R.id.portListContainer);

        for (int i = 0; i < portList.size(); i++) {
            // 추가할 레이아웃
            MyPageListLayout myPageListLayout = new MyPageListLayout(getApplicationContext(), null, portList.get(i));

            // 추가 코드
            layout.addView(myPageListLayout);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 이벤트 리스너 제거
        if (userPortRef != null) {
            userPortRef.removeEventListener(portValueEventListener);
        }
    }
}

