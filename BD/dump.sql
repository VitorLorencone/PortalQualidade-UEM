-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: portalqualidade
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `sis_administrador`
--

DROP TABLE IF EXISTS `sis_administrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_administrador` (
  `areaResponsavel` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CD_PESSOA` int NOT NULL,
  PRIMARY KEY (`CD_PESSOA`),
  CONSTRAINT `FK53n5tnuqopnwxyt3cdulxq2rh` FOREIGN KEY (`CD_PESSOA`) REFERENCES `sis_pessoa` (`CD_PESSOA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_administrador`
--

LOCK TABLES `sis_administrador` WRITE;
/*!40000 ALTER TABLE `sis_administrador` DISABLE KEYS */;
/*!40000 ALTER TABLE `sis_administrador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_diretoria`
--

DROP TABLE IF EXISTS `sis_diretoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_diretoria` (
  `CD_ORGAO` int NOT NULL,
  PRIMARY KEY (`CD_ORGAO`),
  CONSTRAINT `FK2knxum5sybyb7d43uaxw1rpym` FOREIGN KEY (`CD_ORGAO`) REFERENCES `sis_orgao` (`CD_ORGAO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_diretoria`
--

LOCK TABLES `sis_diretoria` WRITE;
/*!40000 ALTER TABLE `sis_diretoria` DISABLE KEYS */;
INSERT INTO `sis_diretoria` VALUES (1),(2);
/*!40000 ALTER TABLE `sis_diretoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_documento`
--

DROP TABLE IF EXISTS `sis_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_documento` (
  `CD_DOCUMENTO` int NOT NULL AUTO_INCREMENT,
  `DE_DESCRICAO` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DE_LINK_DOCS` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DE_LINK_PDF` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DT_CRIACAO` datetime(6) DEFAULT NULL,
  `DT_VENCIMENTO` datetime(6) DEFAULT NULL,
  `ST_ESTADO` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NM_DOCUMENTO` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TP_DOCUMENTO` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CD_ORGAO` int DEFAULT NULL,
  `CD_PESSOA` int DEFAULT NULL,
  PRIMARY KEY (`CD_DOCUMENTO`),
  KEY `FKcuqrop062l1jgocjmivl5kmip` (`CD_ORGAO`),
  KEY `FKb8ve0y0ysi26b4vv8ce0w4o7v` (`CD_PESSOA`),
  CONSTRAINT `FKb8ve0y0ysi26b4vv8ce0w4o7v` FOREIGN KEY (`CD_PESSOA`) REFERENCES `sis_funcionario_hu` (`CD_PESSOA`),
  CONSTRAINT `FKcuqrop062l1jgocjmivl5kmip` FOREIGN KEY (`CD_ORGAO`) REFERENCES `sis_diretoria` (`CD_ORGAO`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_documento`
--

LOCK TABLES `sis_documento` WRITE;
/*!40000 ALTER TABLE `sis_documento` DISABLE KEYS */;
INSERT INTO `sis_documento` VALUES (1,'descr','link.com.docs','link.com.pdf','2025-11-16 23:00:00.000000','2025-11-23 23:00:00.000000','REVISAO','Docs Teste 1','POP',NULL,1);
/*!40000 ALTER TABLE `sis_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_documento_autores`
--

DROP TABLE IF EXISTS `sis_documento_autores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_documento_autores` (
  `CD_DOCUMENTO` int NOT NULL,
  `CD_PESSOA` int NOT NULL,
  KEY `FKjcijho19ktqs25jcywfrkhlud` (`CD_PESSOA`),
  KEY `FKcqjc01l8f6fafqkd7mj3gnbo7` (`CD_DOCUMENTO`),
  CONSTRAINT `FKcqjc01l8f6fafqkd7mj3gnbo7` FOREIGN KEY (`CD_DOCUMENTO`) REFERENCES `sis_documento` (`CD_DOCUMENTO`),
  CONSTRAINT `FKjcijho19ktqs25jcywfrkhlud` FOREIGN KEY (`CD_PESSOA`) REFERENCES `sis_funcionario_hu` (`CD_PESSOA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_documento_autores`
--

LOCK TABLES `sis_documento_autores` WRITE;
/*!40000 ALTER TABLE `sis_documento_autores` DISABLE KEYS */;
INSERT INTO `sis_documento_autores` VALUES (1,1);
/*!40000 ALTER TABLE `sis_documento_autores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_documento_avaliadores`
--

DROP TABLE IF EXISTS `sis_documento_avaliadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_documento_avaliadores` (
  `CD_DOCUMENTO` int NOT NULL,
  `CD_PESSOA` int NOT NULL,
  KEY `FKm7okvuafto8oyl35oruy410mu` (`CD_PESSOA`),
  KEY `FK2poduv0vmisa439diaum6ox9o` (`CD_DOCUMENTO`),
  CONSTRAINT `FK2poduv0vmisa439diaum6ox9o` FOREIGN KEY (`CD_DOCUMENTO`) REFERENCES `sis_documento` (`CD_DOCUMENTO`),
  CONSTRAINT `FKm7okvuafto8oyl35oruy410mu` FOREIGN KEY (`CD_PESSOA`) REFERENCES `sis_funcionario_hu` (`CD_PESSOA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_documento_avaliadores`
--

LOCK TABLES `sis_documento_avaliadores` WRITE;
/*!40000 ALTER TABLE `sis_documento_avaliadores` DISABLE KEYS */;
INSERT INTO `sis_documento_avaliadores` VALUES (1,1);
/*!40000 ALTER TABLE `sis_documento_avaliadores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_documento_funcionario_qualidade`
--

DROP TABLE IF EXISTS `sis_documento_funcionario_qualidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_documento_funcionario_qualidade` (
  `CD_DOCUMENTO` int NOT NULL,
  `CD_PESSOA` int NOT NULL,
  KEY `FKse2ecasewuf03qa1u8fi2oqye` (`CD_PESSOA`),
  KEY `FKh1jlmiive02xddd85ls2go3bb` (`CD_DOCUMENTO`),
  CONSTRAINT `FKh1jlmiive02xddd85ls2go3bb` FOREIGN KEY (`CD_DOCUMENTO`) REFERENCES `sis_documento` (`CD_DOCUMENTO`),
  CONSTRAINT `FKse2ecasewuf03qa1u8fi2oqye` FOREIGN KEY (`CD_PESSOA`) REFERENCES `sis_funcionario_qualidade` (`CD_PESSOA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_documento_funcionario_qualidade`
--

LOCK TABLES `sis_documento_funcionario_qualidade` WRITE;
/*!40000 ALTER TABLE `sis_documento_funcionario_qualidade` DISABLE KEYS */;
INSERT INTO `sis_documento_funcionario_qualidade` VALUES (1,3);
/*!40000 ALTER TABLE `sis_documento_funcionario_qualidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_documento_setor`
--

DROP TABLE IF EXISTS `sis_documento_setor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_documento_setor` (
  `CD_DOCUMENTO` int NOT NULL,
  `CD_ORGAO` int NOT NULL,
  KEY `FK6b7s03o9gqr4a97q06kjwcyad` (`CD_ORGAO`),
  KEY `FKb0yorj84q063hl9eqks9ck1bd` (`CD_DOCUMENTO`),
  CONSTRAINT `FK6b7s03o9gqr4a97q06kjwcyad` FOREIGN KEY (`CD_ORGAO`) REFERENCES `sis_setor` (`CD_ORGAO`),
  CONSTRAINT `FKb0yorj84q063hl9eqks9ck1bd` FOREIGN KEY (`CD_DOCUMENTO`) REFERENCES `sis_documento` (`CD_DOCUMENTO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_documento_setor`
--

LOCK TABLES `sis_documento_setor` WRITE;
/*!40000 ALTER TABLE `sis_documento_setor` DISABLE KEYS */;
INSERT INTO `sis_documento_setor` VALUES (1,3);
/*!40000 ALTER TABLE `sis_documento_setor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_funcionario_hu`
--

DROP TABLE IF EXISTS `sis_funcionario_hu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_funcionario_hu` (
  `CD_PESSOA` int NOT NULL,
  PRIMARY KEY (`CD_PESSOA`),
  CONSTRAINT `FKktnv19teyev93rcdllg5pas79` FOREIGN KEY (`CD_PESSOA`) REFERENCES `sis_pessoa` (`CD_PESSOA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_funcionario_hu`
--

LOCK TABLES `sis_funcionario_hu` WRITE;
/*!40000 ALTER TABLE `sis_funcionario_hu` DISABLE KEYS */;
INSERT INTO `sis_funcionario_hu` VALUES (1),(2);
/*!40000 ALTER TABLE `sis_funcionario_hu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_funcionario_qualidade`
--

DROP TABLE IF EXISTS `sis_funcionario_qualidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_funcionario_qualidade` (
  `CD_PESSOA` int NOT NULL,
  PRIMARY KEY (`CD_PESSOA`),
  CONSTRAINT `FKo2f7m5ju1wmoinvsw6aw84vk1` FOREIGN KEY (`CD_PESSOA`) REFERENCES `sis_pessoa` (`CD_PESSOA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_funcionario_qualidade`
--

LOCK TABLES `sis_funcionario_qualidade` WRITE;
/*!40000 ALTER TABLE `sis_funcionario_qualidade` DISABLE KEYS */;
INSERT INTO `sis_funcionario_qualidade` VALUES (3);
/*!40000 ALTER TABLE `sis_funcionario_qualidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_mensagem`
--

DROP TABLE IF EXISTS `sis_mensagem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_mensagem` (
  `CD_MENSAGEM` int NOT NULL AUTO_INCREMENT,
  `DE_CATEGORIA` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DE_CORPO` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DE_FREQUENCIA` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DE_NOME` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DT_FIM` date DEFAULT NULL,
  `DT_INICIO` date DEFAULT NULL,
  PRIMARY KEY (`CD_MENSAGEM`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_mensagem`
--

LOCK TABLES `sis_mensagem` WRITE;
/*!40000 ALTER TABLE `sis_mensagem` DISABLE KEYS */;
INSERT INTO `sis_mensagem` VALUES (1,'Customizada','Para @destinatario\n\nOi, veja o documento @documento com o link @link até a data @data\n\nAtt','U','Mensagem Padrão','2025-11-15','2025-11-02');
/*!40000 ALTER TABLE `sis_mensagem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_orgao`
--

DROP TABLE IF EXISTS `sis_orgao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_orgao` (
  `CD_ORGAO` int NOT NULL AUTO_INCREMENT,
  `NM_ORGAO` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`CD_ORGAO`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_orgao`
--

LOCK TABLES `sis_orgao` WRITE;
/*!40000 ALTER TABLE `sis_orgao` DISABLE KEYS */;
INSERT INTO `sis_orgao` VALUES (1,'Qualidade'),(2,'Enfermagem'),(3,'X');
/*!40000 ALTER TABLE `sis_orgao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_pessoa`
--

DROP TABLE IF EXISTS `sis_pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_pessoa` (
  `CD_PESSOA` int NOT NULL AUTO_INCREMENT,
  `NU_CELULAR` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LT_EMAIL` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NM_PESSOA` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`CD_PESSOA`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_pessoa`
--

LOCK TABLES `sis_pessoa` WRITE;
/*!40000 ALTER TABLE `sis_pessoa` DISABLE KEYS */;
INSERT INTO `sis_pessoa` VALUES (1,'1','vitorlorencone@gmail.com','Vitor'),(2,'2','vitorlorencone@gmail.com','Nuno'),(3,'1','vitorlorencone@gmail.com','Enzo');
/*!40000 ALTER TABLE `sis_pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_setor`
--

DROP TABLE IF EXISTS `sis_setor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_setor` (
  `CD_ORGAO` int NOT NULL,
  PRIMARY KEY (`CD_ORGAO`),
  CONSTRAINT `FKgq6tp5reosmdc5a8jpgjp8u0o` FOREIGN KEY (`CD_ORGAO`) REFERENCES `sis_orgao` (`CD_ORGAO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_setor`
--

LOCK TABLES `sis_setor` WRITE;
/*!40000 ALTER TABLE `sis_setor` DISABLE KEYS */;
INSERT INTO `sis_setor` VALUES (3);
/*!40000 ALTER TABLE `sis_setor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sis_usuario`
--

DROP TABLE IF EXISTS `sis_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sis_usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nome` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `senha_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6gdqwd3ydtv0u5e46536byi4w` (`email`),
  UNIQUE KEY `UK_cj3eyirvab2hq9buixro3ldt6` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sis_usuario`
--

LOCK TABLES `sis_usuario` WRITE;
/*!40000 ALTER TABLE `sis_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `sis_usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-30 19:31:38
