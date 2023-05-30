package com.example.mp23_termproject_voldemorp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private Button btnMoveToMyLocation;
    private MapView mapView;
    ArrayList<MainRestaurantInfo> restaurantInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MapView 초기화
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // 위치 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // 권한이 이미 허용되어 있음
            initMap();
        } else {
            // 권한을 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        // 내 위치로 이동하는 버튼 설정
        btnMoveToMyLocation = findViewById(R.id.btnMoveToMyLocation);
        btnMoveToMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveMapToCurrentLocation();
            }
        });

        // 마이페이지에 있는 port list에 띄울 정보를 저장하는 배열
        restaurantInfo = new ArrayList<>();

        //[서버] DB에서 사용자가 port한 식당 이름/몇번 port했는지 가져와서 portList에 append해야 함
        // 테스트를 위한 더미데이터 (나중에 구현 성공하면 지우시면 됩니당)
        MainRestaurantInfo dummyInfo1 = new MainRestaurantInfo("화리화리", "한식");
        MainRestaurantInfo dummyInfo2 = new MainRestaurantInfo("태평돈까스", "경양식");
        MainRestaurantInfo dummyInfo3 = new MainRestaurantInfo("폼프리츠", "호프/통닭");
        MainRestaurantInfo dummyInfo4 = new MainRestaurantInfo("호식당", "일식");
        MainRestaurantInfo dummyInfo5 = new MainRestaurantInfo("쩡이네", "호프/통닭");
        MainRestaurantInfo dummyInfo6 = new MainRestaurantInfo("라쿵푸마라탕", "중국식");
        restaurantInfo.add(dummyInfo1);
        restaurantInfo.add(dummyInfo2);
        restaurantInfo.add(dummyInfo3);
        restaurantInfo.add(dummyInfo4);
        restaurantInfo.add(dummyInfo5);
        restaurantInfo.add(dummyInfo6);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // for문 돌면서 추가
        for (int i = 0; i < restaurantInfo.size(); i++) {
            // 추가할 레이아웃
            MainRestaurantListLayout mainRestaurantListLayout = new MainRestaurantListLayout(getApplicationContext(), restaurantInfo.get(i));
            // 추가할 위치
            LinearLayout layout = findViewById(R.id.restaurantLinearView);
            // 추가 코드
            layout.addView(mainRestaurantListLayout);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 위치 권한이 허용되었을 때
                initMap();
            } else {
                // 위치 권한이 거부되었을 때
                Toast.makeText(this, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                // 위치 추적 모드를 설정하지 않음
            }
        }
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // Display the direction arrow for the current location
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
    }

    private void initMap() {
        // 위치 권한이 허용되어 있는 경우에만 지도 초기화
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void moveCameraToLocation(double latitude, double longitude) {
        LatLng target = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(target)
                .animate(CameraAnimation.Easing);
        naverMap.moveCamera(cameraUpdate);
    }

    private void moveMapToCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                moveCameraToLocation(latitude, longitude);
                locationManager.removeUpdates(this);

                // Update the location overlay with the new location
                LocationOverlay locationOverlay = naverMap.getLocationOverlay();
                locationOverlay.setPosition(new LatLng(latitude, longitude));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        // 위치 권한이 허용되어 있는지 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);

            // 위치 추적 모드를 설정
            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

            // LocationOverlay의 가시성을 활성화
            LocationOverlay locationOverlay = naverMap.getLocationOverlay();
            locationOverlay.setVisible(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}