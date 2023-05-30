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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private Button btnMoveToMyLocation;
    private MapView mapView;
    private FirebaseFirestore firestore;
    private double latitude; // 내 위치의 위도
    private double longitude; // 내 위치의 경도
    private List<RestaurantInfo> restaurantInfoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MapView 초기화
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // onCreate 메서드에서 firestore 초기화
        firestore = FirebaseFirestore.getInstance();

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

//        // 마이페이지에 있는 port list에 띄울 정보를 저장하는 배열
//        restaurantInfo = new ArrayList<>();
//
//        //[서버] DB에서 사용자가 port한 식당 이름/몇번 port했는지 가져와서 portList에 append해야 함
//        // 테스트를 위한 더미데이터 (나중에 구현 성공하면 지우시면 됩니당)
//        MainRestaurantInfo dummyInfo1 = new MainRestaurantInfo("화리화리", "한식");
//        MainRestaurantInfo dummyInfo2 = new MainRestaurantInfo("태평돈까스", "경양식");
//        MainRestaurantInfo dummyInfo3 = new MainRestaurantInfo("폼프리츠", "호프/통닭");
//        MainRestaurantInfo dummyInfo4 = new MainRestaurantInfo("호식당", "일식");
//        MainRestaurantInfo dummyInfo5 = new MainRestaurantInfo("쩡이네", "호프/통닭");
//        MainRestaurantInfo dummyInfo6 = new MainRestaurantInfo("라쿵푸마라탕", "중국식");
//        restaurantInfo.add(dummyInfo1);
//        restaurantInfo.add(dummyInfo2);
//        restaurantInfo.add(dummyInfo3);
//        restaurantInfo.add(dummyInfo4);
//        restaurantInfo.add(dummyInfo5);
//        restaurantInfo.add(dummyInfo6);
//
//        // 상태 바 투명하게 하고 사진 보이게 하는 코드
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//
//        // for문 돌면서 추가
//        for (int i = 0; i < restaurantInfo.size(); i++) {
//            // 추가할 레이아웃
//            MainRestaurantListLayout mainRestaurantListLayout = new MainRestaurantListLayout(getApplicationContext(), restaurantInfo.get(i));
//            // 추가할 위치
//            LinearLayout layout = findViewById(R.id.restaurantLinearView);
//            // 추가 코드
//            layout.addView(mainRestaurantListLayout);
//        }
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

    // 식당 정보를 담을 클래스
    private static class RestaurantInfo {
        double x;
        double y;
        String name;
        String foodType;

        public RestaurantInfo(double x, double y, String name, String foodType) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.foodType = foodType;
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

        // 2km 반경의 원 표시
        showCircleOverlay();

        // Firestore에서 좌표 정보 가져오기
        firestore.collection("taepyeong_restaurant")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot) {
                            if (documentSnapshot.exists()) {
                                String xString = documentSnapshot.getString("좌표정보(x)");
                                String yString = documentSnapshot.getString("좌표정보(y)");
                                String restaurantName = documentSnapshot.getString("사업장명");
                                String foodType = documentSnapshot.getString("위생업태명");
                                double restaurantX = Double.parseDouble(xString); // 식당 x좌표
                                double restaurantY = Double.parseDouble(yString); // 식당 y좌표

                                RestaurantInfo restaurantInfo = new RestaurantInfo(restaurantX, restaurantY, restaurantName, foodType);
                                restaurantInfoList.add(restaurantInfo);

                            }
                        }
                        addMarkersWithin2km();
                        Toast.makeText(MainActivity.this, "식당 정보 로드 완료", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,  "식당 정보 로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 거리 계산
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double R = 6371; // 지구의 반지름 (단위: km)

        double lat1Rad = Math.toRadians(x1);
        double lon1Rad = Math.toRadians(y1);
        double lat2Rad = Math.toRadians(x2);
        double lon2Rad = Math.toRadians(y2);

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    // 2km 이내에 있는 식당들을 마커로 표시
    private void addMarkersWithin2km() {
        for (RestaurantInfo restaurantInfo : restaurantInfoList) {
            double distance = calculateDistance(restaurantInfo.x, restaurantInfo.y, latitude, longitude);
            boolean isWithin2Km = distance <= 2.0; // 거리가 2km 이내인지 확인

            if (isWithin2Km) {
                Toast.makeText(MainActivity.this, "x =" + restaurantInfo.x + "\n y = " + restaurantInfo.y + "\n 사업장명 : " +
                        restaurantInfo.name + "\n 위생업태명 : " + restaurantInfo.foodType, Toast.LENGTH_SHORT).show();

//                for (int i = 0; i < restaurantInfo.size(); i++) {
//                    // 추가할 레이아웃
//                    RestaurantListLayout RestaurantListLayout = new RestaurantListLayout(getApplicationContext(), restaurantInfo.get(i));
//                    // 추가할 위치
//                    LinearLayout layout = findViewById(R.id.restaurantLinearView);
//                    // 추가 코드
//                    layout.addView(RestaurantListLayout);
//                }

                // 2km 이내에 있는 식당들에 대해 마커 추가
                Marker marker = new Marker();
                marker.setPosition(new LatLng(restaurantInfo.x, restaurantInfo.y));
                marker.setMap(naverMap);
            }
        }}

    // 내 위치에서 2km 반경의 원 표시
    private void showCircleOverlay () {
        CircleOverlay circleOverlay = new CircleOverlay();
        circleOverlay.setCenter(new LatLng(latitude, longitude));
        circleOverlay.setRadius(2000);// 반지름은 2km에 해당하는 값입니다. (단위: 미터)
        circleOverlay.setColor(0x304169E1);// 채우기 색상
        circleOverlay.setOutlineColor(0xFF4169E1);// 외곽선 색상
        circleOverlay.setOutlineWidth(2);// 외곽선 두께
        circleOverlay.setMap(naverMap);
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