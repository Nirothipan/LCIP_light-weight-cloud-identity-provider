DROP TABLE IF EXISTS `TOKEN_INFO`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TOKEN_INFO`
(
    `token`        varchar(1000) NOT NULL,
    `tenant_id`    varchar(50)   NOT NULL,
    `user_name`    varchar(50)   NOT NULL,
    `app_id`       varchar(50)   NOT NULL,
    `created_date` bigint(20) NOT NULL,
    `expiry_date`  bigint(20) NOT NULL,
    PRIMARY KEY (`token`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

