package com.foodapp.onlinefoodapp.servlet;

import com.foodapp.onlinefoodapp.model.Cart;
import com.foodapp.onlinefoodapp.service.CartService;
import com.foodapp.onlinefoodapp.service.impl.CartServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/cart/*")
public class CartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        cartService = new CartServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        try (PrintWriter out = response.getWriter()) {

            if (path == null || path.equals("/") || path.isEmpty()) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                out.print("""
                        {
                            "error": "User ID is required"
                        }
                        """);

                return;
            }

            try {

                Long userId = Long.parseLong(path.substring(1));

                List<Cart> cartItems =
                        cartService.getUserCart(userId);

                out.print("[");

                for (int i = 0; i < cartItems.size(); i++) {

                    Cart cart = cartItems.get(i);

                    if (i > 0) {
                        out.print(",");
                    }

                    out.print("""
    {
        "id": %d,
        "userId": %d,
        "foodId": %d,
        "quantity": %d,
        "foodName": "%s",
        "foodDescription": "%s",
        "foodPrice": %s,
        "foodImageUrl": "%s",
        "subtotal": %s
    }
    """.formatted(
        cart.getId(),
        cart.getUserId(),
        cart.getFoodId(),
        cart.getQuantity(),
        escapeJson(cart.getFoodName()),
        escapeJson(cart.getFoodDescription()),
        cart.getFoodPrice(),
        escapeJson(cart.getFoodImageUrl()),
        cart.getSubtotal()
));
                }

                out.print("]");

            } catch (NumberFormatException e) {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST);

                out.print("""
                        {
                            "error": "Invalid user ID"
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

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String userIdParam = request.getParameter("userId");
            String foodIdParam = request.getParameter("foodId");
            String quantityParam = request.getParameter("quantity");

            if (userIdParam == null ||
                foodIdParam == null ||
                quantityParam == null) {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST);

                out.print("""
                        {
                            "error": "userId, foodId and quantity are required"
                        }
                        """);

                return;
            }

            Long userId = Long.parseLong(userIdParam);
            Long foodId = Long.parseLong(foodIdParam);
            int quantity = Integer.parseInt(quantityParam);

            Cart cart = cartService.addToCart(
                    userId,
                    foodId,
                    quantity
            );

            response.setStatus(HttpServletResponse.SC_CREATED);

            out.print("""
                    {
                        "message": "Food added to cart",
                        "id": %d,
                        "userId": %d,
                        "foodId": %d,
                        "quantity": %d
                    }
                    """.formatted(
                    cart.getId(),
                    cart.getUserId(),
                    cart.getFoodId(),
                    cart.getQuantity()
            ));

        } catch (NumberFormatException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST);

            try (PrintWriter out = response.getWriter()) {
                out.print("""
                        {
                            "error": "Invalid numeric value"
                        }
                        """);
            }

        } catch (IllegalArgumentException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST);

            try (PrintWriter out = response.getWriter()) {
                out.print("""
                        {
                            "error": "%s"
                        }
                        """.formatted(
                        escapeJson(e.getMessage())
                ));
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

    @Override
    protected void doPut(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String cartIdParam = request.getParameter("cartId");
            String quantityParam = request.getParameter("quantity");

            if (cartIdParam == null || quantityParam == null) {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST);

                out.print("""
                        {
                            "error": "cartId and quantity are required"
                        }
                        """);

                return;
            }

            Long cartId = Long.parseLong(cartIdParam);
            int quantity = Integer.parseInt(quantityParam);

            cartService.updateQuantity(cartId, quantity);

            out.print("""
                    {
                        "message": "Cart quantity updated"
                    }
                    """);

        } catch (NumberFormatException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST);

            try (PrintWriter out = response.getWriter()) {
                out.print("""
                        {
                            "error": "Invalid numeric value"
                        }
                        """);
            }

        } catch (IllegalArgumentException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST);

            try (PrintWriter out = response.getWriter()) {
                out.print("""
                        {
                            "error": "%s"
                        }
                        """.formatted(
                        escapeJson(e.getMessage())
                ));
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

    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String path = request.getPathInfo();

            if (path == null || path.equals("/") || path.isEmpty()) {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST);

                out.print("""
                        {
                            "error": "Cart ID is required"
                        }
                        """);

                return;
            }

            Long cartId = Long.parseLong(path.substring(1));

            cartService.removeFromCart(cartId);

            out.print("""
                    {
                        "message": "Item removed from cart"
                    }
                    """);

        } catch (NumberFormatException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST);

            try (PrintWriter out = response.getWriter()) {
                out.print("""
                        {
                            "error": "Invalid cart ID"
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

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}