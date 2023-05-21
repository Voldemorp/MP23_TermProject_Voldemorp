package com.example.mp23_termproject_voldemorp;

// 마이페이지에 port한 식당 넣기 위한 배열의 커스텀 타입 선언
public class MyPagePortItem {
    String restaurantName;
    int numOfPort;

    MyPagePortItem(String restaurantName, int numOfPort) {
        this.restaurantName = restaurantName;
        this.numOfPort = numOfPort;
    }
    public String getRestaurantName() {
        return restaurantName;
    }
    public int getNumOfPort() {
        return numOfPort;
    }
}
