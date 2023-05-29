package com.example.mp23_termproject_voldemorp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankingManiaListLayout extends LinearLayout {

    public RankingManiaListLayout(Context context, AttributeSet attrs, RankingInfo rankingInfo) {
        super(context, attrs);
        init(context, rankingInfo);
    }

    public RankingManiaListLayout(Context context, RankingInfo rankingInfo) {
        super(context);
        init(context, rankingInfo);
    }

    private void init(Context context, RankingInfo rankingInfo) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_mania_ranking, this, true);

        TextView userName = (TextView)findViewById(R.id.name);
        TextView numOfVisit = (TextView)findViewById(R.id.numOfVisit);
        TextView userVisited = (TextView)findViewById(R.id.userVisited);

        userName.setText(rankingInfo.userName);
        numOfVisit.setText(rankingInfo.numOfVisit);
        userVisited.setText(rankingInfo.place);
    }
}
