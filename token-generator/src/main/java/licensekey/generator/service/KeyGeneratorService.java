package licensekey.generator.service;

import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import licensekey.generator.Application;
import licensekey.generator.dao.UpdateDB;
import licensekey.generator.manager.KeyGenManager;
import licensekey.generator.model.UserData;
import licensekey.generator.model.config.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import licensekey.generator.exception.DBException;
import licensekey.generator.exception.PrivateKeyGenerationException;
import licensekey.generator.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Licence key generator class.
 *
 * @since 1.0.0
 */
@Path("/keygenerator")
public class KeyGeneratorService {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    private KeyGenManager keyGenManager;
    private Configuration config;

    public KeyGeneratorService(Configuration config) {

        this.config = config;
    }

    /**
     * Generate license key.
     *
     * @param userData Input data from the Json which includes username, creator username,
     *                 list of products and the expiry date.
     * @return JsonObject as the response which includes an errorCode, an error message and the JWT
     */
    @POST
    @Path("/key")
    @Consumes("application/json")
    @Produces("application/json")
    public Response generateCustomKey(UserData userData) throws DBException, Exception {

        LOG.info("Generating the custom license key for the user: " + userData.getUsername());
        if (keyGenManager == null) {
            initializeKeyGenManager();
        }

        JsonObject response = keyGenManager.generateKey(userData);
        return Response.ok().entity(response).build();
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
