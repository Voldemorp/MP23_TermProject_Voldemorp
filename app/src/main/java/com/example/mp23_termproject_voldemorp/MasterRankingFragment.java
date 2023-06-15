package com.example.mp23_termproject_voldemorp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MasterRankingFragment extends Fragment {

    ArrayList<RankingInfo> usersInfo;

    public MasterRankingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_ranking, container, false);

        // Array to store ranking information
        usersInfo = new ArrayList<>();

        // [Server] Retrieve user visit data for each restaurant and store it in the usersInfo array
        // [Functionality] Sort the usersInfo array randomly
        // usersInfo array is initially sorted randomly and does not require additional sorting

        // Dummy data for testing purposes (You can remove this later when the implementation is successful)
        RankingInfo dummyInfo1 = new RankingInfo(1, "포켓몬마스터지우", "32");
        RankingInfo dummyInfo2 = new RankingInfo(2, "안종현교수님최고", "29");
        RankingInfo dummyInfo3 = new RankingInfo(3, "발표잘듣고계신가요", "19");
        RankingInfo dummyInfo4 = new RankingInfo(4, "지연은서결혼합니다", "18");
        RankingInfo dummyInfo5 = new RankingInfo(5, "고양이귀여워", "11");
        RankingInfo dummyInfo6 = new RankingInfo(6, "곧종강", "10");

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
            RankingMasterListLayout rankingMasterListLayout = new RankingMasterListLayout(getContext(), usersInfo.get(i));
            // Add the layout
            linearLayout.addView(rankingMasterListLayout);
        }

        return rootView;
    }
}
