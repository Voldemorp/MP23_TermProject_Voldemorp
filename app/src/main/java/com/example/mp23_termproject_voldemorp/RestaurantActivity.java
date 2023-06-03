package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.*;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class RestaurantActivity extends AppCompatActivity {

    //현재 사용자 위치
    static double latitude;
    static double longitude;

    //식당 위치
    private double res_lat;
    private double res_long;
    private ViewPager2 viewPager;
    private FrameLayout restaurantContainer;

    private Button portButton;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase mDatabase;

    private int portNum = 0;

    private boolean likeCheck = false;

    private int postPortNum;

    public class restaurantModel{

        public int portNum = 0;

        public boolean likeCheck = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // MainActivity에서 전달된 인텐트 가져오기
        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("name");
        String restaurantType = intent.getStringExtra("type");
        double res_lat = intent.getDoubleExtra("res_lat",0.0);
        double res_long = intent.getDoubleExtra("res_long",0.0);


        //식당 이름
        TextView resNameText=(TextView)findViewById(R.id.textView2);
        //음식 타입
        TextView foodTypeText=(TextView)findViewById(R.id.textView3);

        resNameText.setText(restaurantName);
        foodTypeText.setText(restaurantType);

        // 현재 내 위치 인텐트에서 위도(latitude)와 경도(longitude) 값 추출(
//        latitude = intent.getDoubleExtra("latitude", 0.0);
//        longitude = intent.getDoubleExtra("longitude", 0.0);

        //파이어베이스 설정
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        portButton=findViewById(R.id.portButton);

        portButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //팝업창 띄우는걸로 바꾸기
                Toast.makeText(getApplicationContext(),"port success",Toast.LENGTH_SHORT).show();

                //유저 DB 업데이트

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String userId = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference reference = database.getReference("users").child(userId).child("restaurant").child(restaurantName);

                // 데이터 존재 여부 확인
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // 식당이 이미 유저DB에 있는 경우
                        if (dataSnapshot.exists()) {
                            updateRestaurant();
                        }
                        // 식당이 유저DB에 없는 경우
                        else {
                            addRestaurant();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 에러 처리
                        Toast.makeText(getApplicationContext(), "데이터베이스 읽기 취소됨: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                //여기에 식당 DB 업데이트
            }

            public void addRestaurant(){
                String userId = firebaseAuth.getCurrentUser().getUid();

                restaurantModel restaurantModel = new restaurantModel();

                restaurantModel.portNum = 1;
                restaurantModel.likeCheck = false;

                mDatabase.getReference().child("users").child(userId).child("restaurant").child(restaurantName).setValue(restaurantModel);

            }
            public void updateRestaurant() {

                String userId = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("restaurant").child(restaurantName).child("portNum");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        postPortNum = dataSnapshot.getValue(Integer.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                restaurantModel restaurantModel = new restaurantModel();

                //초기 portNum ;
                restaurantModel.portNum = postPortNum + 1;

                // user/userId/restaurant/restaurantName에 restaurantModel 저장
                mDatabase.getReference().child("users").child(userId).child("restaurant").child(restaurantName).setValue(restaurantModel);

            }

        });

        // 현재 사용자 위치와 식당 위치 간의 거리 계산
        Location userLocation = new Location("user");
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);

        Location restaurantLocation = new Location("restaurant");
        restaurantLocation.setLatitude(res_lat);
        restaurantLocation.setLongitude(res_long);

        float distance = userLocation.distanceTo(restaurantLocation); // 거리 계산
        Toast.makeText(getApplicationContext(), String.valueOf(distance), Toast.LENGTH_SHORT).show();
        // 거리가 300m 이내인 경우 버튼 활성화, 그렇지 않으면 비활성화
        if (distance <= 100) {
            portButton.setEnabled(true);
            //팝업창 띄워

        } else {
            portButton.setEnabled(false);
            Toast.makeText(getApplicationContext(),"port not possible",Toast.LENGTH_SHORT).show();
        }



        System.out.println("latitude: "+latitude);
        System.out.println("longtitude: "+longitude);

        restaurantContainer = findViewById(R.id.restaurantContainer);
        viewPager = new ViewPager2(this);
        viewPager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        restaurantContainer.addView(viewPager);
        setupViewPager();
    }

    private void setupViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(AppCompatActivity activity) {
            super(activity);
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new RestaurantPhotoFragment();
                case 1:
                    return new ResutaurantRecommendFragment();
                case 2:
                    return new RestaurantRankFragment();
                default:
                    return null;
            }
        }
    }

}