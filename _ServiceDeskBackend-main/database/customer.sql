-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: servicedesk
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `account_id` binary(16) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `company_id` binary(16) NOT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `UK_pg8nkk2jq7i0q6rq8wlp8y3t3` (`company_id`),
  UNIQUE KEY `UK_gex1lmaqpg0ir5g1f5eftyaa1` (`username`),
  CONSTRAINT `FKb5lnqegbx3h1rt2r06x6k1m7q` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (_binary 'Göl½³`G×¤û\Ïl¢¥;ğ','Your Password','Your Password',_binary 'f/\"¦ŠC®¤\ë~««Á~µ'),(_binary '\Ä …×±Cº‚EÔ—|)\á ','$2a$10$AKeiI5i2v/2rnhVlSsTeoOIFlDH7qMUhDyeW4EnN4bUVCGL05IugO','mukwevho',_binary 'HB–\Ç±I‹PZ\Ì('),(_binary '\ç‚`\ï»2A$† ¦\nŒsG','$2a$10$6QPyUcKbW.tImFikFyC87eoOAdYdrDeVV3t/yiWVmn06WwmsbaV3W','mukwevho18@gmail.com',_binary 'úlŠõ\İBBµt9\ç[');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrator` (
  `admin_id` binary(16) NOT NULL,
  `contact_number` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `position` varchar(255) NOT NULL,
  `company_id` binary(16) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `UK_pv9djb9ai0tu4017vkbtalp2l` (`company_id`),
  UNIQUE KEY `UK_jj3mmcc0vjobqibj67dvuwihk` (`email`),
  CONSTRAINT `FK8vp7eroneccpqsfnv552op043` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (_binary 'ş¡²&\İJ¾½‹\íƒ\Ü\áp«','0635405592','mukwevhotk18@gmail.com','Takalani','Mukwevho','$2a$10$WMY9SRwnepO7bDy7AuZay.QZO.yD5HNrxXrXpjWVyPyNkQlApneTi','Developer',_binary 'úlŠõ\İBBµt9\ç[');
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `company_id` binary(16) NOT NULL,
  `company_email` varchar(255) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `company_number` varchar(255) NOT NULL,
  PRIMARY KEY (`company_id`),
  UNIQUE KEY `UK_lcx5d9e7e14th6ytu3fys5pet` (`company_email`),
  UNIQUE KEY `UK_46jubpbtfae2gfb74a3x6qug7` (`company_name`),
  UNIQUE KEY `UK_1ach3gt5gf0wvom9ebjmcccri` (`company_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (_binary 'HB–\Ç±I‹PZ\Ì(','mukwevhotk18@gmail.com','Tech Innovation','012545234324'),(_binary 'f/\"¦ŠC®¤\ë~««Á~µ','Your Company Email','Your Company Name','Your Contact Number'),(_binary 'úlŠõ\İBBµt9\ç[','mukwevho18@gmail.com','Techie Innovation','01254523432');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` binary(16) NOT NULL,
  `content` mediumtext,
  `receiver` binary(16) DEFAULT NULL,
  `sender` binary(16) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `ticket_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (_binary '¼\"1œ\×N±—ˆ¤\Ü&¨Á','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:41:11.820440',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '	\'£±OE/XB+û\ì','study ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 18:27:06.095329',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '\n\"·z¢\àKg¨Y\Ï\0','Bathroom ',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-08 20:02:13.959419',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '\Ò=\èKT½1a`Y\é\'','',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:37:52.527240',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary ':ZE.F˜®1­œ6k','study ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 18:46:19.773213',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '¤K2Ÿ\ÊM„0|D\Û','hi',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-22 11:46:18.046638',_binary 'ø\É@¢\n\ËOñ…\Ù\Ä,}{ú'),(_binary ' M`¶Iò}¢\ÃSTõ\İ','',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:39:50.480616',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary ' H.¿ şJNªj÷\rI\ÕÈ”','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:59:00.002955',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '$Î¸Í‰L\ê®\\W|´¯GW','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:52:33.923634',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '3¡>\Ã)J€ˆØ¨Kr§x','Kitchen zinc',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 20:05:30.672990',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '4¦¶óW¨L“˜2½\ì\Ç@m\É','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:54:51.299028',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary ':\Ô\á\à­A»´>,\Ğ\ÛV','Kitchen zinc',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:59:45.155605',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '<K${ôK¸‘\Ä#\Å+\î','study ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:52:11.665120',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '<_\ásŸJ­£”\ÕÒ¬\Â@','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:54:40.840885',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '>Ó•v\Ö\"@cË¿ıg\ï¦','Kitchen zinc',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:49:37.608582',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'K\ŞP¿\âG?´úœñò\ã ','ta',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-08 18:46:52.320464',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'UW×§\ÄAÊ²~™\È9g;','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-22 11:46:25.231756',_binary 'ø\É@¢\n\ËOñ…\Ù\Ä,}{ú'),(_binary 'WXË¯+\åAT˜§{c\'','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:55:44.037581',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'YT±’%\ÚC«Ÿ\ã\Ş:±œó!','Takalani',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 18:20:32.330470',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'Zm\Î\î %OÏ‹\ã{²½Š','Bathroom ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 18:52:37.039092',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '_\í»JğWOŠ;ƒ_fS','Kitchen zinc',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:44:42.216616',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '`»tQ±VB1™¹ñk´_\Ğ','Bathroom ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 18:30:38.054745',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'bJa<¼EJœ»µ¡Å¼Œ','Bathroom  combo',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-08 20:06:38.729477',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'g`\îzeD3™ŒÓFw','study ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:51:49.037452',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'l6\Ìóz\áL‚–¸>‰¤¦','Bathroom ',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-22 11:46:31.776760',_binary 'ø\É@¢\n\ËOñ…\Ù\Ä,}{ú'),(_binary 'n;X\Ö\ã\'H‡±\Ş^x\×I=³','Bathroom  combo',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-08 19:56:24.597292',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'pL)ÌœJÂ²\Ü\Ä[MªX\Ô','Shower ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:50:21.815732',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'p\êa0!D\á²HÔ–\åBb','Shower ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:55:07.100208',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'søÌ°/\İI\"›¸yQŠ','Bathroom ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-09 09:14:04.609928',_binary '%\Ã}§=hBÅˆıÁEu¶½‘'),(_binary '|¯ûgE«Gz ]\È\Ş','Kitchen zinc',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:55:02.260282',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'ˆˆµ‰²DDh¥t“Â‘\Ã','Kitchen zinc',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:54:45.986631',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '\Ìtj\ÔN©°ù­¨ı‰¶™','sho',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 18:47:10.954345',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '‘Ğ½¨kN\n\è,©€B\Ç','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:44:45.436602',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '\ÅwSMÚ½\r{vYn\ÖH','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 20:06:19.107723',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'ŒJF£LEƒ‹\àN„5h','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:55:14.146265',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'Ÿv3¸AKT—le\Ñ[¤“','study ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:49:41.548236',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '«	Y,\å@DŞ¿Ä¤\ß… ','Bathroom  combo',NULL,_binary 'ƒoo%\Ç@Ÿ¶@\Ä2\İE','2023-06-23 12:07:40.125868',_binary '¨¥µJúA¶Zl  ©L'),(_binary 'µ\Ü/”L—@&±GUØ{}','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 20:01:58.702325',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '¹\Z! \â$H»’ü(†v!','Kitchen zinc',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-08 19:42:01.941715',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '»u#Ş¾\áIB—21G£;\n','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:52:41.367444',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '½2‚LEK½÷«/%eW\È','Kitchen zinc',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-09 09:14:24.029302',_binary '%\Ã}§=hBÅˆıÁEu¶½‘'),(_binary 'Å„\î^=O/—S¿¯™D\Ë\Z','study ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 18:30:18.941526',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '\Ñk9$¿@Ø‰\Ã×…õòüŒ','Ndaa',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 18:23:30.434121',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '\Ö>®¹‹\ÊJh‰Š\É#¢ù°','add',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:50:17.399552',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'Ö¯%‚À\à@y…$\Z`\ÃrG','study ',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-08 20:01:09.925119',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'Ú‰0ö\ÉIr²­y/WB{','Haaaaa go to hell',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-08 18:27:38.210466',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '\ã\Íıü‰ø@(‘N\ÏÁ\Í\Ï*1','Bathroom  combo',NULL,_binary '\×şòD:¾²\åúˆbY','2023-06-08 19:59:58.333901',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '\å›Ó@†	œ\ç´jš','Shower ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:51:43.755991',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '\å< \á\ÒWM[§@)CA¢P','Bathroom  combo',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:58:48.913566',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary '\ìı“Á\êM)€B>®\nóH','Shower ',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-08 19:55:50.716745',_binary 'ñi\Ú.TAK\ÖôvÌ¸'),(_binary 'ğ\Çk½õšKú¬\ìmõ\ÙRş7','hi',NULL,_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','2023-06-22 11:45:26.165959',_binary 'ø\É@¢\n\ËOñ…\Ù\Ä,}{ú');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `ms_id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `ticket_id` bigint DEFAULT NULL,
  PRIMARY KEY (`ms_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (1,'dfdfdf','Tk',1321312312),(2,'sss','Tk',1321312312),(3,'dasssssssssssadadasddasdasd','Tk',1321312312),(4,'ccccc','Tk',1321312312),(5,'takalani','Tk',1321312312),(6,'cccc','Tk',1321312312),(7,'sdfdf','Tk',1321312312),(8,'fsdf','Tk',1321312312),(9,'Takalani','Tk',1321312312),(10,'takaalanda','Thu Jun 08 2023 13:56:57 GMT+0200 (South Africa Standard Time)',1321312312),(11,'Ndaa','Thu Jun 08 2023 13:57:24 GMT+0200 (South Africa Standard Time)',1321312312),(12,'Ndaa','Thu Jun 08 2023 13:57:24 GMT+0200 (South Africa Standard Time)',1321312312),(13,'das','Thu Jun 08 2023 14:29:02 GMT+0200 (South Africa Standard Time)',1321312312),(14,'Takalani','Thu Jun 08 2023 14:35:17 GMT+0200 (South Africa Standard Time)',1321312312),(15,'hjhjh','Thu Jun 08 2023 14:37:31 GMT+0200 (South Africa Standard Time)',1321312312),(16,'jjj','Thu Jun 08 2023 14:40:44 GMT+0200 (South Africa Standard Time)',1321312312),(17,'dggfd','Thu Jun 08 2023 14:43:08 GMT+0200 (South Africa Standard Time)',1321312312),(18,'das','Thu Jun 08 2023 14:56:31 GMT+0200 (South Africa Standard Time)',1321312312),(19,'Takalani Munwevho','Thu Jun 08 2023 14:56:31 GMT+0200 (South Africa Standard Time)',1321312312),(20,'41','Thu Jun 08 2023 15:00:48 GMT+0200 (South Africa Standard Time)',1321312312),(21,'ffff','Thu Jun 08 2023 15:22:01 GMT+0200 (South Africa Standard Time)',1321312312),(22,'Takalani','Thu Jun 08 2023 15:22:52 GMT+0200 (South Africa Standard Time)',1321312312),(23,'20','Thu Jun 08 2023 15:22:52 GMT+0200 (South Africa Standard Time)',1321312312),(24,'10','Thu Jun 08 2023 15:22:01 GMT+0200 (South Africa Standard Time)',1321312312),(25,'ass','Thu Jun 08 2023 15:30:32 GMT+0200 (South Africa Standard Time)',1321312312),(26,'asd','TK',1321312312),(27,'d','TK',1321312312),(28,'sdasdsd','TK',1321312312),(29,'asdadasdasd','TK',1321312312),(30,'Taklani','TK',1321312312),(31,'asd','TK',1321312312),(32,'asdas','TK',1321312312),(33,'asdsad','TK',1321312312);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `physical_address`
--

DROP TABLE IF EXISTS `physical_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `physical_address` (
  `postal_address_id` binary(16) NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `postal_or_zipcode` varchar(255) NOT NULL,
  `state_or_province` varchar(255) NOT NULL,
  `street_address` varchar(255) NOT NULL,
  `unit_or_apartment_number` varchar(255) DEFAULT NULL,
  `company_id` binary(16) NOT NULL,
  PRIMARY KEY (`postal_address_id`),
  UNIQUE KEY `UK_11ck8afgybm0snofmolmffaa` (`company_id`),
  CONSTRAINT `FK5qvatqadehx4i434qd7umrdpi` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `physical_address`
--

LOCK TABLES `physical_address` WRITE;
/*!40000 ALTER TABLE `physical_address` DISABLE KEYS */;
INSERT INTO `physical_address` VALUES (_binary 'oµ\í\Ú[Hw¡Wm\é\Ìq\ë','Pretoria','South Africa','0002','Gauteng','454 Spuy','117',_binary 'úlŠõ\İBBµt9\ç['),(_binary 'Òˆô\ÙüEğ‡5jŞY¤','Your City','Your Country','12345','Your State','456 Broadway','Unit 2',_binary 'f/\"¦ŠC®¤\ë~««Á~µ'),(_binary '\í6\æÙ@€“X­´¢','Pretoria','South Africa','0002','Gauteng','454 Spuy','117',_binary 'HB–\Ç±I‹PZ\Ì(');
/*!40000 ALTER TABLE `physical_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `postal_address`
--

DROP TABLE IF EXISTS `postal_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `postal_address` (
  `postal_address_id` binary(16) NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `postal_or_zipcode` varchar(255) NOT NULL,
  `state_or_province` varchar(255) NOT NULL,
  `street_address` varchar(255) NOT NULL,
  `unit_or_apartment_number` varchar(255) DEFAULT NULL,
  `company_id` binary(16) NOT NULL,
  PRIMARY KEY (`postal_address_id`),
  UNIQUE KEY `UK_o8j7jjj4nqoppe5262uxkp7bb` (`company_id`),
  CONSTRAINT `FKmq37d47i4axi0n5a268pg49um` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `postal_address`
--

LOCK TABLES `postal_address` WRITE;
/*!40000 ALTER TABLE `postal_address` DISABLE KEYS */;
INSERT INTO `postal_address` VALUES (_binary 'G¿©C\Ø\ÙB›IªÁ2¨p','Pretoria','South Africa','0002','Gauteng','454 Spuy','117',_binary 'HB–\Ç±I‹PZ\Ì('),(_binary 'À0‰F»^J½!‡\é<U','Your City','Your Country','12345','Your State','123 Main St','Unit 1',_binary 'f/\"¦ŠC®¤\ë~««Á~µ'),(_binary '\ÍY.«aF‹3%-*\Ò\Z¦','Pretoria','South Africa','0002','Gauteng','454 Spuy','117',_binary 'úlŠõ\İBBµt9\ç[');
/*!40000 ALTER TABLE `postal_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscriptions` (
  `subscription_id` binary(16) NOT NULL,
  `company_id` binary(16) NOT NULL,
  `end_date` datetime(6) NOT NULL,
  `package_name` varchar(255) NOT NULL,
  `start_date` datetime(6) NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`subscription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriptions`
--

LOCK TABLES `subscriptions` WRITE;
/*!40000 ALTER TABLE `subscriptions` DISABLE KEYS */;
INSERT INTO `subscriptions` VALUES (_binary '\ã¹w,G.»zh!»}²',_binary 'úlŠõ\İBBµt9\ç[','2023-12-18 10:38:44.112000','Basic','2023-05-18 10:38:44.115000','Expired');
/*!40000 ALTER TABLE `subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket` (
  `ticket_id` binary(16) NOT NULL,
  `category` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `assigned_to` binary(16) NOT NULL,
  `requested_by` binary(16) NOT NULL,
  `description` varchar(255) NOT NULL,
  `priority` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `company_id` binary(16) NOT NULL,
  PRIMARY KEY (`ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES (_binary 'ñi\Ú.TAK\ÖôvÌ¸','Hardware','2023-05-31 12:56:34.751000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','urgent','High','Close',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '#d‡öµ\áJt±^YŸó2\r','Takalani Maxwell','2023-06-14 10:09:16.818000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','come tommowr','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '%\Ã}§=hBÅˆıÁEu¶½‘','das','2023-05-31 13:01:42.308000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Zwiko','Low','Close',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '([e¹nIù€ki\Ï|\Ä-q','My laptop is low','2023-06-14 10:01:00.754000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Description','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '+¡ö¬/K±¢%\àA\æV½','Takalani Maxwell','2023-06-20 09:13:24.290000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Come fast','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '3~›ñBB©P\Ê{øk÷','Hardware','2023-06-22 14:25:11.232000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Beef','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '3‡lHóD¬ƒ\×_õ†}&','Hardware','2023-06-22 14:23:56.531000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Hardware','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary ':P\Â\Êÿ¨Mü§ K›©º¾¬','Takalani Maxwell','2023-06-14 10:15:57.735000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','come','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'A–\ér¬I»˜\ã\Òı«‰1','das','2023-05-31 13:01:42.302000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Zwiko','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'K1\"ªµM‹kı%\Ûb\ÜL','das','2023-05-31 13:01:42.309000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Zwiko','Low','Escalate',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'Q.7ØˆI\0†Ç¬\ì[ò','My laptop is low','2023-06-20 11:09:21.465000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','adsssssssssssssssssssssssssssssssssssssssssssssss wsadasd','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'U\æ\Úyµ!Eœ¦ûfR›G','','2023-06-22 10:07:54.525000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'YV·\ĞC	‰\æi£†j¶','My laptop is low','2023-06-13 16:09:57.858000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','My laptop is slow when i open programs','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary ']º\nŒ\×Jxµµ#\å;\ékŠ','das','2023-05-31 13:01:42.309000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Zwiko','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '_K„\Ú\ÈGó:\ì=\ë7\è\Ø','Takalani Maxwell','2023-06-20 09:12:47.813000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Hello','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'iôF\ÃK¸’2¶‰G‡,','Hardware','2023-05-31 12:56:34.752000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','urgent','High','Closed',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'rÉ½şû@™µƒ\İ{Oh™','Takalani Maxwell','2023-06-20 11:09:02.673000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Pool','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '„¢ªvsI\ë­C¥…\Ï\æ’\Õ','Hardware','2023-05-31 12:56:34.751000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','urgent','High','Closed',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '\È\ìmChŒ,<†»ø•H','Software ','2023-06-14 09:30:09.281000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Laptop freezing, it seems to be slow','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '•°Ğ¡u¨HŞ¶º7‡ J•','Hardware','2023-06-20 09:07:21.480000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Takalani','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '£ü\è¾\ÃJ–\Û\ã=†]º','Hardware','2023-06-20 09:15:31.957000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','it\'s urgent','High','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '¨¥µJúA¶Zl  ©L','Hardware','2023-06-23 09:02:48.713000',_binary '\ëvŠØF*Š}f/\ì\Ød',_binary 'ƒoo%\Ç@Ÿ¶@\Ä2\İE','My laptop is not working propers','Medium','Open',NULL,_binary '¿3u\Ù<\àF\ç³!@h:\ÚZ'),(_binary '¬úÿ)<K±¹P¨N;\Ò\ß','Hardware','2023-06-08 20:51:57.716000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','dsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa','Low','Closed',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '¯\'´û\\ƒFœ»…{Z\Ø\å†','','2023-06-22 12:27:17.406000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '³}LO~\çO•’²\Øs…„','das','2023-05-31 13:01:42.309000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Zwiko','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '³\ŞO*\ÏrCµ§Ÿ¢ú¯\Õ;','Takalani Maxwell','2023-06-14 10:03:18.908000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Mukwevho i\'m not able to come to word','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '¸\Æi”¥OWŠ6ğˆ;Áş','Hardware','2023-05-31 12:55:31.787000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','emergency','High','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'Â¬\ru\ÄE¡°O\"$<²°k','Hardware','2023-06-20 11:03:00.098000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','E adsas ss','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '\È\àÔ©¨FB´	—/yÉ¾','das','2023-05-31 13:01:42.310000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Zwiko','Low','Closed',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '\Ìû\ß*ı”D@¢´½Í°\Ó_','das','2023-05-31 13:01:42.310000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Zwiko','Low','Closed',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'Í½C\nUC\á²N\ãfs…¾','Hardware','2023-06-20 11:08:47.830000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','asddssdasadasdasd','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '\Õ\à\ë\Æk#EQ½”Z‚ù’û\Å','Takalani Maxwell','2023-06-21 15:14:07.682000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Bak online','Low','Closed',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '\Ùd®M“I`¾ø\Èÿ©Sy','Hardware','2023-05-31 12:55:31.786000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','emergency','High','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '\Û@\ÙÅ¶ŸAÌ³J‡\Ù}\åb','Takalani Maxwell','2023-06-14 10:07:53.525000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','I\'m comming now now','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '\İğ3@	N\\±³=','Hardware','2023-06-05 09:48:38.225000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','asdfghjkljkhjgfhddfgfghfghgfftyetrdff','Low','Closed',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '\â>Œ mHG–Mº¶1±c\Ü','Hardware','2023-06-20 11:04:02.573000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Gamabana','Low','Closed',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary '\élˆ\â¡[K8Ë…N\ÖÄƒ','Hardware','2023-06-14 10:07:15.989000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Hardware mouse what wahta','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'ôaK\ÄnB4®‡\ê„|\Ã','Hardware','2023-05-31 12:54:50.510000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','Urgent','Low','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'ø\É@¢\n\ËOñ…\Ù\Ä,}{ú','Hardware','2023-06-22 11:44:32.546000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','my touchpad is not working','Medium','Open',NULL,_binary 'úlŠõ\İBBµt9\ç['),(_binary 'ıQ–§9·Lü¯G;\Ğ\Ûtñ\Û','Hardware','2023-05-31 12:48:30.002000',_binary '\×şòD:¾²\åúˆbY',_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï','I can\'t work my mouse is broken','Medium','Open',NULL,_binary 'úlŠõ\İBBµt9\ç[');
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_escalated`
--

DROP TABLE IF EXISTS `ticket_escalated`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket_escalated` (
  `ticket_escalation_id` binary(16) NOT NULL,
  `escalated_to` binary(16) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `ticket_id` binary(16) NOT NULL,
  PRIMARY KEY (`ticket_escalation_id`),
  UNIQUE KEY `UK_pn8d7qrba5eaijxqwli7oc0n2` (`ticket_id`),
  CONSTRAINT `FKilv5dkeb7eyxy8uwqbnhg0gir` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_escalated`
--

LOCK TABLES `ticket_escalated` WRITE;
/*!40000 ALTER TABLE `ticket_escalated` DISABLE KEYS */;
INSERT INTO `ticket_escalated` VALUES (_binary '`r\ŞÃ’G\0¡®\×»’(',_binary '°3¡¥@\"\Ş*Z\Ò\ì0<','The ticket is what what',_binary 'K1\"ªµM‹kı%\Ûb\ÜL');
/*!40000 ALTER TABLE `ticket_escalated` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_resolution`
--

DROP TABLE IF EXISTS `ticket_resolution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket_resolution` (
  `ticket_resolution_id` binary(16) NOT NULL,
  `resolution` varchar(255) DEFAULT NULL,
  `ticket_id` binary(16) NOT NULL,
  PRIMARY KEY (`ticket_resolution_id`),
  UNIQUE KEY `UK_deo9q9f6tv2ichkv92an0881w` (`ticket_id`),
  CONSTRAINT `FK9lgf5k8nypqktdcxnjybbavx4` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_resolution`
--

LOCK TABLES `ticket_resolution` WRITE;
/*!40000 ALTER TABLE `ticket_resolution` DISABLE KEYS */;
INSERT INTO `ticket_resolution` VALUES (_binary '±Ÿœ8\ÓMï¾«µ\ËbŸk','ddasdasda',_binary '\Ìû\ß*ı”D@¢´½Í°\Ó_'),(_binary 'G‘¬C[Cêº²“¶¿bIñ','dadadda',_binary '\İğ3@	N\\±³='),(_binary '@ô±·„¿Nƒ€)K\ï\É·','afafafafaf',_binary '\â>Œ mHG–Mº¶1±c\Ü'),(_binary 'Ks-®¤¯NÁ¹u½N\İ\Ûfø','F',_binary '\Õ\à\ë\Æk#EQ½”Z‚ù’û\Å'),(_binary 'Z\ÇA4\éF$€³mÖšö“','It was small issue',_binary '¬úÿ)<K±¹P¨N;\Ò\ß'),(_binary '•\Ğy!K÷tùÀ\è¢TU','',_binary 'iôF\ÃK¸’2¶‰G‡,'),(_binary '³¼\ßë­O¾¿\0Mdğ3õ','adssssssssssss',_binary '\È\àÔ©¨FB´	—/yÉ¾'),(_binary 'ºO#ƒù\çB=•Õ¯E®D','dadad',_binary '„¢ªvsI\ë­C¥…\Ï\æ’\Õ'),(_binary '\â[5‹’…MÉ?\ì\'P','asddddddd',_binary 'ñi\Ú.TAK\ÖôvÌ¸');
/*!40000 ALTER TABLE `ticket_resolution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'servicedesk'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-23 12:48:58
-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: customer-service
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `account_id` binary(16) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `company_id` binary(16) NOT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `UK_pg8nkk2jq7i0q6rq8wlp8y3t3` (`company_id`),
  UNIQUE KEY `UK_gex1lmaqpg0ir5g1f5eftyaa1` (`username`),
  CONSTRAINT `FKb5lnqegbx3h1rt2r06x6k1m7q` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (_binary '­—\É\î\rxJ¦ß©Dsóğ','$2a$10$eL2Ygob6/cMG8NmtFq/Fmu9A9fymP1C.G/GaSxwDcqazeQvOV0ZSi','218206751@tut4life.ac.za',_binary '¿3u\Ù<\àF\ç³!@h:\ÚZ');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrator` (
  `admin_id` binary(16) NOT NULL,
  `contact_number` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `position` varchar(255) NOT NULL,
  `company_id` binary(16) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `UK_pv9djb9ai0tu4017vkbtalp2l` (`company_id`),
  UNIQUE KEY `UK_jj3mmcc0vjobqibj67dvuwihk` (`email`),
  CONSTRAINT `FK8vp7eroneccpqsfnv552op043` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (_binary '¾üzP1\'AÂœoœWg\Â','0635405592','matodzi@gmail.com','Takalani','Mukwevho','$2a$10$UZ0E2RWi9xE1sgmB6Ed0q.F1B.zRA4tIInXsSEYKuZdMxqcfdUzaS','IT',_binary '¿3u\Ù<\àF\ç³!@h:\ÚZ');
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agent_account`
--

DROP TABLE IF EXISTS `agent_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agent_account` (
  `account_id` binary(16) NOT NULL,
  `company_id` binary(16) NOT NULL,
  `contact_number` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `is_expired` bit(1) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `position` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `is_on_leave` bit(1) NOT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `UK_a3e3s32qhohafdoewjfi7sxfh` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent_account`
--

LOCK TABLES `agent_account` WRITE;
/*!40000 ALTER TABLE `agent_account` DISABLE KEYS */;
INSERT INTO `agent_account` VALUES (_binary '\×şòD:¾²\åúˆbY',_binary 'úlŠõ\İBBµt9\ç[','012321321','mukwevhotk18@gmail.com','Mukwevho',_binary '\0','Mukwevho','$2a$10$dcDMka2OH4Mng4OJ4gs/euPk1hn9kCMScHDGGTsejHtnPdmBX1oiW','Technician','Offline',_binary '\0'),(_binary '1ø¬XOÙ¦ˆª0\ä%\Ñ',_binary 'f/\"¦ŠC®¤\ë~««Á~µ','0635405592','maxwell@gmail.com','Takalani',_binary '\0','Mukwevho','90de7a88','Technician','Offline',_binary '\0'),(_binary '°3¡¥@\"\Ş*Z\Ò\ì0<',_binary 'úlŠõ\İBBµt9\ç[','0635405592','mukwe18@gmail.com','Takalani',_binary '\0','Mukwevho','$2a$10$SCn7.2F762P4TInn0svGeekHaLg07D.GnGN4ASPEXmlRQFBCoc502','Developer','Offline',_binary '\0'),(_binary '\ëvŠØF*Š}f/\ì\Ød',_binary '¿3u\Ù<\àF\ç³!@h:\ÚZ','0635405592','agent@gmail.com','Takalani',_binary '\0','Mukwevho','$2a$10$St3/BkMeFWVe6x7LmE5Y8eXCWrQ78q0N0S/Eh6sltabtQ/olEUqom','Developer','Offline',_binary '\0');
/*!40000 ALTER TABLE `agent_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `company_id` binary(16) NOT NULL,
  `company_email` varchar(255) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `company_number` varchar(255) NOT NULL,
  PRIMARY KEY (`company_id`),
  UNIQUE KEY `UK_lcx5d9e7e14th6ytu3fys5pet` (`company_email`),
  UNIQUE KEY `UK_46jubpbtfae2gfb74a3x6qug7` (`company_name`),
  UNIQUE KEY `UK_1ach3gt5gf0wvom9ebjmcccri` (`company_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (_binary '¿3u\Ù<\àF\ç³!@h:\ÚZ','218206751@tut4life.ac.za','VhaKay','0635405592');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_account`
--

DROP TABLE IF EXISTS `customer_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_account` (
  `account_id` binary(16) NOT NULL,
  `company_id` varchar(255) NOT NULL,
  `contact_number` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `position` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `UK_nlt5u7iqr3skug5u5ppkhxa3i` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_account`
--

LOCK TABLES `customer_account` WRITE;
/*!40000 ALTER TABLE `customer_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `physical_address`
--

DROP TABLE IF EXISTS `physical_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `physical_address` (
  `postal_address_id` binary(16) NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `postal_or_zipcode` varchar(255) NOT NULL,
  `state_or_province` varchar(255) NOT NULL,
  `street_address` varchar(255) NOT NULL,
  `unit_or_apartment_number` varchar(255) DEFAULT NULL,
  `company_id` binary(16) NOT NULL,
  PRIMARY KEY (`postal_address_id`),
  UNIQUE KEY `UK_11ck8afgybm0snofmolmffaa` (`company_id`),
  CONSTRAINT `FK5qvatqadehx4i434qd7umrdpi` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `physical_address`
--

LOCK TABLES `physical_address` WRITE;
/*!40000 ALTER TABLE `physical_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `physical_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `postal_address`
--

DROP TABLE IF EXISTS `postal_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `postal_address` (
  `postal_address_id` binary(16) NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `postal_or_zipcode` varchar(255) NOT NULL,
  `state_or_province` varchar(255) NOT NULL,
  `street_address` varchar(255) NOT NULL,
  `unit_or_apartment_number` varchar(255) DEFAULT NULL,
  `company_id` binary(16) NOT NULL,
  PRIMARY KEY (`postal_address_id`),
  UNIQUE KEY `UK_o8j7jjj4nqoppe5262uxkp7bb` (`company_id`),
  CONSTRAINT `FKmq37d47i4axi0n5a268pg49um` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `postal_address`
--

LOCK TABLES `postal_address` WRITE;
/*!40000 ALTER TABLE `postal_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `postal_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_account` (
  `account_id` binary(16) NOT NULL,
  `company_id` binary(16) NOT NULL,
  `contact_number` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `is_expired` bit(1) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `position` varchar(255) NOT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `UK_hl02wv5hym99ys465woijmfib` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES (_binary '-}‡–n	K\í‰ó’x\Ç*',_binary 'úlŠõ\İBBµt9\ç[','0635405592','mukwevhotk18@gmail.com','Takalani',_binary '\0','Mukwevho','$2a$10$eW.RdJCs05n53mAJ9JXF4.mCJDgcMn6IeGDLKMImhMYx/vSAZnwM2','IT'),(_binary '9F\ÖvSA†p?-†¦_ö',_binary '¿3u\Ù<\àF\ç³!@h:\ÚZ','0635405592','sheron@gmail.com','Takalani',_binary '\0','Mukwevho','$2a$10$4A1uRO4Db6H9wgIjDB//ZOgKSQOS/wSDjwNuIYg5dFjHQAdptlAAW','IT'),(_binary 'Z\0•	ô•IÍ¯Ÿö\Ğ\çW\×',_binary 'f/\"¦ŠC®¤\ë~««Á~µ','0645405592','mukwevho@gmail.com','Takalani',_binary '\0','Mukwevho','68581ea1','Technician'),(_binary 'ƒoo%\Ç@Ÿ¶@\Ä2\İE',_binary '¿3u\Ù<\àF\ç³!@h:\ÚZ','0635405592','mendusers@gmail.com','Takalani',_binary '\0','Mukwevho','$2a$10$5laJHu5UTkPQDIEwy/fK.uJ5ulSERvpEP6UGBn8Huxs7EiGaquwbu','IT'),(_binary '‘\Ø\ïÎ·\ÕK¢¿‹?E',_binary '¿3u\Ù<\àF\ç³!@h:\ÚZ','0635405592','enduser@gmail.com','Takalani',_binary '\0','Mukwevho','$2a$10$XvE7gLRnbGbp8flglDI0GOLGczwkpkX10xv4PX6sAKZkGe55UmIFu','IT'),(_binary 'š\Ïß¥Ã’J*ˆj¦Q–pÇ¶',_binary 'f/\"¦ŠC®¤\ë~««Á~µ','0635405592','tk@gmail.com','Takalani',_binary '\0','Mukwevho','0aa0c7e7','Technician'),(_binary 'ó\ØİŒL­Mf’™\ÙU0\ëú',_binary 'úlŠõ\İBBµt9\ç[','0635405592','mukwevhotk181@gmail.com','Takalani',_binary '\0','Mukwevho','$2a$10$EP...mCk41HtHceLArA0R.uBrAuLyAMUOAdqwa41XlQQGaDYrxJk.','IT'),(_binary 'ûŞ–òªGÂ¼\ÂTË‘\Û\Ï',_binary 'úlŠõ\İBBµt9\ç[','0635405592','matodzikutama@gmail.com','TK',_binary '\0','Maxwell','$2a$10$cVTF8U.e5uuY8/9p8L0HpuSKzhgKdeqicRK6kJbfi4NUBiqew5Vf6','Technician');
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'customer-service'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-23 12:48:58
