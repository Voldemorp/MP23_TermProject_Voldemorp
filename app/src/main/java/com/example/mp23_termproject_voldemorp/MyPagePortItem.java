package com.example.mp23_termproject_voldemorp;

// Custom type declaration for storing restaurants ported in MyPage
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
