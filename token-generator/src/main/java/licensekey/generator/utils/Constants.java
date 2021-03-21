package licensekey.generator.utils;

/**
 * This class includes the constance used in the licensekey.generator package.
 *
 * @since 1.0.0
 */
public class Constants {

    public static final String ISSUER = "uom.lk";
    public static final String API_CODE_CLAIM = "apiCodes";

    private Constants() {

    }

    /**
     * Configuration constants.
     */
    public static class Configurations {

        public static final String CONFIGURATION_YAML = "licensekeygenerator.yaml";
    }

    /**
     * Database constants.
     */
    public static class Database {

        public static final String PERSISTENCE_UNIT_NAME = "org.wso2.carbon.licensekey.generator.unit";
        public static final String JDBC_URL = "javax.persistence.jdbc.url";
        public static final String JDBC_USER = "javax.persistence.jdbc.user";
        public static final String JDBC_PASSWORD = "javax.persistence.jdbc.password";
        public static final String C3P0_MAX_CONNECTION_POOL_SIZE = "hibernate.c3p0.max_size";

        /**
         * Database Queries Constants.
         */
        public static class Queries {

            public static final String FIND_LICENSE_KEY_IF_EXISTS_FOR_A_GIVEN_USER_NAME = "findLicenseKeyByUsername";
            public static final String FIND_LICENSE_KEY_IF_EXISTS_FOR_A_GIVEN_USER =
                    "SELECT * FROM KEY_GEN_INFO WHERE username = :user_name";
        }

        /**
         * SQL query parameters.
         */
        public static class QueryParams {

            public static final String USERNAME = "user_name";
        }
    }
}
