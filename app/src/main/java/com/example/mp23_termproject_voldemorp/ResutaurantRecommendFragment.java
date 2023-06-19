package com.example.mp23_termproject_voldemorp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ResutaurantRecommendFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    static public String restaurantName;
    private int portNum;
    public ResutaurantRecommendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_recommend, container, false);

        TextView titleMarketName = view.findViewById(R.id.marketName1);
        TextView descriptionMarketName = view.findViewById(R.id.marketName2);
        TextView peoplePortNum = view.findViewById(R.id.textView241);
        TextView myPortNum = view.findViewById(R.id.textView24);

        // 랜덤한 값을 생성하여 peoplePortNum에 할당
        Random random = new Random();
        int randomValue = random.nextInt(100); // 0부터 99까지의 랜덤한 정수
        peoplePortNum.setText(String.valueOf(randomValue));

        //[서버] 데이터에서 나의 방문 횟수 불러오기

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //사용자의 userId 받아와서 데베에서 portNum 받아오기
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference reference = database.getReference("users").child(userId).child("restaurant").child(restaurantName);

        titleMarketName.setText(restaurantName);
        descriptionMarketName.setText(restaurantName);

        // 데이터 존재 여부 확인
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 식당이 이미 유저DB에 있는 경우
                if (dataSnapshot.exists()) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("restaurant").child(restaurantName).child("portNum");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            portNum = dataSnapshot.getValue(Integer.class);
                            String strportnum = String.valueOf(portNum);
                            myPortNum.setText(strportnum);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            myPortNum.setText(0);

                        }
                    });

                }
                // 식당이 유저DB에 없는 경우
                else {

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 에러 처리
            }
        });



        return view;
    }
}