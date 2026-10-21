package com.foodapp.onlinefoodapp.model;

public class OrderItem {

    private Long id;
    private Long orderId;
    private Long foodItemId;
    private String name;
    private Double price;
    private int quantity;

    public OrderItem() {
    }

    public OrderItem(Long id, Long orderId, Long foodItemId,
                     String name, Double price, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.foodItemId = foodItemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItem(Long foodItemId, String name, Double price, int quantity) {
        this.foodItemId = foodItemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}