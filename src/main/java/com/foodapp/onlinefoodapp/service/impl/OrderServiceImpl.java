package com.foodapp.onlinefoodapp.service.impl;

import com.foodapp.onlinefoodapp.model.Cart;
import com.foodapp.onlinefoodapp.model.Food;
import com.foodapp.onlinefoodapp.model.Order;
import com.foodapp.onlinefoodapp.model.OrderItem;
import com.foodapp.onlinefoodapp.repository.CartRepository;
import com.foodapp.onlinefoodapp.repository.FoodRepository;
import com.foodapp.onlinefoodapp.repository.OrderItemRepository;
import com.foodapp.onlinefoodapp.repository.OrderRepository;
import com.foodapp.onlinefoodapp.service.OrderService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final FoodRepository foodRepository;

    public OrderServiceImpl() {
        this.orderRepository = new OrderRepository();
        this.orderItemRepository = new OrderItemRepository();
        this.cartRepository = new CartRepository();
        this.foodRepository = new FoodRepository();
    }

    @Override
    public Order placeOrder(Long userId, String deliveryAddress) throws SQLException {

        List<Cart> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        double totalAmount = 0.0;

        List<OrderItem> orderItems = new ArrayList<>();

        for (Cart cart : cartItems) {

            Food food = foodRepository.findById(cart.getFoodId());

            if (food == null) {
                throw new IllegalArgumentException("Food not found");
            }

            double itemTotal = food.getPrice() * cart.getQuantity();

            totalAmount += itemTotal;

            OrderItem orderItem = new OrderItem(
                    cart.getFoodId(),
                    food.getName(),
                    food.getPrice(),
                    cart.getQuantity()
            );

            orderItems.add(orderItem);
        }

        Order order = new Order();

        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setDeliveryAddress(deliveryAddress);
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        for (OrderItem orderItem : orderItems) {

            orderItem.setOrderId(savedOrder.getId());

            orderItemRepository.save(orderItem);
        }

        cartRepository.deleteByUserId(userId);

        return savedOrder;
    }

    @Override
    public List<Order> getUserOrders(Long userId) throws SQLException {

        List<Order> orders = orderRepository.findByUserId(userId);

        for (Order order : orders) {

            List<OrderItem> items =
                    orderItemRepository.findByOrderId(order.getId());

            order.setItems(items);
        }

        return orders;
    }
}