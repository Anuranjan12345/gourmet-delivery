
package com.foodapp.onlinefoodapp.service.impl;

import com.foodapp.onlinefoodapp.model.User;
import com.foodapp.onlinefoodapp.repository.UserRepository;
import com.foodapp.onlinefoodapp.service.UserService;
import com.foodapp.onlinefoodapp.util.PasswordUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl() {
        this.userRepository = new UserRepository();
    }

    @Override
    public User signup(User user) throws SQLException {

        if (user == null
                || user.getName() == null
                || user.getName().isBlank()
                || user.getEmail() == null
                || user.getEmail().isBlank()
                || user.getPassword() == null
                || user.getPassword().isEmpty()) {

            throw new IllegalArgumentException(
                    "Name, email and password are required"
            );
        }

        User existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new IllegalArgumentException(
                    "Email already registered"
            );
        }

        user.setPassword(
                PasswordUtil.hash(user.getPassword())
        );

        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password)
            throws SQLException {

        if (email == null || email.isBlank()
                || password == null || password.isEmpty()) {

            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        String storedPassword = user.getPassword();

        if (storedPassword == null) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        boolean authenticated;

        if (PasswordUtil.isHashed(storedPassword)) {

            // Verify a previously hashed password.
            authenticated =
                    PasswordUtil.verify(password, storedPassword);

        } else {

            // Support existing accounts that still have
            // a legacy plain-text password.
            authenticated = MessageDigest.isEqual(
                    password.getBytes(StandardCharsets.UTF_8),
                    storedPassword.getBytes(StandardCharsets.UTF_8)
            );

            if (authenticated) {

                // Upgrade the legacy password after successful login.
                String passwordHash = PasswordUtil.hash(password);

                userRepository.updatePassword(
                        user.getId(),
                        passwordHash
                );

                user.setPassword(passwordHash);
            }
        }

        if (!authenticated) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        return user;
    }
}
