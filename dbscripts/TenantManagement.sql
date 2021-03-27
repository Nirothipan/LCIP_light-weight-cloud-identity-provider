  DROP TABLE IF EXISTS `TENANT_INFO`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TENANT_INFO`
(
    `tenant_id`     varchar(50) NOT NULL,
    `tenant_name`   varchar(50) NOT NULL,
    PRIMARY KEY (`tenant_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 32
  DEFAULT CHARSET = latin1;