package com.example.mp23_termproject_voldemorp;

// Custom data type for storing user information in the rank list
public class RestaurantRankUserInfo {
    String userName; // User name
    String numOfVisit; // Number of visits

    // Constructor for initializing the user information
    RestaurantRankUserInfo(String userName, String numOfVisit) {
        this.userName = userName;
        this.numOfVisit = numOfVisit;
    }

    // Getter method for retrieving the user name
    public String getUserName() {
        return userName;
    }

    // Getter method for retrieving the number of visits
    public String getNumOfVisit() {
        return numOfVisit;
    }
}
