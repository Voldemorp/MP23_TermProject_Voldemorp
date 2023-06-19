package com.example.mp23_termproject_voldemorp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RestaurantRankListLayout extends LinearLayout {

    public RestaurantRankListLayout(Context context, AttributeSet attrs, RestaurantRankUserInfo restaurantRankUserInfo) {
        super(context, attrs);
        init(context, restaurantRankUserInfo);
    }

    public RestaurantRankListLayout(Context context, RestaurantRankUserInfo restaurantRankUserInfo) {
        super(context);
        init(context, restaurantRankUserInfo);
    }

    private void init(Context context, RestaurantRankUserInfo restaurantRankUserInfo) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_restaurant_rank, this, true);


        TextView userName = (TextView)findViewById(R.id.name);
        TextView numOfVisit = (TextView)findViewById(R.id.numOfVisit);

        userName.setText(restaurantRankUserInfo.userName);
        numOfVisit.setText(restaurantRankUserInfo.numOfVisit);
    }
}
