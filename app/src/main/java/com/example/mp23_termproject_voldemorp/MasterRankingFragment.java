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

        // 랭크 띄울 정보를 저장하는 배열
        usersInfo = new ArrayList<>();

        //[서버] 식당별로 사용자 방문 데이터를 모두 찾아서 usersInfo 배열에 저장해야 함
        //[기능] 무작위로 정렬되어있을 usersInfo 배열을 방문수(numOfVisitPlace)대로 정렬

        // 테스트를 위한 더미데이터 (나중에 구현 성공하면 지우시면 됩니당)
        RankingInfo dummyInfo1 = new RankingInfo(1, "유짐이와농담곰", "53");
        RankingInfo dummyInfo2 = new RankingInfo(2, "포켓몬마스터지우", "25");
        RankingInfo dummyInfo3 = new RankingInfo(3, "수미칩은맛이있을까", "15");
        RankingInfo dummyInfo4 = new RankingInfo(4, "지연은서결혼합니다", "5");
        RankingInfo dummyInfo5 = new RankingInfo(5, "교수님살려주세요", "2");
        RankingInfo dummyInfo6 = new RankingInfo(6, "으아아악이건아니야", "1");
        usersInfo.add(dummyInfo1);
        usersInfo.add(dummyInfo2);
        usersInfo.add(dummyInfo3);
        usersInfo.add(dummyInfo4);
        usersInfo.add(dummyInfo5);
        usersInfo.add(dummyInfo6);

        ScrollView scrollView = rootView.findViewById(R.id.rankScrollView);
        LinearLayout linearLayout = rootView.findViewById(R.id.rankLinearView);


        // 식당 랭킹에 있는 사용자 수(배열 요소 수)만큼 컴포넌트를 스크롤 뷰에 추가
        for (int i = 0; i < usersInfo.size(); i++) {
            // 추가할 레이아웃
            RankingMasterListLayout rankingMasterListLayout = new RankingMasterListLayout(getContext(), usersInfo.get(i));
            // 추가 코드
            linearLayout.addView(rankingMasterListLayout);
        }

        return rootView;
    }
}