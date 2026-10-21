package com.foodapp.onlinefoodapp.service;

import com.foodapp.onlinefoodapp.model.Order;

import java.sql.SQLException;
import java.util.List;

public interface OrderService {

    Order placeOrder(Long userId, String deliveryAddress) throws SQLException;

    List<Order> getUserOrders(Long userId) throws SQLException;
}