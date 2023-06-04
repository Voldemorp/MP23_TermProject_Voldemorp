package com.example.mp23_termproject_voldemorp;

public class RankingInfo {
    int rank;
    String userName;
    String numOfVisit;
    String place;

    // 마스터 지수
    RankingInfo(int rank, String userName, String numOfVisitPlace) {
        this.rank = rank;
        this.userName = userName;
        this.numOfVisit = numOfVisitPlace;
    }

    // 매니아 지수
    RankingInfo(int rank, String userName, String place, String numOfVisit) {
        this.rank = rank;
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
