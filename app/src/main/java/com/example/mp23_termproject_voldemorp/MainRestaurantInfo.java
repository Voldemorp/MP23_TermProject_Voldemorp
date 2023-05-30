package com.example.mp23_termproject_voldemorp;

public class MainRestaurantInfo {
    String placeName;
    String type;
    Boolean isHeartFilled;

    MainRestaurantInfo(String placeName, String type) {
        this.placeName = placeName;
        this.type = type;
        this.isHeartFilled = false;
    }

    MainRestaurantInfo(String placeName, String type, Boolean isHeartFilled) {
        this.placeName = placeName;
        this.type = type;
        this.isHeartFilled = isHeartFilled;
    }

    public String getPlaceName() {
        return placeName;
    }
    public String getType() {
        return type;
    }
    public boolean getIsHeartFilled() {
        return isHeartFilled;
    }
}
