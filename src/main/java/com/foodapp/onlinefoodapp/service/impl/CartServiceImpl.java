package com.foodapp.onlinefoodapp.service.impl;

import com.foodapp.onlinefoodapp.model.Cart;
import com.foodapp.onlinefoodapp.model.Food;
import com.foodapp.onlinefoodapp.repository.CartRepository;
import com.foodapp.onlinefoodapp.repository.FoodRepository;
import com.foodapp.onlinefoodapp.service.CartService;

import java.sql.SQLException;
import java.util.List;

public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final FoodRepository foodRepository;

    public CartServiceImpl() {
        this.cartRepository = new CartRepository();
        this.foodRepository = new FoodRepository();
    }

    @Override
    public Cart addToCart(Long userId, Long foodId, int quantity) throws SQLException {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Food food = foodRepository.findById(foodId);

        if (food == null) {
            throw new IllegalArgumentException("Food not found");
        }

        Cart existingCart = cartRepository.findByUserIdAndFoodId(userId, foodId);

        if (existingCart != null) {

            int newQuantity = existingCart.getQuantity() + quantity;

            cartRepository.updateQuantity(
                    existingCart.getId(),
                    newQuantity
            );

            existingCart.setQuantity(newQuantity);

            return existingCart;
        }

        Cart cart = new Cart(userId, foodId, quantity);

        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getUserCart(Long userId) throws SQLException {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void updateQuantity(Long cartId, int quantity) throws SQLException {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        cartRepository.updateQuantity(cartId, quantity);
    }

    @Override
    public void removeFromCart(Long cartId) throws SQLException {
        cartRepository.deleteById(cartId);
    }

    @Override
    public void clearCart(Long userId) throws SQLException {
        cartRepository.deleteByUserId(userId);
    }
}