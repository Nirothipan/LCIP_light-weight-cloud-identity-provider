package licensekey.generator;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import licensekey.generator.dao.UpdateDB;
import licensekey.generator.manager.KeyGenManager;
import licensekey.generator.model.UserData;
import licensekey.generator.model.config.Configuration;
import licensekey.generator.utils.Constants;
import licensekey.generator.utils.Utils;
import org.hibernate.exception.JDBCConnectionException;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

public class KeyGeneratorHandler implements RequestHandler<UserData, Object> {

    private KeyGenManager keyGenManager;
    private Configuration config;


    @Override
    public Object handleRequest(UserData userData, Context context) {
        LambdaLogger logger = context.getLogger();
        String okResult = "200 OK";
        // log execution details
        logger.log("initializing hanlder ");

         config = Utils.loadConfig(Constants.Configurations.CONFIGURATION_YAML, Configuration.class);
        if (keyGenManager == null) {
            initializeKeyGenManager();
        }
        JsonObject response = new JsonObject();
        try {
            response = keyGenManager.generateKey(userData);
        } catch (Exception e) {
            logger.log("Exception :: " + e);
            e.printStackTrace();
        }
       // logger.log("Response : " + response.toString());
        return response.toString();
    }

    /**
     * This method is used to initialize the Manager.
     */
    private synchronized void initializeKeyGenManager() {

        if (keyGenManager != null) {
            return;
        }
        // Map<String, Object> jdbcConfig = new HashMap<>();
        // if (config.getDatabaseConfig().getEndpoint() != null) {
        //     jdbcConfig.put(Constants.Database.JDBC_URL, config.getDatabaseConfig().getEndpoint());
        // }
        // if (config.getDatabaseConfig().getCredentials().getUsername() != null) {
        //     jdbcConfig.put(Constants.Database.JDBC_USER,
        //             config.getDatabaseConfig().getCredentials().getUsername());
        // }
        // if (config.getDatabaseConfig().getCredentials().getPassword() != null) {
        //     jdbcConfig.put(Constants.Database.JDBC_PASSWORD,
        //             config.getDatabaseConfig().getCredentials().getPassword());
        // }
        // if (config.getDatabaseConfig().getPoolSize() != null) {
        //     jdbcConfig.put(Constants.Database.C3P0_MAX_CONNECTION_POOL_SIZE, config.getDatabaseConfig().getPoolSize());
        // }
        // int numAttempts = 0;
        // do {
        //     ++numAttempts;
        //     try {
        //         EntityManagerFactory emf = Persistence.createEntityManagerFactory(
        //                 Constants.Database.PERSISTENCE_UNIT_NAME,
        //                 jdbcConfig);
        //         keyGenManager = new KeyGenManager(new UpdateDB(emf, config.getDatabaseConfig().getMaxRetries(),
        //                 config.getDatabaseConfig().getRetryInterval()), config);
        //         return;
        //     } catch (PersistenceException e) {
        //         Throwable cause = e.getCause();
        //         if ((cause instanceof CommunicationsException || cause instanceof JDBCConnectionException)) {
        //             continue;
        //         }
        //         throw new RuntimeException("Exception occurred when creating EntityManagerFactory for the named " +
        //                 "persistence unit: ", e);
        //     }
        // } while (numAttempts <= config.getDatabaseConfig().getMaxRetries());
        keyGenManager = new KeyGenManager(config);
    }
}
