package licensekey.generator.lambda;

import app.management.ApplicationManagement;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import licensekey.generator.dao.UpdateDB;
import licensekey.generator.lambda.dao.LicenseKeyGenerator;
import licensekey.generator.manager.KeyGenManager;
import licensekey.generator.model.UserData;
import licensekey.generator.model.entity.LicensekeyGeneratorEntity;
import licensekey.generator.utils.Constants;
import org.json.simple.JSONObject;
import user.management.UserApi;
import user.management.model.entity.UserDataEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GenerateKey implements RequestHandler<LicenseKeyGenerator, Object> {

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
        token.setUserName("ram2");
        token.setAppId("id-2");
        token.setTenantId("121qw");
        token.setExpiryDate(getDate("2021-11-01 00:00:00+0530"));
        createKey(token, "1234", new String[] {});
    }

    private static long getDate(String date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        try {
            Date d = f.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            return 0L;
        }
    }

    @Override
    public Object handleRequest(LicenseKeyGenerator token, Context context) {

        LicensekeyGeneratorEntity licensekeyGeneratorEntity = new LicensekeyGeneratorEntity();
        licensekeyGeneratorEntity.setUserName(token.getUserName());
        licensekeyGeneratorEntity.setAppId(token.getAppName());
        licensekeyGeneratorEntity.setTenantId(token.getTenantId());
        licensekeyGeneratorEntity.setExpiryDate(getDate(token.getExpiryDate()));

        String[] scopes = token.getScopes();
        if (scopes == null) {
            scopes = new String[] {};
        }
        return createKey(licensekeyGeneratorEntity, token.getPassword(), scopes);
    }

    public static Object createKey(LicensekeyGeneratorEntity token, String pwd, String[] scopes) {

        // validate expiry date
        Date expiryDate = new Date(token.getExpiryDate());
        Date currentDate = new Date((Calendar.getInstance().getTime()).getTime());
        if (currentDate.after(expiryDate)) {
            String msg = "Expiry date is invalid";
            System.out.println(msg);
            return (getErrorOutput(msg));
        }

        UserDataEntity userDataEntity = new UserDataEntity();
        userDataEntity.setTenantId(token.getTenantId());
        userDataEntity.setUserName(token.getUserName());
        userDataEntity.setPassword(pwd);

        UserApi userApi = new UserApi();
        boolean isUserValid = userApi.validate(userDataEntity);

        if (isUserValid) {
            if (!ApplicationManagement.checkApplication(token.getAppId(), token.getTenantId())) {
                System.out.println(getErrorOutput("Application Not Valid"));
                return getErrorOutput("Application Not Valid");
            }
        } else {
            System.out.println(getErrorOutput("Invalid User"));
            return getErrorOutput("Invalid User");
        }

        try {
            LicensekeyGeneratorEntity existingToken = getToken(token);
            if (existingToken != null) {
                Object response = keyGenManager.createOutput(existingToken.getToken(), 0, null);
                System.out.println(response);
                return response;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return (getErrorOutput(ex.getMessage()));
        }

        // generate token
        UserData userData = new UserData();
        userData.setUsername(token.getUserName());
        userData.setTenantId(token.getTenantId());
        userData.setAppId(token.getAppId());
        userData.setExpiryDate(token.getExpiryDate());
        try {
            return keyGenManager.generateKeyAndUpdateDB(userData, scopes);
        } catch (Exception ex) {
            ex.printStackTrace();
            return (getErrorOutput(ex.getMessage()));
        }
    }

    protected static JSONObject getErrorOutput(String message) {
        JSONObject output = new JSONObject();
        output.put("Status", "Internal Server Error");
        output.put("Message", message);
        return output;
    }

    private static LicensekeyGeneratorEntity getToken(LicensekeyGeneratorEntity token) throws Exception {
        return keyGenManager.fetchToken(token);
    }
}
