package com.example.mp23_termproject_voldemorp;

// 마이페이지에 port한 식당 넣기 위한 배열의 커스텀 타입 선언
public class MyPagePortItem {
    String mypageRestaurantTextView;
    int mypagePort;

    MyPagePortItem(String mypageRestaurantTextView, int mypagePort) {
        this.mypageRestaurantTextView = mypageRestaurantTextView;
        this.mypagePort = mypagePort;
    }
    public String getMypageRestaurantTextView() {
        return mypageRestaurantTextView;
    }

    public int getMypagePort() {
        return mypagePort;
    }
}
