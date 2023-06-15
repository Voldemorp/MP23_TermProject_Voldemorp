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

import java.util.ArrayList;
import java.util.List;

public class MyPageActivity extends AppCompatActivity {
    private TextView nicknameTextView;
    private TextView nicknameTextViewBadge;
    private TextView nicknameTextViewPort;
    static public TextView addressTextView;
    static public String address;
    private ImageView profileImageView;
    private TextView mainBadgeTextView;
    private String userId;
    private DatabaseReference userPortRef;
    private List<MyPagePortItem> portList = new ArrayList<>();

    private ValueEventListener portValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Logic to be executed when the data is changed
            // For example, retrieve necessary data from the data snapshot and process it
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Logic to be executed when the operation is cancelled
            // For example, perform error handling
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // Make status bar transparent and display image
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Retrieve user information from the server
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        // *-- Display Profile Badge Image --*
        profileImageView = findViewById(R.id.profileImageView);
        loadSelectedImageFromSharedPreferences();   // Retrieve and set the selected badge image from SharedPreferences

        // *-- Display Profile Badge Text --*
        mainBadgeTextView = findViewById(R.id.mainBadgeTextView);
        userRef.child(userId).child("badge").child("mainBadge").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()) {
                    // Set the retrieved main badge to the text view
                    String mainBadge = dataSnapshot.getValue(String.class);
                    mainBadgeTextView.setText(mainBadge);
                }
                else { System.out.println("none"); }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // *-- Display Nickname --*
        nicknameTextView = findViewById(R.id.nicknameTextview);
        nicknameTextViewBadge = findViewById(R.id.nicknameTextviewBadge);
        nicknameTextViewPort = findViewById(R.id.nicknameTextviewPort);
        userRef.child(userId).child("nickName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()) {
                    // Set the retrieved nickname to the text view
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

        // *-- Display Address --*
        addressTextView = findViewById(R.id.addressTextview);
        userRef.child(userId).child("address1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()) {
                    // Set the retrieved address to the text view
                    address=dataSnapshot.getValue(String.class);

                    // Add the following code inside the onCreate method
                    Intent intent = getIntent();
                    String address2 = intent.getStringExtra("address");
                    if (address2 != null) {
                        addressTextView.setText(address2);  // Set the address value to the text view
                    }
                    else
                        addressTextView.setText(address);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // *-- Display Badge List --*
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

        // Retrieve the badge values for the current user from Firebase and set the appropriate visibility
//        DatabaseReference badgeRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("badge").child("badgeForMania");
//        badgeRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    boolean isBadgeVisible = dataSnapshot.getValue(Boolean.class);
//
//                    if (isBadgeVisible) {
//                        // Show the badge if it should be visible
//                        badgeForMania.setVisibility(View.VISIBLE);
//                    } else {
//                        // Hide the badge if it should be hidden
//                        badgeForMania.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle the error
//            }
//        });

        // *-- Display Port List --*
        // [Server] Retrieve the names of restaurants that the user has ported and the number of times they have ported,
        // and append the information to the portList
        DatabaseReference userPortRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("restaurant");
        userPortRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot document : dataSnapshot.getChildren()) {
                        String restaurantName = document.getKey(); // Retrieve the restaurant name

                        // Retrieve the restaurant name and portNum, and add them to the portList
                        DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("restaurant").child(restaurantName);
                        restaurantRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    int portNum = dataSnapshot.child("portNum").getValue(Integer.class); // Retrieve the portNum of the restaurant

                                    // Add the restaurant information to the portList
                                    MyPagePortItem portItem = new MyPagePortItem(restaurantName, portNum);
                                    portList.add(portItem);

                                    // Add the added restaurant information to the layout
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

        // 'Edit Profile' button click event to switch screens
        Button editProfileBtn = findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // 'Go Back to Main' button click event to switch screens
        Button backToMainBtn = findViewById(R.id.backToMainBtn);
        backToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Make status bar transparent and display image
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    // Load and set the image from SharedPreferences
    private void loadSelectedImageFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String selectedImageString = preferences.getString("selectedImage", null);
        if (selectedImageString != null) {
            // Convert the image string stored as a string to a Bitmap and set it as a Drawable
            Bitmap bitmap = stringToBitmap(selectedImageString);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            profileImageView.setImageDrawable(drawable);
        }
    }

    // Convert a string to a Bitmap
    private Bitmap stringToBitmap(String imageString) {
        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    private void addPortListToLayout() {
        LinearLayout layout = findViewById(R.id.portListContainer);

        for (int i = 0; i < portList.size(); i++) {
            // Layout to be added
            MyPageListLayout myPageListLayout = new MyPageListLayout(getApplicationContext(), null, portList.get(i));

            // Add the layout
            layout.addView(myPageListLayout);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the event listener
        if (userPortRef != null) {
            userPortRef.removeEventListener(portValueEventListener);
        }
    }
}
