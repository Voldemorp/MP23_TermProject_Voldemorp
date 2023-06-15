package com.example.mp23_termproject_voldemorp;

public class MainRestaurantInfo {

    double x;
    double y;
    String name;
    String foodType;

    public MainRestaurantInfo(double x, double y, String name, String foodType) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.foodType = foodType;
    }

    public String getPlaceName() {
        return name;
    }

    public String getType() {
        return foodType;
    }
}
