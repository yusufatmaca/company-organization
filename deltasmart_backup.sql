-- MySQL dump 10.13  Distrib 8.0.39, for Linux (x86_64)
--
-- Host: localhost    Database: deltasmart
-- ------------------------------------------------------
-- Server version	8.0.39-0ubuntu0.24.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` VALUES (1,'2024-08-29 10:10:51.606355','İzmir');
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `active` bit(1) NOT NULL,
  `company_type_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `town_id` int DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `address_detail` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `short_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtle8dllhekyx8lbfycue0eild` (`company_type_id`),
  KEY `FKde0fxr5uwr7nluy48yfu25uu7` (`town_id`),
  CONSTRAINT `FKde0fxr5uwr7nluy48yfu25uu7` FOREIGN KEY (`town_id`) REFERENCES `town` (`id`),
  CONSTRAINT `FKtle8dllhekyx8lbfycue0eild` FOREIGN KEY (`company_type_id`) REFERENCES `company_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (_binary '',1,1,1,'2024-08-29 10:10:51.688944',NULL,'Teknopark İzmir A8 Binası','Delta Akıllı Teknolojiler A.Ş.','Delta');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_type`
--

DROP TABLE IF EXISTS `company_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_type` (
  `active` bit(1) NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_type`
--

LOCK TABLES `company_type` WRITE;
/*!40000 ALTER TABLE `company_type` DISABLE KEYS */;
INSERT INTO `company_type` VALUES (_binary '',1,'2024-08-29 10:10:51.661812',NULL,'Yazılım Geliştirme');
/*!40000 ALTER TABLE `company_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `active` bit(1) NOT NULL,
  `company_id` int DEFAULT NULL,
  `department_type_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `manager_id` int DEFAULT NULL,
  `town_id` int DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `address_detail` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_g9435hkqyjp3h3qsaslcmk4rw` (`manager_id`),
  KEY `FKh1m88q0f7sc0mk76kju4kcn6f` (`company_id`),
  KEY `FKlqp34iubxvsqq0169w82fgcrq` (`department_type_id`),
  KEY `FKag3dst7tffs8pn8og9mv22ugr` (`town_id`),
  CONSTRAINT `FK4b3j4ilxbfdt9fes1junm2cph` FOREIGN KEY (`manager_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKag3dst7tffs8pn8og9mv22ugr` FOREIGN KEY (`town_id`) REFERENCES `town` (`id`),
  CONSTRAINT `FKh1m88q0f7sc0mk76kju4kcn6f` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `FKlqp34iubxvsqq0169w82fgcrq` FOREIGN KEY (`department_type_id`) REFERENCES `department_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (_binary '',1,1,1,3,1,'2024-08-29 10:10:51.725523',NULL,'Teknopark İzmir A8 Binası','Genel Müdürlük'),(_binary '',1,2,2,2,1,'2024-08-29 10:10:51.734383',NULL,'Teknopark İzmir A8 Binası','Yazılım Geliştirme');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department_hierarchy`
--

DROP TABLE IF EXISTS `department_hierarchy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department_hierarchy` (
  `child_department_id` int NOT NULL,
  `parent_department_id` int NOT NULL,
  PRIMARY KEY (`child_department_id`,`parent_department_id`),
  KEY `FKdh7hymbt37uh845eccjbedbox` (`parent_department_id`),
  CONSTRAINT `FKdh7hymbt37uh845eccjbedbox` FOREIGN KEY (`parent_department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `FKhygbaivi7opp3b97o5rf0glk0` FOREIGN KEY (`child_department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department_hierarchy`
--

LOCK TABLES `department_hierarchy` WRITE;
/*!40000 ALTER TABLE `department_hierarchy` DISABLE KEYS */;
INSERT INTO `department_hierarchy` VALUES (2,1);
/*!40000 ALTER TABLE `department_hierarchy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department_type`
--

DROP TABLE IF EXISTS `department_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department_type` (
  `active` bit(1) NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department_type`
--

LOCK TABLES `department_type` WRITE;
/*!40000 ALTER TABLE `department_type` DISABLE KEYS */;
INSERT INTO `department_type` VALUES (_binary '',1,'2024-08-29 10:10:51.702547',NULL,'Yönetsel'),(_binary '',2,'2024-08-29 10:10:51.713080',NULL,'Operasyonel');
/*!40000 ALTER TABLE `department_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_confirmation_token`
--

DROP TABLE IF EXISTS `email_confirmation_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_confirmation_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `verification_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_s88xsr4dc97qxxs46aafsxnt6` (`user_id`),
  CONSTRAINT `FK1nn2s9fe9dn1ive1wd0y99utv` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_confirmation_token`
--

LOCK TABLES `email_confirmation_token` WRITE;
/*!40000 ALTER TABLE `email_confirmation_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_confirmation_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `region` (
  `city_id` int NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKedafr6k537xtdes7gc7nq778a` (`city_id`),
  CONSTRAINT `FKedafr6k537xtdes7gc7nq778a` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region`
--

LOCK TABLES `region` WRITE;
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
INSERT INTO `region` VALUES (1,1,'2024-08-29 10:10:51.639765','İzmir Güney');
/*!40000 ALTER TABLE `region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `role_name` enum('ADMIN','EMPLOYEE','MANAGER') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'2024-08-29 10:10:51.812716','ADMIN'),(2,'2024-08-29 10:10:51.823193','MANAGER'),(3,'2024-08-29 10:10:51.831774','EMPLOYEE');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `town`
--

DROP TABLE IF EXISTS `town`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `town` (
  `id` int NOT NULL AUTO_INCREMENT,
  `region_id` int NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqsj4ud2o2yp0i2fkajq1rbsax` (`region_id`),
  CONSTRAINT `FKqsj4ud2o2yp0i2fkajq1rbsax` FOREIGN KEY (`region_id`) REFERENCES `region` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `town`
--

LOCK TABLES `town` WRITE;
/*!40000 ALTER TABLE `town` DISABLE KEYS */;
INSERT INTO `town` VALUES (1,1,'2024-08-29 10:10:51.651167','Urla');
/*!40000 ALTER TABLE `town` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `active` bit(1) NOT NULL,
  `department_id` int DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `surname` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FKfi832e3qv89fq376fuh8920y4` (`department_id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKfi832e3qv89fq376fuh8920y4` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (_binary '',1,_binary '',1,1,'2024-08-29 10:10:51.942068',NULL,'admin@delta.smart','System','$2a$10$UR39yvC4cUJktxiUqBHsIOxBV.JpWcz66LLCqnlVbwEd6O01u3Pne','Administrator'),(_binary '',2,_binary '',2,2,'2024-08-29 10:11:38.223670',NULL,'33yusufatmaca@gmail.com','Yusuf','$2a$10$tfZpDHL6xANaz3.MpSQ9CeTZv7OBYlTSZs.a7y5kZ6oymBIXmlGOC','Atmaca'),(_binary '',1,_binary '',3,2,'2024-08-29 10:11:49.020658',NULL,'yusufatmaca@protonmail.com','Yusuf','$2a$10$prmbu2yMTws5rxVVT8jrQOVtZjqn3xdkaFFYDYgr6prbVDmlQGMg6','Atmaca'),(_binary '',2,_binary '',4,3,'2024-08-29 10:12:16.117850',NULL,'yusufatmaca@std.iyte.edu.tr','Yusuf','$2a$10$c8VYZj/up0A0R60vOruLTuOGIxpeRtOFeyPsNmjcf/bZNXosbcB7W','Atmaca');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-29 10:19:45
