package com.example.mp23_termproject_voldemorp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.naver.maps.map.overlay.Overlay;
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
    private static double latitude;
    private static double longitude;
    private boolean isCircleOverlayAdded = false;
    private List<MainRestaurantInfo> restaurantInfoList = new ArrayList<>();
    private List<MainRestaurantInfo> nearbyRestaurants = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Initialize MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            initMap();
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Button to move to current location
        btnMoveToMyLocation = findViewById(R.id.btnMoveToMyLocation);
        btnMoveToMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    moveMapToCurrentLocation();
            }
        });

        FloatingActionButton mypageBtn = findViewById(R.id.mypageBtn);
        mypageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton rankingBtn = findViewById(R.id.rankingBtn);
        rankingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                initMap();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initMap() {
        // Initialize FusedLocationSource
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // Set location source for MapView
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull NaverMap naverMap) {
                MainActivity.this.naverMap = naverMap;
                naverMap.setLocationSource(locationSource);
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

                loadRestaurantData();
            }
        });
    }

    private void loadRestaurantData() {
        firestore.collection("bokjeong_restaurant")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                // Retrieve restaurant data
                                Object xObject = document.get("좌표정보(x)");
                                if (xObject instanceof Number) {
                                    double x = ((Number) xObject).doubleValue();
                                    double y = document.getDouble("좌표정보(y)");
                                    String name = document.getString("사업장명");
                                    String foodType = document.getString("위생업태명");
                                    MainRestaurantInfo restaurantInfo = new MainRestaurantInfo(x, y, name, foodType);
                                    restaurantInfoList.add(restaurantInfo);
                                } else {
                                    // Handle the case when the value is not a number
                                    Log.e("RestaurantData", "'좌표정보(x)' field is not a number");
                                }
                            }
//                            addMarkers();
//                            moveMapToCurrentLocation(); // Move map after loading restaurant data
                        } else {
                            Toast.makeText(MainActivity.this, "No restaurant data available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to load restaurant data.", Toast.LENGTH_SHORT).show();
                    }
                });
        firestore.collection("taepyeong_restaurant")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                // Retrieve restaurant data
                                Object xObject = document.get("좌표정보(x)");
                                if (xObject instanceof Number) {
                                    double x = ((Number) xObject).doubleValue();
                                    double y = document.getDouble("좌표정보(y)");
                                    String name = document.getString("사업장명");
                                    String foodType = document.getString("위생업태명");
                                    MainRestaurantInfo restaurantInfo = new MainRestaurantInfo(x, y, name, foodType);
                                    restaurantInfoList.add(restaurantInfo);
                                } else {
                                    // Handle the case when the value is not a number
                                    Log.e("RestaurantData", "'좌표정보(x)' field is not a number");
                                }
                            }
                            addMarkers();
                            moveMapToCurrentLocation(); // Move map after loading restaurant data
                        } else {
                            Toast.makeText(MainActivity.this, "No restaurant data available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to load restaurant data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void addMarkers() {
//                LinearLayout layout = findViewById(R.id.restaurantLinearView);

        for (MainRestaurantInfo restaurant : nearbyRestaurants) {
            LatLng latLng = new LatLng(restaurant.x, restaurant.y);
            Marker marker = new Marker();
            marker.setPosition(latLng);
            marker.setCaptionText(restaurant.name);
            marker.setCaptionColor(getResources().getColor(R.color.black));
            marker.setCaptionHaloColor(getResources().getColor(R.color.white));
            marker.setMap(naverMap);

            // Add OnClickListener to each marker
            marker.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    // Launch RestaurantActivity with selected restaurant's name and type
                    Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                    intent.putExtra("name", restaurant.name);
                    intent.putExtra("type", restaurant.foodType);
                    intent.putExtra("res_lat",restaurant.x);
                    intent.putExtra("res_long",restaurant.y);
//                    intent.putExtra("latitude",latitude);
//                    intent.putExtra("longitude",longitude);
                    RestaurantActivity.latitude=latitude;
                    RestaurantActivity.longitude=longitude;
                    ResutaurantRecommendFragment.restaurantName=restaurant.name;
                    startActivity(intent);
                    return true;
                }
            });
        }
    }

    private void moveMapToCurrentLocation() {
        if (naverMap != null && locationSource != null) {
            LocationOverlay locationOverlay = naverMap.getLocationOverlay();
            Location lastLocation = locationSource.getLastLocation();
            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng)
                        .animate(CameraAnimation.Easing, 100)
                        .finishCallback(new CameraUpdate.FinishCallback() {
                            @Override
                            public void onCameraUpdateFinish() {
                                if (!isCircleOverlayAdded) {
                                    // Add circle overlay only if it hasn't been added yet
                                    CircleOverlay circleOverlay = new CircleOverlay();
                                    circleOverlay.setCenter(latLng);
                                    circleOverlay.setRadius(1000); // 반경 1km
                                    circleOverlay.setColor(Color.argb(70, 0, 0, 255)); // 파란색 반투명
                                    circleOverlay.setMap(naverMap);
                                    isCircleOverlayAdded = true;
                                }
                                findRestaurantsWithinRadius(latLng);
                                addMarkers();
                            }
                        });
                naverMap.moveCamera(cameraUpdate);

                // Delay the execution of adding the markers and showing the restaurant list
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Add markers and show restaurant list after a delay
                        findRestaurantsWithinRadius(latLng);
                        addMarkers();
                    }
                }, 3000); // Adjust the delay time as needed
            } else {
//                Toast.makeText(this, "Failed to get current location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void findRestaurantsWithinRadius(LatLng center) {
        LinearLayout layout = findViewById(R.id.restaurantLinearView);
        for (MainRestaurantInfo restaurant : restaurantInfoList) {
            LatLng restaurantLocation = new LatLng(restaurant.x, restaurant.y);
            double distance = center.distanceTo(restaurantLocation);
            if (distance <= 1000) { // 1km 이내의 식당만 추출
                nearbyRestaurants.add(restaurant);
            }
        }

        // Print nearby restaurants to console
        for (MainRestaurantInfo restaurant : nearbyRestaurants) {
            System.out.println("Name: " + restaurant.name);
            System.out.println("Food Type: " + restaurant.foodType);
            System.out.println();

            // Create an instance of MainRestaurantListLayout to display restaurant information
            MainRestaurantListLayout restaurantLayout = new MainRestaurantListLayout(this, restaurant);

            // Add the restaurantLayout to the linear layout
            layout.addView(restaurantLayout);
        }

//                Toast.makeText(getApplicationContext(),"Current Location: " + center.latitude + ", " + center.longitude,Toast.LENGTH_SHORT).show();

        // 값을 받는 액티비티로 데이터 전달
                RestaurantActivity.latitude = latitude;
                RestaurantActivity.longitude = longitude;
                MainRestaurantListLayout.latitude=latitude;
                MainRestaurantListLayout.longitude=longitude;

    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        // Do nothing
        MainActivity.this.naverMap = naverMap;
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
    public void onSaveInstanceState(Bundle outState) {
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
}