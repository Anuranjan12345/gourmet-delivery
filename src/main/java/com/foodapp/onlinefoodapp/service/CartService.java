package com.foodapp.onlinefoodapp.service;

import com.foodapp.onlinefoodapp.model.Cart;

import java.sql.SQLException;
import java.util.List;

public interface CartService {

    Cart addToCart(Long userId, Long foodId, int quantity) throws SQLException;

    List<Cart> getUserCart(Long userId) throws SQLException;

    void updateQuantity(Long cartId, int quantity) throws SQLException;

    void removeFromCart(Long cartId) throws SQLException;

    void clearCart(Long userId) throws SQLException;
}