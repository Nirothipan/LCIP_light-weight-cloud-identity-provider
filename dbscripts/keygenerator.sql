

DROP TABLE IF EXISTS `USER_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_INFO` (
 `tenantId` INTEGER (50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `password` varchar(1000) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
