package com.example.mp23_termproject_voldemorp;

import android.annotation.SuppressLint;
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
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

public class RestaurantActivity extends AppCompatActivity implements OnMapReadyCallback {

    //현재 사용자 위치
    static double latitude;
    static double longitude;

    //식당 위치
    private double res_lat;
    private double res_long;
    private ViewPager2 viewPager;
    private FrameLayout restaurantContainer;
    private NaverMap naverMap;
    private MapView mapView;

    private Button portButton;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase mDatabase;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Initialize MapView
        mapView = findViewById(R.id.restaurantMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);

        // MainActivity에서 전달된 인텐트 가져오기
        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("name");
        String restaurantType = intent.getStringExtra("type");
        res_lat = intent.getDoubleExtra("res_lat",0.0);
        res_long = intent.getDoubleExtra("res_long",0.0);


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
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        // Add a marker for the restaurant location
        LatLng restaurantLatLng = new LatLng(res_lat, res_long);
        Marker marker = new Marker();
        marker.setPosition(restaurantLatLng);
        marker.setMap(naverMap);

        // Move the camera to the restaurant location with an offset
        double offset = -0.008; // Adjust this value to change the marker offset
        LatLng cameraLatLng = new LatLng(res_lat + offset, res_long);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(cameraLatLng)
                .animate(CameraAnimation.Easing);
        naverMap.moveCamera(cameraUpdate);
    }







    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void setupViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }


    // 포트 버튼 눌렀을 때 유저 데베 업데이트
}