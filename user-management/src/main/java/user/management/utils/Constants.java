package user.management.utils;

public class Constants {

    private Constants() {

    }

    /**
     * Database constants.
     */
    public static class Database {

        public static final String PERSISTENCE_UNIT_NAME = "user.management.model.UserData";
        public static final String JDBC_URL = "javax.persistence.jdbc.url";
        public static final String JDBC_USER = "javax.persistence.jdbc.user";
        public static final String JDBC_PASSWORD = "javax.persistence.jdbc.password";
        public static final String C3P0_MAX_CONNECTION_POOL_SIZE = "hibernate.c3p0.max_size";

        /**
         * Database Queries Constants.
         */
        public static class Queries {

            public static final String FIND_LICENSE_KEY_IF_EXISTS_FOR_A_GIVEN_USER_NAME = "getAllUser";
            public static final String FIND_LICENSE_KEY_IF_EXISTS_FOR_A_GIVEN_USER = "SELECT * FROM USER_INFO";
        }

    }
}
