package com.example.mp23_termproject_voldemorp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Random;

public class ResutaurantRecommendFragment extends Fragment {
    public ResutaurantRecommendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_recommend, container, false);

        TextView peoplePortNum = view.findViewById(R.id.textView241);
        TextView myPortNum = view.findViewById(R.id.textView24);

        // 랜덤한 값을 생성하여 peoplePortNum에 할당
        Random random = new Random();
        int randomValue = random.nextInt(100); // 0부터 99까지의 랜덤한 정수
        peoplePortNum.setText(String.valueOf(randomValue));

        //[서버] 데이터에서 나의 방문 횟수 불러오기
        myPortNum.setText("");



        return view;
    }
}