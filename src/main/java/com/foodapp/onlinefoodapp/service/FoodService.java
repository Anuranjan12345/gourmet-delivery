package com.foodapp.onlinefoodapp.service;

import com.foodapp.onlinefoodapp.model.Food;

import java.sql.SQLException;
import java.util.List;

public interface FoodService {

    List<Food> getByRestaurant(Long restaurantId) throws SQLException;

    Food getById(Long foodId) throws SQLException;
}