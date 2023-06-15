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

        TextView rank = findViewById(R.id.rank);
        TextView userName = findViewById(R.id.name);
        TextView numOfVisit = findViewById(R.id.numOfVisit);
        TextView userVisited = findViewById(R.id.userVisited);

        // Set the ranking information to the respective views
        rank.setText(Integer.toString(rankingInfo.rank)); // Set the rank position
        userName.setText(rankingInfo.userName); // Set the user name
        numOfVisit.setText(rankingInfo.numOfVisit); // Set the number of visits
        userVisited.setText(rankingInfo.place); // Set the place visited by the user
    }
}
