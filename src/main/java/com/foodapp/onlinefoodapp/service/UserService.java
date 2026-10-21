package com.foodapp.onlinefoodapp.service;

import com.foodapp.onlinefoodapp.model.User;

import java.sql.SQLException;

public interface UserService {

    User signup(User user) throws SQLException;

    User login(String email, String password) throws SQLException;
}