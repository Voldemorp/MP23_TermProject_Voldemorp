package com.example.mp23_termproject_voldemorp;


import static android.provider.Contacts.SettingsColumns.KEY;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.map.MapFragment;

import java.util.List;

import okhttp3.internal.http2.ErrorCode;

public class MainActivity extends AppCompatActivity {

    private Button btnScreen1, btnScreen2, btnScreen3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MapView를 찾아서 MapFragment를 추가
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mapView, mapFragment)
                    .commit();
        }
//        GeocodingApi.getKey(KEY);
//
//        // 주소 변환 요청
//        GeocodingApiRequest request = new GeocodingApiRequest(context);
//        request.address("주소")
//                .execute(new OnGeocodingResponseListener() {
//                    @Override
//                    public void onSuccess(List<Address> results) {
//                        // 주소 변환 성공 시 처리 로직
//                        // 결과 처리
//                    }
//
//                    @Override
//                    public void onError(ErrorCode errorCode) {
//                        // 주소 변환 실패 시 처리 로직
//                        // 에러 처리
//                    }
//                });

//        setContentView(R.layout.activity_toolbar);
//        btnScreen1 = findViewById(R.id.btnScreen1);
//        btnScreen2 = findViewById(R.id.btnScreen2);
//        btnScreen3 = findViewById(R.id.btnScreen3);
//        btnScreen1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showScreen1();
//            }
//        });
//
//        btnScreen2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showScreen2();
//            }
//        });
//
//        btnScreen3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showScreen3();
//            }
//        });
    }
    private void showScreen1() {
        Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
        startActivity(intent);
    }

    private void showScreen2() {
        // Screen 2를 띄우는 로직 작성
    }

    private void showScreen3() {
        // Screen 3을 띄우는 로직 작성

    }
}