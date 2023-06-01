package com.example.mp23_termproject_voldemorp;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainRestaurantListLayout extends LinearLayout {

    public MainRestaurantListLayout(Context context, AttributeSet attrs, MainRestaurantInfo mainRestaurantInfo) {
        super(context, attrs);
        init(context, mainRestaurantInfo);
    }

    public MainRestaurantListLayout(Context context, MainRestaurantInfo mainRestaurantInfo) {
        super(context);
        init(context, mainRestaurantInfo);
    }

    private void init(Context context, MainRestaurantInfo mainRestaurantInfo) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_main, this, true);

        TextView placeName = (TextView)findViewById(R.id.restaurantName);
        TextView restaurantType = (TextView)findViewById(R.id.restaurantType);
        ImageButton heartButton = (ImageButton)findViewById(R.id.heartButton);

        placeName.setText(mainRestaurantInfo.name);
        restaurantType.setText(mainRestaurantInfo.foodType);

        // 유저가 이 곳을 추천했는지 여부에 따라 하트 채워지도록 조정
        // userVisited.setText(mainRestaurantInfo.isHeartFilled);

        // Set OnClickListener for the list item
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch RestaurantActivity with selected restaurant's name and type
                Intent intent = new Intent(context, RestaurantActivity.class);
                intent.putExtra("name", mainRestaurantInfo.name);
                intent.putExtra("type", mainRestaurantInfo.foodType);
                intent.putExtra("res_lat", mainRestaurantInfo.x);
                intent.putExtra("res_long", mainRestaurantInfo.y);
                context.startActivity(intent);
            }
        });
    }
}
