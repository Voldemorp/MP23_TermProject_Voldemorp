package com.example.mp23_termproject_voldemorp;

// 랭크 리스트에 유저 정보를 넣기 위한 배열의 커스텀 타입 선언
public class RestaurantRankUserInfo {
    String userName;
    String numOfVisit;

    RestaurantRankUserInfo(String restaurantName, String numOfPort) {
        this.userName = restaurantName;
        this.numOfVisit = numOfPort;
    }

    public String getUserName() {
        return userName;
    }
    public String getNumOfVisit() {
        return numOfVisit;
    }
}
