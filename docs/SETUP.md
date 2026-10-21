# Local Development Setup

## Prerequisites

- JDK compatible with the version configured in `pom.xml`
- MySQL Server
- Apache Tomcat 10.1
- Apache Maven
- Visual Studio Code or another Java-capable editor

## Database Setup

The application uses a MySQL database named `foodapp`.

1. Open MySQL Workbench and connect to your local MySQL instance.
2. Create the database if it does not already exist:

   ```sql
   CREATE DATABASE foodapp;
   ```

3. Select the `foodapp` database.
4. Run `database/schema.sql` to create the tables.
5. Run `database/seed-data.sql` to insert the initial restaurant and food catalogue.

The schema and seed files are intended for setting up a fresh database. Review them before importing, and do not run them against your existing working database without understanding their effects.

The seed file contains restaurant and food catalogue data. It does not include existing user accounts, orders, or cart records.

## Database Connection

The application reads its database configuration from environment variables:

- `FOODEXPRESS_DB_URL`
- `FOODEXPRESS_DB_USERNAME`
- `FOODEXPRESS_DB_PASSWORD`

The default JDBC URL is `jdbc:mysql://localhost:3306/foodapp`.

Configure these variables for your own local environment. Never commit actual database credentials to GitHub.

## Build the Application

Open a terminal in the project root, where `pom.xml` is located, and run:

```bat
mvn clean package
```

The generated WAR file will be in the `target/` directory.

## Deploy to Tomcat

1. Stop Tomcat if it is already running.
2. Copy the generated WAR into Tomcat's `webapps` directory.
3. Name it `foodapp.war` if you want the application context path `/foodapp`.
4. Start Tomcat and wait for deployment.
5. Open `http://localhost:8080/foodapp/`.

## Verify the Application

- [ ] Home page loads.
- [ ] Signup and login work.
- [ ] Restaurant and food data appear correctly.
- [ ] Food images load.
- [ ] Cart operations work.
- [ ] Checkout places an order.
- [ ] My Orders displays the order.
- [ ] Profile page loads.
