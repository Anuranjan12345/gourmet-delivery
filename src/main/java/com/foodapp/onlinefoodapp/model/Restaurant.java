package com.foodapp.onlinefoodapp.model;

public class Restaurant {

    private Long id;
    private String name;
    private String address;
    private Double rating;
    private String category;
    private String imageUrl;
    private String location;

    public Restaurant() {
    }

    public Restaurant(Long id, String name, String address, Double rating,
                      String category, String imageUrl, String location) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.category = category;
        this.imageUrl = imageUrl;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}