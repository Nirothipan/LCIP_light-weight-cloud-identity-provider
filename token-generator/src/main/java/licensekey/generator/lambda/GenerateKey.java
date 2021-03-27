package licensekey.generator.lambda;

import app.management.ApplicationManagement;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import licensekey.generator.dao.UpdateDB;
import licensekey.generator.manager.KeyGenManager;
import licensekey.generator.model.UserData;
import licensekey.generator.model.entity.LicensekeyGeneratorEntity;
import licensekey.generator.utils.Constants;
import user.management.UserApi;
import user.management.model.entity.UserDataEntity;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GenerateKey implements RequestHandler<LicensekeyGeneratorEntity, Object> {

    private static UpdateDB updateDB;

    static {
        Map<String, Object> jdbcConfig = new HashMap<>();

        String jdbcUrl = System.getenv("jdbcUrl");
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            jdbcUrl = "jdbc:mysql://localhost:3306/cloud_db?useSSL=false";
        }
        jdbcConfig.put(Constants.Database.JDBC_URL, jdbcUrl);

        String userName = System.getenv("userName");
        if (userName == null || userName.isEmpty()) {
            userName = "root";
        }
        jdbcConfig.put(Constants.Database.JDBC_USER, userName);

        String secret = System.getenv("password");
        if (secret == null || secret.isEmpty()) {
            secret = "root";
        }
        jdbcConfig.put(Constants.Database.JDBC_PASSWORD, secret);
        jdbcConfig.put(Constants.Database.C3P0_MAX_CONNECTION_POOL_SIZE, 30);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(Constants.Database.PERSISTENCE_UNIT_NAME,
                                                                          jdbcConfig);
        updateDB = new UpdateDB(emf, 3, 5000);

    }

    private static KeyGenManager keyGenManager = new KeyGenManager(updateDB);

    public static void main(String[] args) {
        LicensekeyGeneratorEntity token = new LicensekeyGeneratorEntity();
        token.setUserName("niro");
        token.setAppId("1212");
        token.setTenantId("212121");
        token.setExpiryTime("2020-11-11 00:00:00");
        createKey(token);
    }

    @Override
    public Object handleRequest(LicensekeyGeneratorEntity token, Context context) {
        return null;
    }

    public static Object createKey(LicensekeyGeneratorEntity token) {

        UserDataEntity userDataEntity = new UserDataEntity();

        UserApi userApi = new UserApi();
        boolean isUserValid = userApi.validate(userDataEntity);

        if (isUserValid) {
            if (!ApplicationManagement.checkApplication(token.getAppId(), token.getTenantId())) {
                return getErrorOutput("Application Not Valid");
            }
        } else {
            return getErrorOutput("Invalid User");
        }

        LicensekeyGeneratorEntity existingToken = getToken(token);

        if (token != null) {
            // do something and send the token back
        }

        // generate token

        UserData userData = new UserData();
        userData.setUsername(token.getUserName());
        userData.setTenantId(token.getTenantId());
        userData.setAppId(token.getAppId());
        userData.setExpiryDate(token.getExpiryTime());
        try {
            JsonObject response = keyGenManager.generateKeyAndUpdateDB(userData);
            return response.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return toJson(getErrorOutput(ex.getMessage()));
        }
    }

    protected static JsonObject getErrorOutput(String message) {
        JsonObject output = new JsonObject();
        output.addProperty("Status", "Internal Server Error");
        output.addProperty("Message", message);
        return output;
    }

    protected static String toJson(Object json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    // todo - get Existing token from the db
    private static LicensekeyGeneratorEntity getToken(LicensekeyGeneratorEntity token) {
        LicensekeyGeneratorEntity key = new LicensekeyGeneratorEntity();
        return key;
    }
}
