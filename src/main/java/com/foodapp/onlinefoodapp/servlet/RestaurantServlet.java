package com.foodapp.onlinefoodapp.servlet;

import com.foodapp.onlinefoodapp.model.Restaurant;
import com.foodapp.onlinefoodapp.service.RestaurantService;
import com.foodapp.onlinefoodapp.service.impl.RestaurantServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/restaurants/*")
public class RestaurantServlet extends HttpServlet {

    private RestaurantService restaurantService;

    @Override
    public void init() throws ServletException {
        restaurantService = new RestaurantServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        try (PrintWriter out = response.getWriter()) {

            if (path == null || "/".equals(path) || path.isEmpty()) {

                List<Restaurant> restaurants =
                        restaurantService.getAllRestaurants();

                out.print("[");

                for (int i = 0; i < restaurants.size(); i++) {

                    Restaurant restaurant = restaurants.get(i);

                    if (i > 0) {
                        out.print(",");
                    }

                    out.print("""
                            {
                                "id": %d,
                                "name": "%s",
                                "address": "%s",
                                "rating": %.1f,
                                "cuisine": "%s",
                                "image": "%s",
                                "location": "%s",
                                "delivery": "Free Delivery",
                                "time": "30-40 min"
                            }
                            """.formatted(
                            restaurant.getId(),
                            escapeJson(restaurant.getName()),
                            escapeJson(restaurant.getAddress()),
                            restaurant.getRating(),
                            escapeJson(restaurant.getCategory()),
                            escapeJson(restaurant.getImageUrl()),
                            escapeJson(restaurant.getLocation())
                    ));
                }

                out.print("]");

            } else {

                try {

                    Long restaurantId =
                            Long.parseLong(path.substring(1));

                    Restaurant restaurant =
                            restaurantService.getRestaurantById(restaurantId);

                    if (restaurant == null) {

                        response.setStatus(
                                HttpServletResponse.SC_NOT_FOUND);

                        out.print("""
                                {
                                    "error": "Restaurant not found"
                                }
                                """);

                        return;
                    }

                    out.print("""
                            {
                                "id": %d,
                                "name": "%s",
                                "address": "%s",
                                "rating": %.1f,
                                "cuisine": "%s",
                                "image": "%s",
                                "location": "%s",
                                "delivery": "Free Delivery",
                                "time": "30-40 min"
                            }
                            """.formatted(
                            restaurant.getId(),
                            escapeJson(restaurant.getName()),
                            escapeJson(restaurant.getAddress()),
                            restaurant.getRating(),
                            escapeJson(restaurant.getCategory()),
                            escapeJson(restaurant.getImageUrl()),
                            escapeJson(restaurant.getLocation())
                    ));

                } catch (NumberFormatException e) {

                    response.setStatus(
                            HttpServletResponse.SC_BAD_REQUEST);

                    out.print("""
                            {
                                "error": "Invalid restaurant ID"
                            }
                            """);
                }
            }

        } catch (Exception e) {

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

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