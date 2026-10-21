package com.foodapp.onlinefoodapp.service.impl;

import com.foodapp.onlinefoodapp.model.Restaurant;
import com.foodapp.onlinefoodapp.repository.RestaurantRepository;
import com.foodapp.onlinefoodapp.service.RestaurantService;

import java.sql.SQLException;
import java.util.List;

public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl() {
        this.restaurantRepository = new RestaurantRepository();
    }

    @Override
    public List<Restaurant> getAllRestaurants() throws SQLException {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant getRestaurantById(Long restaurantId) throws SQLException {
        return restaurantRepository.findById(restaurantId);
    }
}