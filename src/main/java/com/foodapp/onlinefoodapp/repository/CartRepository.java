
package com.foodapp.onlinefoodapp.repository;

import com.foodapp.onlinefoodapp.model.Cart;
import com.foodapp.onlinefoodapp.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {

    public List<Cart> findByUserId(Long userId) throws SQLException {

        List<Cart> cartItems = new ArrayList<>();

        String sql = """
                SELECT
                    c.id,
                    c.user_id,
                    c.food_id,
                    c.quantity,
                    f.name AS food_name,
                    f.description AS food_description,
                    f.price AS food_price,
                    f.image_url AS food_image_url
                FROM cart_items c
                JOIN food f ON c.food_id = f.id
                WHERE c.user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Cart cart = new Cart();

                    cart.setId(resultSet.getLong("id"));
                    cart.setUserId(resultSet.getLong("user_id"));
                    cart.setFoodId(resultSet.getLong("food_id"));
                    cart.setQuantity(resultSet.getInt("quantity"));

                    cart.setFoodName(
                            resultSet.getString("food_name"));
                    cart.setFoodDescription(
                            resultSet.getString("food_description"));
                    cart.setFoodPrice(
                            resultSet.getDouble("food_price"));
                    cart.setFoodImageUrl(
                            resultSet.getString("food_image_url"));

                    double price = cart.getFoodPrice();
                    cart.setSubtotal(price * cart.getQuantity());

                    cartItems.add(cart);
                }
            }
        }

        return cartItems;
    }

    public Cart findByUserIdAndFoodId(
            Long userId, Long foodId) throws SQLException {

        String sql =
                "SELECT * FROM cart_items WHERE user_id = ? AND food_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, foodId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Cart cart = new Cart();

                    cart.setId(resultSet.getLong("id"));
                    cart.setUserId(resultSet.getLong("user_id"));
                    cart.setFoodId(resultSet.getLong("food_id"));
                    cart.setQuantity(resultSet.getInt("quantity"));

                    return cart;
                }
            }
        }

        return null;
    }

    public Cart save(Cart cart) throws SQLException {

        String sql =
                "INSERT INTO cart_items (user_id, food_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, cart.getUserId());
            statement.setLong(2, cart.getFoodId());
            statement.setInt(3, cart.getQuantity());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    cart.setId(resultSet.getLong(1));
                }
            }
        }

        return cart;
    }

public void updateQuantity(
            Long cartId, int quantity) throws SQLException {

        String sql =
                "UPDATE cart_items SET quantity = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setLong(2, cartId);

            statement.executeUpdate();
        }
    }

    public void deleteById(Long cartId) throws SQLException {

        String sql = "DELETE FROM cart_items WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, cartId);

            statement.executeUpdate();
        }
    }

     public void deleteByUserId(Long userId) throws SQLException {

        String sql = "DELETE FROM cart_items WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            statement.executeUpdate();
        }
    }
}
