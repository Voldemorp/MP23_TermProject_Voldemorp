package com.example.mp23_termproject_voldemorp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_restautrant_rank, container, false);

        // Initialize the ArrayList to store user information for ranking
        usersInfo = new ArrayList<>();

        // [Server] Retrieve and store user visit data for each restaurant in the usersInfo ArrayList
        // [Functionality] Sort the usersInfo array in ascending order based on the number of visits (numOfVisit)

        // Dummy data for testing purposes (Please remove it after successful implementation)
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

        ScrollView scrollView = rootView.findViewById(R.id.rankScrollView);
        LinearLayout linearLayout = rootView.findViewById(R.id.rankLinearView);

        // Add the components to the scroll view for each user in the restaurant ranking
        for (int i = 0; i < usersInfo.size(); i++) {
            // Create a custom layout for each ranking item
            RestaurantRankListLayout restaurantRankListLayout = new RestaurantRankListLayout(getContext(), usersInfo.get(i));
            // Add the layout to the linear layout
            linearLayout.addView(restaurantRankListLayout);
        }

        return rootView;
    }
}
