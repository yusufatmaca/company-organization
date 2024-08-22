-- MySQL dump 10.13  Distrib 8.0.39, for Linux (x86_64)
--
-- Host: localhost    Database: companyorganization
-- ------------------------------------------------------
-- Server version	8.0.39-0ubuntu0.24.04.1

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
INSERT INTO `city` VALUES (1,'2024-08-22 23:03:03.409864','İzmir');
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `address_detail` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `short_name` varchar(255) DEFAULT NULL,
  `company_type_id` int DEFAULT NULL,
  `town_id` int DEFAULT NULL,
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
INSERT INTO `company` VALUES (1,_binary '','Teknopark İzmir A8 Binası','2024-08-22 23:03:19.770522',NULL,'Delta Akıllı Teknolojiler A.Ş.','Delta',1,1);
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_type`
--

DROP TABLE IF EXISTS `company_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
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
INSERT INTO `company_type` VALUES (1,_binary '','2024-08-22 23:02:56.095247',NULL,'Yazılım Geliştirme');
/*!40000 ALTER TABLE `company_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `address_detail` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `company_id` int DEFAULT NULL,
  `department_type_id` int DEFAULT NULL,
  `manager_id` int DEFAULT NULL,
  `town_id` int DEFAULT NULL,
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
INSERT INTO `department` VALUES (1,_binary '','Teknopark İzmir A8 Binası','2024-08-22 23:04:00.058819',NULL,'Genel Müdürlük',1,1,NULL,1),(2,_binary '','Teknopark İzmir A8 Binası','2024-08-22 23:04:07.345883',NULL,'Yazılım Geliştirme',1,2,NULL,1);
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
/*!40000 ALTER TABLE `department_hierarchy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department_type`
--

DROP TABLE IF EXISTS `department_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
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
INSERT INTO `department_type` VALUES (1,_binary '','2024-08-22 23:03:47.021784',NULL,'Yönetsel'),(2,_binary '','2024-08-22 23:03:50.933394',NULL,'Operasyonel');
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
  `created_at` datetime(6) DEFAULT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `verification_code` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_s88xsr4dc97qxxs46aafsxnt6` (`user_id`),
  CONSTRAINT `FK1nn2s9fe9dn1ive1wd0y99utv` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `city_id` int NOT NULL,
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
INSERT INTO `region` VALUES (1,'2024-08-22 23:03:08.256099','İzmir Güney',1);
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
INSERT INTO `roles` VALUES (1,'2024-08-22 22:59:55.906784','ADMIN'),(2,'2024-08-22 22:59:55.954655','MANAGER'),(3,'2024-08-22 22:59:55.968109','EMPLOYEE');
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
  `created_at` datetime(6) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `region_id` int NOT NULL,
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
INSERT INTO `town` VALUES (1,'2024-08-22 23:03:12.879393','Urla',1);
/*!40000 ALTER TABLE `town` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `surname` varchar(255) NOT NULL,
  `department_id` int DEFAULT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FKfi832e3qv89fq376fuh8920y4` (`department_id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKfi832e3qv89fq376fuh8920y4` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,_binary '','2024-08-22 23:02:10.568206',NULL,'yusufatmaca@protonmail.com',_binary '','Yusuf','$2a$10$gypPYrrUrd8FW7xe1./05etEZyBery9myyVgi.K7Du/bJnvpYIItW','Atmaca',NULL,1);
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

-- Dump completed on 2024-08-22 23:04:29
