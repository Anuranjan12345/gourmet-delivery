# Gourmet Delivery

Gourmet Delivery is a web-based food ordering application built with Java Servlets, JDBC, MySQL, HTML, CSS, and JavaScript. It was developed as a learning and portfolio project to practise frontend-to-backend integration and database-driven application workflows.

## Features

- User signup and login
- Restaurant and food menu browsing
- Shopping cart management
- Checkout with a delivery address
- Order placement and order history
- User profile page

> Feature availability reflects the current local project. This project has been tested locally; it is not presented as a production-hosted service.

## Technology Stack

- **Frontend:** HTML, CSS, JavaScript
- **Backend:** Java, Jakarta Servlets
- **Database access:** JDBC
- **Database:** MySQL
- **Build:** Apache Maven
- **Application server:** Apache Tomcat 10.1
- **Packaging:** WAR
- **Java version configured in Maven:** 20

## Application Architecture

The application follows a layered structure:

1. The browser displays HTML and CSS and runs JavaScript.
2. JavaScript sends requests to backend Servlet endpoints.
3. Servlets handle HTTP requests and responses.
4. Service classes implement application operations.
5. Repository classes execute SQL through JDBC.
6. MySQL stores user, restaurant, food, cart, and order data.

See [Project Flow](docs/PROJECT_FLOW.md) for a more detailed overview.

## Prerequisites

Install the following before running the project:

- JDK 20 (or a compatible configured JDK)
- Apache Maven, or use the included Maven Wrapper
- MySQL Server
- Apache Tomcat 10.1
- A code editor such as Visual Studio Code

## Local Setup

1. Clone or download this repository.
2. Create the database in MySQL:

   ```sql
   CREATE DATABASE foodapp;
   ```

3. Select the `foodapp` database and run `database/schema.sql` to create the tables.
4. Run `database/seed-data.sql` to insert the initial restaurant and food catalogue.

   **Warning:** `schema.sql` contains `DROP TABLE IF EXISTS` statements. Use these SQL files only for a fresh database; do not run them against an existing database containing data you want to keep.

5. Configure the environment variables used by `DBConnection.java`:

   - `FOODEXPRESS_DB_URL` — `jdbc:mysql://localhost:3306/foodapp`
   - `FOODEXPRESS_DB_USERNAME` — your MySQL username
   - `FOODEXPRESS_DB_PASSWORD` — your MySQL password

   Do not commit your password or other private credentials.

6. Open a terminal in the project root, where `pom.xml` is located, and build the WAR:

   ```bat
   mvn clean package
   ```

   Alternatively, on Windows:

   ```bat
   mvnw.cmd clean package
   ```

7. Copy `target/foodapp-0.0.1-SNAPSHOT.war` into Tomcat's `webapps` directory. Rename it to `foodapp.war` if you want the application context `/foodapp`.
8. Start Tomcat and open `http://localhost:8080/foodapp/`.

## Testing Checklist

- [ ] Home page loads
- [ ] Signup and login work
- [ ] Food menu and images load
- [ ] Items can be added to and managed in the cart
- [ ] Checkout accepts a delivery address and places an order
- [ ] The order appears in My Orders
- [ ] Profile page loads correctly

## Project Structure

```text
foodapp/
├── pom.xml
├── src/
│   └── main/
│       ├── java/com/foodapp/onlinefoodapp/
│       │   ├── dto/
│       │   ├── model/
│       │   ├── repository/
│       │   ├── service/
│       │   ├── servlet/
│       │   └── util/
│       └── webapp/
│           ├── css/
│           ├── js/
│           └── *.html
└── docs/
```

The exact file and folder contents may evolve as the project is maintained.

## Security Notes

- Do not commit database passwords, private keys, access tokens, or real user data.
- Use prepared statements for SQL queries.
- Keep authentication and user-ownership checks on the server, not only in frontend JavaScript.
- This project is for learning and portfolio demonstration. Review authentication, authorization, session management, transaction handling, validation, and deployment security before using it for real customers.

## Current Status

The project runs locally on Tomcat and its main user flows have been manually tested. The documentation and code may continue to improve as the project is reviewed.

## Learning Goals

This project is intended to demonstrate practice with Java Servlets, JDBC, SQL, frontend integration, application deployment, and debugging.
