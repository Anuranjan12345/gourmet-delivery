package com.foodapp.onlinefoodapp.repository;

import com.foodapp.onlinefoodapp.model.OrderItem;
import com.foodapp.onlinefoodapp.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRepository {

    public OrderItem save(OrderItem item) throws SQLException {

        String sql = """
                INSERT INTO order_items
                (order_id, food_item_id, name, price, quantity)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, item.getOrderId());
            statement.setLong(2, item.getFoodItemId());
            statement.setString(3, item.getName());
            statement.setDouble(4, item.getPrice());
            statement.setInt(5, item.getQuantity());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    item.setId(resultSet.getLong(1));
                }
            }
        }

        return item;
    }

    public List<OrderItem> findByOrderId(Long orderId) throws SQLException {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    OrderItem item = new OrderItem();

                    item.setId(resultSet.getLong("id"));
                    item.setOrderId(resultSet.getLong("order_id"));
                    item.setFoodItemId(resultSet.getLong("food_item_id"));
                    item.setName(resultSet.getString("name"));
                    item.setPrice(resultSet.getDouble("price"));
                    item.setQuantity(resultSet.getInt("quantity"));

                    items.add(item);
                }
            }
        }

        return items;
    }
}