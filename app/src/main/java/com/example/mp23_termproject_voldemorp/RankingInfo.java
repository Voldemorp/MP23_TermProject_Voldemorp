package com.example.mp23_termproject_voldemorp;

public class RankingInfo {
    int rank; // Ranking position
    String userName; // User name
    String numOfVisit; // Number of visits
    String place; // Place name

    /**
     * Constructor for master ranking information.
     * @param rank The ranking position.
     * @param userName The user name.
     * @param numOfVisitPlace The number of visits to places.
     */
    RankingInfo(int rank, String userName, String numOfVisitPlace) {
        this.rank = rank;
        this.userName = userName;
        this.numOfVisit = numOfVisitPlace;
    }

    /**
     * Constructor for mania ranking information.
     * @param rank The ranking position.
     * @param userName The user name.
     * @param place The place name.
     * @param numOfVisit The number of visits to the place.
     */
    RankingInfo(int rank, String userName, String place, String numOfVisit) {
        this.rank = rank;
        this.userName = userName;
        this.place = place;
        this.numOfVisit = numOfVisit;
    }

    /**
     * Get the user name.
     * @return The user name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get the number of visits.
     * @return The number of visits.
     */
    public String getNumOfVisit() {
        return numOfVisit;
    }

    /**
     * Get the place name.
     * @return The place name.
     */
    public String getPlace() {
        return place;
    }
}
