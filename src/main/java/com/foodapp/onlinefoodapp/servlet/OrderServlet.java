package com.foodapp.onlinefoodapp.servlet;

import com.foodapp.onlinefoodapp.model.Order;
import com.foodapp.onlinefoodapp.model.OrderItem;
import com.foodapp.onlinefoodapp.service.OrderService;
import com.foodapp.onlinefoodapp.service.impl.OrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/orders/*")
public class OrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderServiceImpl();
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

                List<Order> orders =
                        orderService.getUserOrders(userId);

                writeOrdersJson(out, orders);

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

            String userIdParam =
                    request.getParameter("userId");

            String address =
                    request.getParameter("deliveryAddress");

            if (userIdParam == null || address == null) {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST);

                out.print("""
                        {
                            "error": "userId and deliveryAddress are required"
                        }
                        """);

                return;
            }

            Long userId = Long.parseLong(userIdParam);

            Order order =
                    orderService.placeOrder(userId, address);

            response.setStatus(HttpServletResponse.SC_CREATED);

            writeOrderJson(out, order);

        } catch (NumberFormatException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST);

            try (PrintWriter out = response.getWriter()) {
                out.print("""
                        {
                            "error": "Invalid user ID"
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

    private void writeOrdersJson(PrintWriter out,
                                 List<Order> orders) {

        out.print("[");

        for (int i = 0; i < orders.size(); i++) {

            if (i > 0) {
                out.print(",");
            }

            writeOrderJson(out, orders.get(i));
        }

        out.print("]");
    }

    private void writeOrderJson(PrintWriter out,
                                Order order) {

        out.print("""
                {
                    "id": %d,
                    "userId": %d,
                    "totalAmount": %.2f,
                    "deliveryAddress": "%s",
                    "status": "%s",
                    "createdAt": "%s",
                    "items": [
                """.formatted(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                escapeJson(order.getDeliveryAddress()),
                escapeJson(order.getStatus()),
                order.getCreatedAt()
        ));

        List<OrderItem> items = order.getItems();

        for (int i = 0; i < items.size(); i++) {

            if (i > 0) {
                out.print(",");
            }

            OrderItem item = items.get(i);

            out.print("""
                    {
                        "id": %d,
                        "foodItemId": %d,
                        "name": "%s",
                        "price": %.2f,
                        "quantity": %d
                    }
                    """.formatted(
                    item.getId(),
                    item.getFoodItemId(),
                    escapeJson(item.getName()),
                    item.getPrice(),
                    item.getQuantity()
            ));
        }

        out.print("""
                ]
                }
                """);
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