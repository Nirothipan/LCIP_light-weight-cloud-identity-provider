package app.management;

import app.management.dao.UpdateDB;
import app.management.manager.ApplicationManager;
import app.management.model.entity.ApplicationDataEntity;
import app.management.utils.Constants;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Main user.management.Application class.
 *
 * @since 1.0.0
 */
public class ApplicationManagement {

    private static UpdateDB updateDB;

    static {
        Map<String, Object> jdbcConfig = new HashMap<>();

        String jdbcUrl = System.getenv("jdbcUrl");
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            jdbcUrl = "jdbc:mysql://localhost:3306/cloud?useSSL=false";
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

    protected static ApplicationManager applicationManager = new ApplicationManager(updateDB);

    protected static JsonObject getErrorOutput(String message) {
        JsonObject output = new JsonObject();
        output.addProperty("Status", "Internal Server Error");
        output.addProperty("Message", message);
        return output;
    }

    protected static boolean checkApplication(String applicationName, String tenantID) {
        List<ApplicationDataEntity> applicationList = null;
        try {
            applicationList = applicationManager.listTenantApplication();
        } catch (Exception e) {
            return false;
        }
        Optional<ApplicationDataEntity> resultList = applicationList.stream().parallel().filter(
                app -> app.getTenantId().equals(tenantID) && app.getAppName().equals(applicationName)).findFirst();
        return resultList.isPresent();
    }
}
