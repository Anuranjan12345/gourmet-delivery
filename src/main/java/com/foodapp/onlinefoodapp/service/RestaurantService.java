package com.foodapp.onlinefoodapp.service;

import com.foodapp.onlinefoodapp.model.Restaurant;

import java.sql.SQLException;
import java.util.List;

public interface RestaurantService {

    List<Restaurant> getAllRestaurants() throws SQLException;

    Restaurant getRestaurantById(Long restaurantId) throws SQLException;
}