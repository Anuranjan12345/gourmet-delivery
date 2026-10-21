# Project Flow

This document explains the high-level flow of Gourmet Delivery.

## 1. Browser to Backend

1. The user opens a page such as `index.html` or `menu.html`.
2. The browser loads HTML and CSS.
3. JavaScript runs page logic and sends HTTP requests to the application's Servlet endpoints.
4. A Servlet reads the request, validates the input it receives, calls the appropriate service or repository logic, and returns a response.
5. JavaScript reads the response and updates the page.

## 2. Backend Layers

- **Servlets:** Handle HTTP requests and responses.
- **Services:** Coordinate application operations and business rules.
- **Repositories:** Run SQL queries using JDBC.
- **Models and DTOs:** Represent application data and request data.
- **Database utility:** Provides database connections.

The exact path differs by feature. Use the relevant Servlet and service implementation as the source of truth when tracing a request.

## 3. Main User Flows

### Signup and Login

The frontend submits account information to an authentication endpoint. The backend processes the request and interacts with the user repository and database. Password handling should use the project's password utility; never store plain-text passwords.

### Food Menu

The page requests food and restaurant data from backend endpoints. JavaScript displays the returned data and provides controls for adding items to the cart.

### Cart

The user changes quantities or removes items in the cart interface. The frontend sends requests to the relevant cart endpoints, and backend code uses repositories to read or update cart data in MySQL.

### Checkout and Order Placement

The checkout page gathers the delivery address and displays the cart summary. When the user places an order, the backend creates the order and its items and updates the cart. These operations should be reviewed for transaction consistency so partial failures do not leave incomplete data.

### Order History and Profile

The frontend requests data for the relevant user and renders it. The backend must verify user identity and ownership on the server for every user-specific operation.

## 4. How to Trace a Feature

For a feature, follow this order:

1. Find the HTML element or form.
2. Find the JavaScript event handler or API call.
3. Identify the matching Servlet mapping.
4. Follow the service method called by the Servlet.
5. Follow repository methods and SQL queries.
6. Identify the relevant database tables.
7. Trace how the response returns to JavaScript and updates the page.

This document is a high-level guide. Confirm exact endpoint names, SQL, and control flow in the current source code before relying on them in an interview.
