package licensekey.generator.manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import licensekey.generator.dao.UpdateDB;
import licensekey.generator.exception.DBException;
import licensekey.generator.exception.PrivateKeyGenerationException;
import licensekey.generator.model.UserData;
import licensekey.generator.model.config.Configuration;
import licensekey.generator.model.entity.LicensekeyGeneratorEntity;
import licensekey.generator.service.PrivateKeyReader;
import org.hibernate.exception.JDBCConnectionException;

import java.security.interfaces.RSAPrivateKey;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import javax.persistence.PersistenceException;

import static licensekey.generator.utils.Constants.API_CODE_CLAIM;
import static licensekey.generator.utils.Constants.ISSUER;

/**
 * This class will generate or retrieve the existing the JWT key.
 *
 * @since 1.0.0
 */
public class KeyGenManager {

    private final Configuration config;
    private final UpdateDB updateDB;

    public KeyGenManager(Configuration config) {

        this.config = config;
        this.updateDB = null;

    }

    public KeyGenManager(UpdateDB updateDB, Configuration config) {

        this.config = config;
        this.updateDB = updateDB;

    }

    /**
     * This method create the output Object
     *
     * @param jwt       JWT token
     * @param errorCode Error code to distinguish the status of the response from the client side
     * @param message   Message to the client side if any
     * @return JsonObject
     */
    private JsonObject createOutput(String jwt, int errorCode, String message) {

        JsonObject output = new JsonObject();
        output.addProperty("key", jwt);
        output.addProperty("errorCode", errorCode);
        output.addProperty("message", message);
        return output;
    }

    /**
     * Generates a custom key as the license key. If a license key is already available for the same user, that key is
     * returned without generating a new one.
     *
     * @param userData input Json which includes username
     * @return Json Object with the productCodes, errorCode, message and a JWT
     * @throws PrivateKeyGenerationException If the Private Key cannot be generated
     * @throws DBException                   If the Database connection causes an error
     */
    public JsonObject generateKey(UserData userData) throws DBException, Exception {
        // Avoid creating duplicate keys
        String username = userData.getUsername();
        String jwt = null;

        if (jwt == null) {
            Timestamp timestamp = Timestamp.valueOf(userData.getExpiryDate());
            Calendar currentTime = Calendar.getInstance();
            Date expiryDate = new Date(timestamp.getTime());
            List<String> productCodesList = userData.getApis();
            String[] apiCodesArray = new String[productCodesList.size()];
            apiCodesArray = productCodesList.toArray(apiCodesArray);
            Date today = new Date((currentTime.getTime()).getTime());
            Algorithm signingAlgorithm = getAlgorithm();

            jwt = JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(today)
                    .withExpiresAt(expiryDate)
                    .withArrayClaim(API_CODE_CLAIM, apiCodesArray)
                    .sign(signingAlgorithm);

            LicensekeyGeneratorEntity licensekeyGeneratorEntity = new LicensekeyGeneratorEntity();
            licensekeyGeneratorEntity.setCreatedDate(today.getTime());
            licensekeyGeneratorEntity.setCreatorUsername(userData.getCreatorUsername());
            licensekeyGeneratorEntity.setExpiryDate(expiryDate.getTime());
            licensekeyGeneratorEntity.setJwtToken(jwt);
            licensekeyGeneratorEntity.setUsername(username);
            // Persist to the database
            // persistToDB(licensekeyGeneratorEntity);

        }
        return createOutput(jwt, 0, null);
    }

    /**
     * This method inserts the license key information to the DB.
     *
     * @param licenseKeyGeneratorEntity entity to be persisted
     * @throws DBException If the DB connection causes an error
     */
    private synchronized void persistToDB(LicensekeyGeneratorEntity licenseKeyGeneratorEntity) throws DBException {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                updateDB.insertEntity(licenseKeyGeneratorEntity);
                return;
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if ((cause instanceof CommunicationsException ||
                        cause instanceof JDBCConnectionException)) {
                    continue;
                }
                throw new RuntimeException("Exception occurred when creating EntityManagerFactory for the named " +
                        "persistence unit: ", e);
            }
        } while (numAttempts <= config.getDatabaseConfig().getMaxRetries());
    }

    /**
     * This method is to retrieve the encryption algorithm
     *
     * @return Algorithm
     * @throws PrivateKeyGenerationException errors with reading provate key files
     */
    private Algorithm getAlgorithm() throws Exception {

       
            RSAPrivateKey privateKey = (RSAPrivateKey) PrivateKeyReader.getPrivateKey(
                config.getKeyFileInfo().getLocation());
            return Algorithm.RSA384(null, privateKey);
        
    }
}
