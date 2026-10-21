-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: foodapp
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `food`
--

LOCK TABLES `food` WRITE;
/*!40000 ALTER TABLE `food` DISABLE KEYS */;
INSERT INTO `food` VALUES (1,'Crispy chicken burger','Zinger Burger',150,1,'images/foods/whopper.png'),(2,'Spicy','Chicken Biryani',180,1,'images/foods/chicken-biryani.jpg'),(3,'Crispy dosa with chutney','Dosa',60,1,'images/foods/dosa.jpg'),(4,'Cheese Pizza','Pizza',189,1,'images/foods/pizza.jpg'),(5,'Spicy, full plate','Chicken Biryani',180,1,'images/foods/chicken-biryani.jpg'),(6,'Tasty burger','Burger',150,1,'images/foods/cheeseburger.png'),(7,'Classic cheese pizza','Margherita Pizza',189,2,'images/foods/margherita.png'),(8,'Pepperoni topped pizza','Pepperoni Pizza',229,2,'images/foods/pepperoni.png'),(9,'Crispy nachos with cheese','Cheese Nachos',129,2,'images/foods/nachos.png'),(10,'Refreshing cold drink','Coke',60,2,'images/foods/coke.png'),(11,'Fresh and healthy salad','Fresh Salad Bowl',149,3,'images/foods/fresh-salad-bowl.jpg'),(12,'Fresh vegetables with dressing','Veggie Salad',129,3,'images/foods/veggie-salad.jpg'),(13,'Fresh tuna with vegetables','Tuna Salad',199,3,'images/foods/tuna-salad.jpg'),(14,'Refreshing cold drink','Coke',60,3,'images/foods/coke.png'),(15,'Grilled steak cooked to perfection','Grilled Steak',399,4,'images/foods/grilled-steak.jpg'),(16,'Juicy steak with pepper sauce','Pepper Steak',449,4,'images/foods/pepper-steak.jpg'),(17,'Crispy chicken burger','Chicken Burger',199,4,'images/foods/cheeseburger.png'),(18,'Refreshing cold drink','Coke',60,4,'images/foods/coke.png'),(19,'Fresh salmon sushi','Salmon Nigiri',299,5,'images/foods/salmon-nigiri.png'),(20,'Spicy tuna sushi','Spicy Tuna',279,5,'images/foods/spicy-tuna.png'),(21,'Fresh and delicious sushi','Sushi Platter',399,5,'images/foods/sushi-platter.jpg'),(22,'Refreshing cold drink','Coke',60,5,'images/foods/coke.png');
/*!40000 ALTER TABLE `food` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `restaurants`
--

LOCK TABLES `restaurants` WRITE;
/*!40000 ALTER TABLE `restaurants` DISABLE KEYS */;
INSERT INTO `restaurants` VALUES (1,'Burger King','Chennai',4.5,'Fast Food','images/restaurant/burger-king (1).png','Chennai'),(2,'Pizza Hut','Chennai',4.4,'Pizza','images/restaurant/pizza-hut.png','Chennai'),(3,'Salad Bar','Chennai',4.3,'Healthy Food','images/restaurant/salad-bar.png','Chennai'),(4,'Steak House','Chennai',4.6,'Steak','images/restaurant/steak-house.png','Chennai'),(5,'Sushi World','Chennai',4.5,'Japanese','images/restaurant/sushi-world.png','Chennai');
/*!40000 ALTER TABLE `restaurants` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-21 13:20:57
