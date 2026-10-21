package com.foodapp.onlinefoodapp.servlet;

import com.foodapp.onlinefoodapp.model.User;
import com.foodapp.onlinefoodapp.service.UserService;
import com.foodapp.onlinefoodapp.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        try (PrintWriter out = response.getWriter()) {

            if ("/signup".equals(path)) {

                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String password = request.getParameter("password");

                User user = new User(name, email, password);

                User savedUser = userService.signup(user);

                response.setStatus(HttpServletResponse.SC_CREATED);

                out.print("""
                        {
                            "message": "Signup successful",
                            "id": %d,
                            "name": "%s",
                            "email": "%s"
                        }
                        """.formatted(
                        savedUser.getId(),
                        escapeJson(savedUser.getName()),
                        escapeJson(savedUser.getEmail())
                ));

            } else if ("/login".equals(path)) {

                String email = request.getParameter("email");
                String password = request.getParameter("password");

                User user = userService.login(email, password);

                response.setStatus(HttpServletResponse.SC_OK);

                out.print("""
                        {
                            "message": "Login successful",
                            "id": %d,
                            "name": "%s",
                            "email": "%s"
                        }
                        """.formatted(
                        user.getId(),
                        escapeJson(user.getName()),
                        escapeJson(user.getEmail())
                ));

            } else {

                response.setStatus(HttpServletResponse.SC_NOT_FOUND);

                out.print("""
                        {
                            "error": "Endpoint not found"
                        }
                        """);
            }

        } catch (IllegalArgumentException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            try (PrintWriter out = response.getWriter()) {
                out.print("""
                        {
                            "error": "%s"
                        }
                        """.formatted(escapeJson(e.getMessage())));
            }

        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            try (PrintWriter out = response.getWriter()) {
                out.print("""
                        {
                            "error": "Internal server error"
                        }
                        """);
            }

            e.printStackTrace();
        }
    }

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}