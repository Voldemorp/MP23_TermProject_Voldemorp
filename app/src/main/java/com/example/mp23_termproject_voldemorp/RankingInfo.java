package com.example.mp23_termproject_voldemorp;

public class RankingInfo {
    String userName;
    String numOfVisit;
    String place;

    // 마스터 지수
    RankingInfo(String userName, String numOfVisitPlace) {
        this.userName = userName;
        this.numOfVisit = numOfVisitPlace;
    }

    // 매니아 지수
    RankingInfo(String userName, String place, String numOfVisit) {
        this.userName = userName;
        this.place = place;
        this.numOfVisit = numOfVisit;
    }

    public String getUserName() {
        return userName;
    }
    public String getNumOfVisit() {
        return numOfVisit;
    }
    public String getPlace() {
        return place;
    }
}
