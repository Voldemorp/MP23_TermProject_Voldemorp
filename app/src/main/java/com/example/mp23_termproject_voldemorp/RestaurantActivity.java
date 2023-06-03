package com.example.mp23_termproject_voldemorp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2
        ;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.*;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

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

        private int portNum = 0;

        private boolean likeCheck = false;

        private int postPortNum;

        public class restaurantModel{

            public int portNum = 0;

            public boolean likeCheck = false;
        }


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

            // 식당의 위도(latitude)와 경도(longitude) 값 추출
            res_lat = intent.getDoubleExtra("res_lat",0.0);
            res_long = intent.getDoubleExtra("res_long",0.0);

            // 현재 내 위도(latitude)와 경도(longitude) 값 추출
            latitude = intent.getDoubleExtra("latitude", 0.0);
            longitude = intent.getDoubleExtra("longitude", 0.0);

            System.out.println(res_lat);
            System.out.println(res_long);
            System.out.println(latitude);
            System.out.println(longitude);

            //식당 이름
            TextView resNameText=(TextView)findViewById(R.id.textView2);
            //음식 타입
            TextView foodTypeText=(TextView)findViewById(R.id.textView3);

            resNameText.setText(restaurantName);
            foodTypeText.setText(restaurantType);

            firebaseAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance();
            portButton = findViewById(R.id.portButton);

            portButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // [서버] 포트버튼 클릭시 방문수 +1 해서 DB저장

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

                    // 추천 팝업창 띄우기
                    showPopup();
//                    Toast.makeText(getApplicationContext(), "port success", Toast.LENGTH_SHORT).show();
                }

                // 유저 DB에 새로운 레스토랑 child 추가
                public void addRestaurant(){
                    String userId = firebaseAuth.getCurrentUser().getUid();

                    restaurantModel restaurantModel = new restaurantModel();

                    restaurantModel.portNum = 1;
                    restaurantModel.likeCheck = false;

                    mDatabase.getReference().child("users").child(userId).child("restaurant").child(restaurantName).setValue(restaurantModel);
                }

                //유저DB에서 해당 레스토랑 찾아서 portNum +1로 업데이트
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
//            Toast.makeText(getApplicationContext(), String.valueOf(distance), Toast.LENGTH_SHORT).show();
            // 거리가 300m 이내인 경우 버튼 활성화, 그렇지 않으면 비활성화
            if (distance <= 500) {
                portButton.setEnabled(true);
                //팝업창 띄워
            } else {
                portButton.setEnabled(false);
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

    //   *---추천 팝업창 ---*
    private AlertDialog dialog;
    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popupView = getLayoutInflater().inflate(R.layout.dialog_recommend_alert, null);

        // 팝업 레이아웃을 AlertDialog에 설정
        builder.setView(popupView);
        dialog = builder.create();
        dialog.show();

        TextView visitTextView = popupView.findViewById(R.id.numOfVisit);
        TextView visitMessage = popupView.findViewById(R.id.visitMessage);
        TextView doyourecommend = popupView.findViewById(R.id.doyourecommend);
        ImageView badgeImage = popupView.findViewById(R.id.badgeImage);
        Button notRecommendBtn = popupView.findViewById(R.id.notRecommendBtn);
        Button recommendBtn = popupView.findViewById(R.id.recommendBtn);
        Button closeBtn = popupView.findViewById(R.id.closeBtn);

        // [서버] 유저의 방문수 불러와서 visitNum에 저장, 임의로 넣은 int visitnum은 빼주세용
        // "몇" 번째 방문의 '방문수' 표시
        int visitNum = 2;
        String strvisitNum = String.valueOf(visitNum);
        visitTextView.setText(strvisitNum);

        // 첫 방문시 추천여부 버튼이 보이지 않음
        if (visitNum == 1) {
            recommendBtn.setVisibility(View.GONE); // 버튼을 숨김 처리
            notRecommendBtn.setVisibility(View.GONE); // 버튼을 숨김 처리
            doyourecommend.setVisibility(View.GONE); // 텍스트뷰를 숨김 처리
        }

        // 두번째 방문부터
        if (visitNum >= 2) {
            recommendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // [서버] 추천함 누를 시 추천 T로 변경
                    dialog.dismiss();
                }
            });
            notRecommendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // [서버] 추천안함 누를 시 추천 F로 변경
                    dialog.dismiss();
                }
            });
        }

        // 닫힘 버튼 'X'
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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