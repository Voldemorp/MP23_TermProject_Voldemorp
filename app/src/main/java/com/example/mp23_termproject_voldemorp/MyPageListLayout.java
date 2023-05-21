package com.example.mp23_termproject_voldemorp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

// port list 레이아웃에 추가할 데이터 세팅
public class MyPageListLayout extends LinearLayout {
    public MyPageListLayout(Context context, AttributeSet attrs, MyPagePortItem myPagePortItem) {
        super(context, attrs);
        init(context, myPagePortItem);
    }

    public MyPageListLayout(Context context, MyPagePortItem myPagePortItem) {
        super(context);
        init(context, myPagePortItem);
    }

    private void init(Context context, MyPagePortItem myPagePortItem) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_mypage_resturant, this, true);

        TextView restaurantName = (TextView)findViewById(R.id.restaurantName);
        TextView numOfPort = (TextView)findViewById(R.id.numOfPort);

        restaurantName.setText(myPagePortItem.restaurantName);
        numOfPort.setText(myPagePortItem.numOfPort);
    }
}
