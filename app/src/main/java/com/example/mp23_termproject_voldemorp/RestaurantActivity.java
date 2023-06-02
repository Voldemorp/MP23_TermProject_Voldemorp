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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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


        portButton=findViewById(R.id.portButton);

        portButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //팝업창 띄우는걸로 바꾸기
                Toast.makeText(getApplicationContext(),"port success",Toast.LENGTH_SHORT).show();


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
            return 2;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new ResutaurantRecommendFragment();
                case 1:
                    return new RestaurantRankFragment();
                default:
                    return null;
            }
        }
    }

    // 포트 버튼 눌렀을 때 유저 데베 업데이트

}