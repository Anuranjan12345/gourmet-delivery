package com.foodapp.onlinefoodapp.service.impl;

import com.foodapp.onlinefoodapp.model.Food;
import com.foodapp.onlinefoodapp.repository.FoodRepository;
import com.foodapp.onlinefoodapp.service.FoodService;

import java.sql.SQLException;
import java.util.List;

public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    public FoodServiceImpl() {
        this.foodRepository = new FoodRepository();
    }

    @Override
    public List<Food> getByRestaurant(Long restaurantId) throws SQLException {
        return foodRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public Food getById(Long foodId) throws SQLException {
        return foodRepository.findById(foodId);
    }
}