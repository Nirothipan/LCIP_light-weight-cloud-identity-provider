

DROP TABLE IF EXISTS `USER_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_INFO` (
                             `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                             `tenant_id` varchar (50) NOT NULL,
                             `user_name` varchar(50) NOT NULL,
                             `user_email` varchar(50) NOT NULL,
                             `password` varchar(1000) NOT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
# todo add constraint
#
# DROP TABLE IF EXISTS `APPLICATION_INFO`;
# /*!40101 SET @saved_cs_client     = @@character_set_client */;
# /*!40101 SET character_set_client = utf8 */;
# CREATE TABLE `APPLICATION_INFO` (
#                                     `client_id` varchar(1000) NOT NULL,
#                                     `application_name` varchar(50) NOT NULL,
#                                     `tenant_id` varchar(50) NOT NULL,
#                                     `callbackUrl` varchar(1000) NOT NULL,
#                                     PRIMARY KEY (`client_id`),
#                                     CONSTRAINT application UNIQUE (`tenant_id`,`application_name`)
# ) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
