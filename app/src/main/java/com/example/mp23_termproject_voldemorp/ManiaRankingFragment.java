package com.example.mp23_termproject_voldemorp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ManiaRankingFragment extends Fragment {

    ArrayList<RankingInfo> usersInfo;

    public ManiaRankingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mania_ranking, container, false);

        // Array to store ranking information
        usersInfo = new ArrayList<>();

        // [Server] Retrieve user visit data for each restaurant and store it in the usersInfo array
        // [Functionality] Sort the usersInfo array in descending order based on the number of visits (numOfVisit)

        // Dummy data for testing purposes (You can remove this later when the implementation is successful)
        RankingInfo dummyInfo1 = new RankingInfo(1, "유짐이와농담곰", "행복로드마라탕", "61");
        RankingInfo dummyInfo2 = new RankingInfo(2, "포켓몬마스터지우", "겐코", "32");
        RankingInfo dummyInfo3 = new RankingInfo(3, "수미칩은맛이있을까", "디왈리", "27");
        RankingInfo dummyInfo4 = new RankingInfo(4, "지연은서결혼합니다", "육연차", "19");
        RankingInfo dummyInfo5 = new RankingInfo(5, "교수님살려주세요", "1988", "17");
        RankingInfo dummyInfo6 = new RankingInfo(6, "모프에이쁠기원", "쩡이순대국", "15");
        usersInfo.add(dummyInfo1);
        usersInfo.add(dummyInfo2);
        usersInfo.add(dummyInfo3);
        usersInfo.add(dummyInfo4);
        usersInfo.add(dummyInfo5);
        usersInfo.add(dummyInfo6);

        ScrollView scrollView = rootView.findViewById(R.id.rankScrollView);
        LinearLayout linearLayout = rootView.findViewById(R.id.rankLinearView);

        // Add components to the scroll view for each user in the restaurant ranking
        for (int i = 0; i < usersInfo.size(); i++) {
            // Create a layout to be added
            RankingManiaListLayout rankingManiaListLayout = new RankingManiaListLayout(getContext(), usersInfo.get(i));
            // Add the layout
            linearLayout.addView(rankingManiaListLayout);
        }

        return rootView;
    }
}
