package com.example.mp23_termproject_voldemorp;
public class MainRestaurantInfo {

    double x;
    double y;
    String name;
    String foodType;
    Boolean isHeartFilled;

    public MainRestaurantInfo(double x, double y, String name, String foodType) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.foodType = foodType;
    }

    public MainRestaurantInfo(double x, double y, String name, String foodType, Boolean isHeartFilled) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.foodType = foodType;
        this.isHeartFilled = isHeartFilled;
    }

    public String getPlaceName() {
        return name;
    }
    public String getType() {
        return foodType;
    }
    public boolean getIsHeartFilled() {
        return isHeartFilled;
    }
}
