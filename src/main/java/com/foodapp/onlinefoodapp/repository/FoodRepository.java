package com.foodapp.onlinefoodapp.repository;

import com.foodapp.onlinefoodapp.model.Food;
import com.foodapp.onlinefoodapp.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodRepository {

    public List<Food> findByRestaurantId(Long restaurantId) throws SQLException {

        List<Food> foods = new ArrayList<>();

        String sql = "SELECT * FROM food WHERE restaurant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, restaurantId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Food food = new Food();

                    food.setId(resultSet.getLong("id"));
                    food.setName(resultSet.getString("name"));
                    food.setDescription(resultSet.getString("description"));
                    food.setPrice(resultSet.getDouble("price"));
                    food.setRestaurantId(resultSet.getLong("restaurant_id"));
                    food.setImageUrl(resultSet.getString("image_url"));

                    foods.add(food);
                }
            }
        }

        return foods;
    }

    public Food findById(Long foodId) throws SQLException {

        String sql = "SELECT * FROM food WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, foodId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Food food = new Food();

                    food.setId(resultSet.getLong("id"));
                    food.setName(resultSet.getString("name"));
                    food.setDescription(resultSet.getString("description"));
                    food.setPrice(resultSet.getDouble("price"));
                    food.setRestaurantId(resultSet.getLong("restaurant_id"));
                    food.setImageUrl(resultSet.getString("image_url"));

                    return food;
                }
            }
        }

        return null;
    }
}