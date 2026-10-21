package com.foodapp.onlinefoodapp.servlet;

import com.foodapp.onlinefoodapp.model.Food;
import com.foodapp.onlinefoodapp.service.FoodService;
import com.foodapp.onlinefoodapp.service.impl.FoodServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/food/*")
public class FoodServlet extends HttpServlet {

    private FoodService foodService;

    @Override
    public void init() throws ServletException {
        foodService = new FoodServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        try (PrintWriter out = response.getWriter()) {

            if (path == null || path.equals("/")) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                out.print("""
                        {
                            "error": "Restaurant ID is required"
                        }
                        """);

                return;
            }

            // /api/food/{restaurantId}
            String idPart = path.substring(1);

            try {

                Long restaurantId = Long.parseLong(idPart);

                List<Food> foods =
                        foodService.getByRestaurant(restaurantId);

                out.print("[");

                for (int i = 0; i < foods.size(); i++) {

                    Food food = foods.get(i);

                    if (i > 0) {
                        out.print(",");
                    }

                    out.print("""
                            {
                                "id": %d,
                                "name": "%s",
                                "description": "%s",
                                "price": %.2f,
                                "restaurantId": %d,
                                "image": "%s"
                            }
                            """.formatted(
                            food.getId(),
                            escapeJson(food.getName()),
                            escapeJson(food.getDescription()),
                            food.getPrice(),
                            food.getRestaurantId(),
                            getFoodImage(food.getName())
                    ));
                }

                out.print("]");

            } catch (NumberFormatException e) {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST);

                out.print("""
                        {
                            "error": "Invalid restaurant ID"
                        }
                        """);
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

private String getFoodImage(String foodName) {

    if (foodName == null) {
        return "";
    }

    String name = foodName.toLowerCase();

    if (name.contains("zinger burger")) {
        return "images/foods/cheeseburger.png";
    }

    if (name.equals("burger") || name.contains("chicken burger")) {
        return "images/foods/whopper.png";
    }

    if (name.contains("margherita")) {
        return "images/foods/margherita.png";
    }

    if (name.contains("pepperoni")) {
        return "images/foods/pepperoni.png";
    }

    if (name.contains("nachos")) {
        return "images/foods/nachos.png";
    }

    if (name.contains("salmon nigiri")) {
        return "images/foods/salmon-nigiri.png";
    }

    if (name.contains("spicy tuna")) {
        return "images/foods/spicy-tuna.png";
    }

    if (name.equals("coke")) {
    return "images/foods/coke.png";
}

if (name.contains("chicken biryani")) {
    return "images/foods/chicken-biryani.jpg";
}

if (name.equals("dosa")) {
    return "images/foods/dosa.jpg";
}

if (name.equals("pizza")) {
    return "images/foods/pizza.jpg";
}

if (name.contains("fresh salad bowl")) {
    return "images/foods/fresh-salad-bowl.jpg";
}

if (name.contains("veggie salad")) {
    return "images/foods/veggie-salad.jpg";
}

if (name.contains("tuna salad")) {
    return "images/foods/tuna-salad.jpg";
}

if (name.contains("grilled steak")) {
    return "images/foods/grilled-steak.jpg";
}

if (name.contains("pepper steak")) {
    return "images/foods/pepper-steak.jpg";
}

if (name.contains("sushi platter")) {
    return "images/foods/sushi-platter.jpg";
}

return "";
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