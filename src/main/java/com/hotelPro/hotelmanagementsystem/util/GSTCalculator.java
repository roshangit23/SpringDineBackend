package com.hotelPro.hotelmanagementsystem.util;

import com.hotelPro.hotelmanagementsystem.model.FoodItem;
import com.hotelPro.hotelmanagementsystem.model.RestaurantSection;

public class GSTCalculator {
    public static double calculateGST(double amount, RestaurantSection.RestaurantType restaurantType, FoodItem.FoodType foodType) {
        double gstRate = 0;
        if (restaurantType == RestaurantSection.RestaurantType.AC || foodType == FoodItem.FoodType.ALCOHOLIC) {
            gstRate = 0.18;
        } else if (restaurantType == RestaurantSection.RestaurantType.NON_AC && foodType == FoodItem.FoodType.NON_ALCOHOLIC) {
            gstRate = 0.05;
        }
        return amount * gstRate;
    }
}
