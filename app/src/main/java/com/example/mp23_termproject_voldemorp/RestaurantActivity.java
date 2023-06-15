package com.example.mp23_termproject_voldemorp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

public class RestaurantActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Current user's location
    static public double latitude;
    static public double longitude;

    // Restaurant location
    private double res_lat;
    private double res_long;
    private ViewPager2 viewPager;
    private FrameLayout restaurantContainer;
    private NaverMap naverMap;
    private MapView mapView;

    private Button portButton;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase mDatabase;

    private int portNum;

    private boolean likeCheck = false;

    private int postPortNum;

    private int popPortNum;

    public class RestaurantModel {
        public int portNum;
        public boolean likeCheck = false;
    }

    public class UserModel {
        public int userTotalLike;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // Make the status bar transparent and the image visible
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Initialize MapView
        mapView = findViewById(R.id.restaurantMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);

        // Get the intent from MainActivity
        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("name");
        String restaurantType = intent.getStringExtra("type");

        // Extract latitude and longitude of the restaurant
        res_lat = intent.getDoubleExtra("res_lat", 0.0);
        res_long = intent.getDoubleExtra("res_long", 0.0);

        // Extract current user's latitude and longitude
//        latitude = intent.getDoubleExtra("latitude", 0.0);
//        longitude = intent.getDoubleExtra("longitude", 0.0);

        System.out.println(res_lat);
        System.out.println(res_long);
        System.out.println(latitude);
        System.out.println(longitude);

        // Set the restaurant name and food type
        TextView resNameText = findViewById(R.id.textView2);
        TextView foodTypeText = findViewById(R.id.textView3);

        resNameText.setText(restaurantName);
        foodTypeText.setText(restaurantType);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        portButton = findViewById(R.id.portButton);

        portButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String userId = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference reference = database.getReference("users").child(userId).child("restaurant").child(restaurantName);

                // Check if the data exists
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // If the restaurant already exists in the user's database
                        if (dataSnapshot.exists()) {
                            updateRestaurant();
                        }
                        // If the restaurant doesn't exist in the user's database
                        else {
                            addRestaurant();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Error handling
                        Toast.makeText(getApplicationContext(), "Database read canceled: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Show recommendation popup
                showPopup();
//                showBadgePopup();
//                Toast.makeText(getApplicationContext(), "Port success", Toast.LENGTH_SHORT).show();
            }

            // [Server] Add a new restaurant child to the user's database
            public void addRestaurant() {
                String userId = firebaseAuth.getCurrentUser().getUid();

                RestaurantModel restaurantModel = new RestaurantModel();
                restaurantModel.portNum = 1;
                restaurantModel.likeCheck = false;

                mDatabase.getReference().child("users").child(userId).child("restaurant").child(restaurantName).setValue(restaurantModel);
            }

            // [Server] Find the restaurant in the user's database and update portNum to portNum + 1
            public void updateRestaurant() {
                String userId = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("restaurant").child(restaurantName).child("portNum");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        portNum = dataSnapshot.getValue(Integer.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                RestaurantModel restaurantModel = new RestaurantModel();
                restaurantModel.portNum = portNum + 1;

                // Save the restaurantModel to user/userId/restaurant/restaurantName
                mDatabase.getReference().child("users").child(userId).child("restaurant").child(restaurantName).setValue(restaurantModel);
            }
        });

        // Calculate the distance between the current user's location and the restaurant location
        Location userLocation = new Location("user");
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);

        Location restaurantLocation = new Location("restaurant");
        restaurantLocation.setLatitude(res_lat);
        restaurantLocation.setLongitude(res_long);

        float distance = userLocation.distanceTo(restaurantLocation); // Calculate the distance
//        Toast.makeText(getApplicationContext(), String.valueOf(distance), Toast.LENGTH_SHORT).show();
        // Enable the button if the distance is within 500m, otherwise disable it
        if (distance <= 500) {
            portButton.setEnabled(true);
            // Show popup
        } else {
            portButton.setEnabled(false);
        }

        System.out.println("latitude: " + latitude);
        System.out.println("longitude: " + longitude);

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

    // *---Recommendation Popup---*
    private AlertDialog dialog;
    private void showPopup() {

        // Get the restaurant name through intent
        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("name");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popupView = getLayoutInflater().inflate(R.layout.dialog_recommend_alert, null);

        // Set the popup layout to AlertDialog
        builder.setView(popupView);
        dialog = builder.create();
        dialog.show();

        TextView visitTextView = popupView.findViewById(R.id.numOfVisit);
        TextView visitMessage = popupView.findViewById(R.id.visitMessage);
        TextView doYouRecommend = popupView.findViewById(R.id.doyourecommend);
        ImageView badgeImage = popupView.findViewById(R.id.badgeImage);
        Button notRecommendBtn = popupView.findViewById(R.id.notRecommendBtn);
        Button recommendBtn = popupView.findViewById(R.id.recommendBtn);
        Button closeBtn = popupView.findViewById(R.id.closeBtn);

        // Get the userId of the user and retrieve portNum from the database
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference portNumRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("restaurant").child(restaurantName).child("portNum");
        portNumRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    portNum = dataSnapshot.getValue(Integer.class);
                    // Display the 'numOfVisit' for the current visit
                    String strPortNum = String.valueOf(portNum);
                    visitTextView.setText(strPortNum);

                    if (portNum == 1) {
                        recommendBtn.setVisibility(View.GONE); // Hide the button
                        notRecommendBtn.setVisibility(View.GONE); // Hide the button
                        doYouRecommend.setVisibility(View.GONE); // Hide the text view
                    } else {
                        recommendBtn.setVisibility(View.VISIBLE); // Show the button
                        notRecommendBtn.setVisibility(View.VISIBLE); // Show the button
                        doYouRecommend.setVisibility(View.VISIBLE); // Show the text view

                        recommendBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle the recommend button click -> Change likeCheck to true
                                RestaurantModel restaurantModel = new RestaurantModel();
                                restaurantModel.likeCheck = true;
                                restaurantModel.portNum = portNum;

                                // Save the restaurantModel to user/userId/restaurant/restaurantName
                                mDatabase.getReference().child("users").child(userId)
                                        .child("restaurant").child(restaurantName).setValue(restaurantModel);

                                DatabaseReference totalLikeRef = FirebaseDatabase.getInstance().getReference("users")
                                        .child(userId).child("userTotalLike");
                                totalLikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            int userTotalLike = dataSnapshot.getValue(Integer.class);
                                            userTotalLike++; // Increment userTotalLike by 1

                                            // Save the updated userTotalLike value to Firebase
                                            totalLikeRef.setValue(userTotalLike);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle cancellation
                                    }
                                });

                                dialog.dismiss();
                            }
                        });

                        notRecommendBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle the not recommend button click -> Change likeCheck to false
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                                        .child(userId).child("restaurant").child(restaurantName).child("likeCheck");

                                RestaurantModel restaurantModel = new RestaurantModel();
                                restaurantModel.likeCheck = false;
                                restaurantModel.portNum = portNum;

                                // Save the restaurantModel to user/userId/restaurant/restaurantName
                                mDatabase.getReference().child("users").child(userId)
                                        .child("restaurant").child(restaurantName).setValue(restaurantModel);

                                dialog.dismiss();
                            }
                        });
                    }
                } else {
                    // Handle the case when the restaurant doesn't exist in the user's database
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                visitTextView.setText("0");
            }
        });

        // Close button 'X'
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

//    // *---Badge Popup---*
//    private AlertDialog dialog2;
//    private void showBadgePopup() {
//        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
//        View popupView = getLayoutInflater().inflate(R.layout.dialog_badge_alert, null);
//
//        TextView nameOfNewBadge = popupView.findViewById(R.id.nameOfNewBadge);
//        ImageView badgeImage = popupView.findViewById(R.id.badgeImage);
//        Button closeButton = popupView.findViewById(R.id.closeButton);
//
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
//        DatabaseReference userTotalLikeRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("userTotalLike");
//        DatabaseReference maxPortNumRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("max_portNum");
//        DatabaseReference badgeRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("badge");
//
//        // [Server] Listen for changes in the recommendation count
//        userTotalLikeRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int newRecommendationCount = dataSnapshot.getValue(int.class);
//
//                // Badge 4: When the recommendation count changes to 1
//                if (newRecommendationCount == 1) {
//                    // [Server] Update Badge 4 to true
//                    nameOfNewBadge.setText("Timid Foodie");
//                    Drawable newDrawable = getResources().getDrawable(R.drawable.badge_recommend1); // Get the drawable
//                    badgeImage.setImageDrawable(newDrawable);
//
//                    // Set the popup layout to AlertDialog
//                    builder2.setView(popupView);
//                    dialog2 = builder2.create();
//                    dialog2.show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });
//    }

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
}
