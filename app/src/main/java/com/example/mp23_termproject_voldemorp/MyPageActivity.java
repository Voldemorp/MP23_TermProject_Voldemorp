package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
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

import java.util.LinkedHashMap;
import java.util.Map;


public class MyPageActivity extends AppCompatActivity {
    private TextView nicknameTextView;
    private TextView nicknameTextViewBadge;
    private TextView nicknameTextViewPort;
    static public TextView addressTextView;
    static public String address;
    private ImageView profileImageView;
    private TextView mainBadgeTextView;
    private TextView textView13;
    private TextView textView14;
    private String userId;
    private DatabaseReference userPortRef;
    private LinkedHashMap<String, MyPagePortItem> portList = new LinkedHashMap<>();

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


        // *-- 프로필 대표뱃지 사진 표시 --*
        profileImageView = findViewById(R.id.profileImageView);
        loadSelectedImageFromSharedPreferences();   // SharedPreferences에서 저장된 대표뱃지 가져와서 설정


        // *-- 프로필 대표뱃지 Text 표시 --*
        mainBadgeTextView = findViewById(R.id.mainBadgeTextView);
        textView13 = findViewById(R.id.textView13);
        textView14 = findViewById(R.id.textView14);

        userRef.child(userId).child("badge").child("mainBadge")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()) {
                    String mainBadge = dataSnapshot.getValue(String.class);
                    if ("".equals(mainBadge)) {
                        textView13.setVisibility(View.GONE);
                        textView14.setVisibility(View.GONE);
                    } else {
                        textView13.setVisibility(View.VISIBLE);
                        textView14.setVisibility(View.VISIBLE);
                    }
                    // 불러온 대표뱃지를 텍스트뷰에 설정
                    mainBadgeTextView.setText(mainBadge);
                }
                else { System.out.println("none"); }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

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
                    address=dataSnapshot.getValue(String.class);

                    // onCreate 메서드 내부에서 아래 코드 추가
                    Intent intent = getIntent();
                    String address2 = intent.getStringExtra("address");
                    if (address2 != null) {
                        addressTextView.setText(address2);  // 주소 값을 텍스트뷰에 설정
                    }
                    else
                    addressTextView.setText(address);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        // *-- 뱃지 목록 표시 --*
        ImageView badgeForMania = findViewById(R.id.badgeForMania);
        ImageView badgeForMaster = findViewById(R.id.badgeForMaster);
        ImageView badgeForFirstRecommend = findViewById(R.id.badgeForFirstRecommend);
        ImageView badgeForTenRecommend = findViewById(R.id.badgeForTenRecommend);
        ImageView badgeForHundredRecommend = findViewById(R.id.badgeForHundredRecommend);
        ImageView badgeForFirstVisit = findViewById(R.id.badgeForFirstVisit);
        ImageView badgeForTenVisit = findViewById(R.id.badgeForTenVisit);
        ImageView badgeForHundredVisit = findViewById(R.id.badgeForHundredVisit);
        ImageView badgeForFirstPhoto = findViewById(R.id.badgeForFirstPhoto);
        ImageView badgeForTenPhoto = findViewById(R.id.badgeForTenPhoto);
        ImageView badgeForHundredPhoto = findViewById(R.id.badgeForHundredPhoto);

        badgeForMania.setVisibility(View.VISIBLE);
        badgeForMaster.setVisibility(View.VISIBLE);
        badgeForFirstRecommend.setVisibility(View.VISIBLE);
        badgeForTenRecommend.setVisibility(View.VISIBLE);

        badgeForHundredRecommend.setVisibility(View.VISIBLE);
        badgeForHundredRecommend.setImageResource(R.drawable.badge_none);

        badgeForFirstVisit.setVisibility(View.VISIBLE);
        badgeForTenVisit.setVisibility(View.VISIBLE);

        badgeForHundredVisit.setVisibility(View.VISIBLE);
        badgeForHundredVisit.setImageResource(R.drawable.badge_none);

        badgeForFirstPhoto.setVisibility(View.VISIBLE);
        badgeForFirstPhoto.setImageResource(R.drawable.badge_none);

        badgeForTenPhoto.setVisibility(View.VISIBLE);
        badgeForTenPhoto.setImageResource(R.drawable.badge_none);

        badgeForHundredPhoto.setVisibility(View.VISIBLE);
        badgeForHundredPhoto.setImageResource(R.drawable.badge_none);

// 파이어베이스에서 현재 사용자의 배지 값을 가져와서 적절한 가시성을 설정합니다.
//        DatabaseReference badgeRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("badge").child("badgeForMania");
//        badgeRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    boolean isBadgeVisible = dataSnapshot.getValue(Boolean.class);
//
//                    if (isBadgeVisible) {
//                        // 배지가 보여야 하는 경우
//                        badgeForMania.setVisibility(View.VISIBLE);
//                    } else {
//                        // 배지가 숨겨야 하는 경우
//                        badgeForMania.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // 오류 처리
//            }
//        });


        // *-- Port list 표시 --*
            //[서버] DB에서 사용자가 port한 식당 이름/몇 번 port했는지 가져와서 portList에 append
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
                                    portList.put(restaurantName, portItem);

                                    // 추가한 식당 정보를 레이아웃에 추가
                                    addPortListToLayout();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



        // '프로필 편집' 버튼 화면전환
        Button editProfileBtn = findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // '메인으로 돌아가기' 버튼 화면전환
        Button backToMainBtn = findViewById(R.id.backToMainBtn);
        backToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


    }




    // SharedPreferences에서 이미지를 불러오고 설정하는 메서드
    private void loadSelectedImageFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String selectedImageString = preferences.getString("selectedImage", null);
        if (selectedImageString != null) {
            // 문자열로 저장된 이미지를 Bitmap으로 변환하여 Drawable로 설정
            Bitmap bitmap = stringToBitmap(selectedImageString);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            profileImageView.setImageDrawable(drawable);
        }
    }

    // 문자열을 Bitmap으로 변환하는 메서드
    private Bitmap stringToBitmap(String imageString) {
        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }


    private void addPortListToLayout() {
        LinearLayout layout = findViewById(R.id.portListContainer);
        int index = 0;
        int size = portList.size();

        for (Map.Entry<String, MyPagePortItem> entry : portList.entrySet()) {
            index++;

            // 마지막 요소인 경우 추가 작업 실행
            if (index == size) {
                String key = entry.getKey();
                MyPagePortItem value = entry.getValue();

                System.out.println(key + " " + value);

                // 추가할 레이아웃
                MyPageListLayout myPageListLayout = new MyPageListLayout(getApplicationContext(), null, value);

                // 추가 코드
                layout.addView(myPageListLayout);
            }
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


