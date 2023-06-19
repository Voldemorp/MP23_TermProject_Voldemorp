package com.example.mp23_termproject_voldemorp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;



public class RestaurantRankFragment extends Fragment {

    ArrayList<RestaurantRankUserInfo> usersInfo;

    public RestaurantRankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restautrant_rank, container, false);


        // 랭크 띄울 정보를 저장하는 배열
        usersInfo = new ArrayList<>();

        //[서버] 식당별로 사용자 방문 데이터를 모두 찾아서 usersInfo 배열에 저장해야 함
        //[기능] 무작위로 정렬되어있을 usersInfo 배열을 방문수(numOfVisit)대로 정렬

        // 테스트를 위한 더미데이터 (나중에 구현 성공하면 지우시면 됩니당)
        RestaurantRankUserInfo dummyInfo1 = new RestaurantRankUserInfo("유짐이와농담곰", "53");
        RestaurantRankUserInfo dummyInfo2 = new RestaurantRankUserInfo("포켓몬마스터지우", "25");
        RestaurantRankUserInfo dummyInfo3 = new RestaurantRankUserInfo("수미칩은맛이있을까", "15");
        RestaurantRankUserInfo dummyInfo4 = new RestaurantRankUserInfo("지연은서결혼합니다", "5");
        RestaurantRankUserInfo dummyInfo5 = new RestaurantRankUserInfo("교수님살려주세요", "2");
        usersInfo.add(dummyInfo1);
        usersInfo.add(dummyInfo2);
        usersInfo.add(dummyInfo3);
        usersInfo.add(dummyInfo4);
        usersInfo.add(dummyInfo5);

        TextView rankingText1= rootView.findViewById(R.id.textView19);
        TextView rankingText2= rootView.findViewById(R.id.textView21);
        ScrollView scrollView = rootView.findViewById(R.id.rankScrollView);
        LinearLayout linearLayout = rootView.findViewById(R.id.rankLinearView);


        //실제 식당이름으로 바꾸기
        rankingText1.setText(ResutaurantRecommendFragment.restaurantName+"의 랭킹");
        rankingText2.setText(ResutaurantRecommendFragment.restaurantName+"의 사용자 랭킹이에요");

        // 식당 랭킹에 있는 사용자 수(배열 요소 수)만큼 컴포넌트를 스크롤 뷰에 추가
        for (int i = 0; i < usersInfo.size(); i++) {

            // 추가할 레이아웃
            RestaurantRankListLayout restaurantRankListLayout = new RestaurantRankListLayout(getContext(), usersInfo.get(i));
            // 추가 코드
            linearLayout.addView(restaurantRankListLayout);
            TextView rankNum = restaurantRankListLayout.findViewById(R.id.rank);
            //순위 설정
            rankNum.setText(String.valueOf(i + 1)+"위");
        }

        return rootView;
    }
}