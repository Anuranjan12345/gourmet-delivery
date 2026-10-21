package com.foodapp.onlinefoodapp.repository;

import com.foodapp.onlinefoodapp.model.Order;
import com.foodapp.onlinefoodapp.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    public Order save(Order order) throws SQLException {

        String sql = """
                INSERT INTO orders
                (user_id, total_amount, delivery_address, status, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, order.getUserId());
            statement.setDouble(2, order.getTotalAmount());
            statement.setString(3, order.getDeliveryAddress());
            statement.setString(4, order.getStatus());
            statement.setTimestamp(5, Timestamp.valueOf(order.getCreatedAt()));

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    order.setId(resultSet.getLong(1));
                }
            }
        }

        return order;
    }

    public List<Order> findByUserId(Long userId) throws SQLException {

        List<Order> orders = new ArrayList<>();

        String sql = """
                SELECT * FROM orders
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Order order = new Order();

                    order.setId(resultSet.getLong("id"));
                    order.setUserId(resultSet.getLong("user_id"));
                    order.setTotalAmount(resultSet.getDouble("total_amount"));
                    order.setDeliveryAddress(
                            resultSet.getString("delivery_address"));
                    order.setStatus(resultSet.getString("status"));

                    Timestamp timestamp = resultSet.getTimestamp("created_at");

                    if (timestamp != null) {
                        order.setCreatedAt(timestamp.toLocalDateTime());
                    }

                    orders.add(order);
                }
            }
        }

        return orders;
    }
}