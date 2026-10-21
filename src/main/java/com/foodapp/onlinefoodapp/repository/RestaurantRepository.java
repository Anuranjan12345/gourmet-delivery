package com.foodapp.onlinefoodapp.repository;

import com.foodapp.onlinefoodapp.model.Restaurant;
import com.foodapp.onlinefoodapp.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {

    public List<Restaurant> findAll() throws SQLException {

        List<Restaurant> restaurants = new ArrayList<>();

        String sql = "SELECT * FROM restaurants";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

          while (resultSet.next()) {

                Restaurant restaurant = new Restaurant();

                restaurant.setId(resultSet.getLong("id"));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setAddress(resultSet.getString("address"));
                restaurant.setRating(resultSet.getDouble("rating"));
                restaurant.setCategory(resultSet.getString("category"));
                restaurant.setImageUrl(resultSet.getString("image_url"));
                restaurant.setLocation(resultSet.getString("location"));

                restaurants.add(restaurant);
            }
        }

        return restaurants;
    }

public Restaurant findById(Long restaurantId) throws SQLException {

        String sql = "SELECT * FROM restaurants WHERE id = ?";

    try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, restaurantId);

      try (ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {

                    Restaurant restaurant = new Restaurant();

                    restaurant.setId(resultSet.getLong("id"));
                    restaurant.setName(resultSet.getString("name"));
                    restaurant.setAddress(resultSet.getString("address"));
                    restaurant.setRating(resultSet.getDouble("rating"));
                    restaurant.setCategory(resultSet.getString("category"));
                    restaurant.setImageUrl(resultSet.getString("image_url"));
                    restaurant.setLocation(resultSet.getString("location"));

                    return restaurant;
                }
            }
        }

        return null;
    }
}