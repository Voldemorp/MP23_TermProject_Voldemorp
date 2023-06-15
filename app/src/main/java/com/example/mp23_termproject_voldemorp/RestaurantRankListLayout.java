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
        // Inflate the layout for the custom list item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_restaurant_rank, this, true);

        // Get references to the TextViews in the custom layout
        TextView userName = findViewById(R.id.name);
        TextView numOfVisit = findViewById(R.id.numOfVisit);

        // Set the user name and number of visits in the TextViews
        userName.setText(restaurantRankUserInfo.userName);
        numOfVisit.setText(restaurantRankUserInfo.numOfVisit);
    }
}
