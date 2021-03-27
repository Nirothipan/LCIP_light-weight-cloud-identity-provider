DROP TABLE IF EXISTS `USER_INFO`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_INFO`
(
    `id`         int(6) unsigned NOT NULL AUTO_INCREMENT,
    `tenant_id`  varchar(50)     NOT NULL,
    `user_name`  varchar(50)     NOT NULL,
    `user_email` varchar(50)     NOT NULL,
    `password`   varchar(1000)   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT userManagement UNIQUE (tenant_id, user_name)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = latin1;
