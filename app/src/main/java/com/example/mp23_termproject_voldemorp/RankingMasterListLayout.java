package com.example.mp23_termproject_voldemorp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankingMasterListLayout extends LinearLayout {

    public RankingMasterListLayout(Context context, AttributeSet attrs, RankingInfo rankingInfo) {
        super(context, attrs);
        init(context, rankingInfo);
    }

    public RankingMasterListLayout(Context context, RankingInfo rankingInfo) {
        super(context);
        init(context, rankingInfo);
    }

    private void init(Context context, RankingInfo rankingInfo) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_master_ranking, this, true);

        TextView rank = (TextView)findViewById(R.id.rank);
        TextView userName = (TextView)findViewById(R.id.name);
        TextView numOfVisitPlace = (TextView)findViewById(R.id.numOfVisit);

        rank.setText(Integer.toString(rankingInfo.rank));
        userName.setText(rankingInfo.userName);
        numOfVisitPlace.setText(rankingInfo.numOfVisit);
    }
}
