package app.management;

import app.management.dao.UpdateDB;
import app.management.manager.ApplicationManager;
import app.management.model.config.Configuration;
import app.management.model.entity.ApplicationDataEntity;
import app.management.utils.Constants;
import app.management.utils.Utils;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationListLambda implements RequestHandler<ApplicationDataEntity, Object> {

    private static Configuration config = Utils.loadConfig(Constants.Configurations.CONFIGURATION_YAML,
                                                           Configuration.class);

    private static UpdateDB updateDB;

    static {
        Map<String, Object> jdbcConfig = new HashMap<>();
        if (config.getDatabaseConfig().getEndpoint() != null) {
            jdbcConfig.put(Constants.Database.JDBC_URL, config.getDatabaseConfig().getEndpoint());
        }
        if (config.getDatabaseConfig().getCredentials().getUsername() != null) {
            jdbcConfig.put(Constants.Database.JDBC_USER, config.getDatabaseConfig().getCredentials().getUsername());
        }
        if (config.getDatabaseConfig().getCredentials().getPassword() != null) {
            jdbcConfig.put(Constants.Database.JDBC_PASSWORD, config.getDatabaseConfig().getCredentials().getPassword());
        }
        if (config.getDatabaseConfig().getPoolSize() != null) {
            jdbcConfig.put(Constants.Database.C3P0_MAX_CONNECTION_POOL_SIZE, config.getDatabaseConfig().getPoolSize());
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(Constants.Database.PERSISTENCE_UNIT_NAME,
                                                                          jdbcConfig);
        updateDB = new UpdateDB(emf, 3, 5000);
    }

    private static ApplicationManager applicationManager = new ApplicationManager(updateDB, config);

    @Override
    public Object handleRequest(ApplicationDataEntity userData, Context context) {
        return getApplicationList(userData.getId());
    }

    public static void main(String[] args) {

        getApplicationList("1234");
    }

    private static Object getApplicationList(String tenantId) {

        JsonArray response = new JsonArray();
        try {
            List<ApplicationDataEntity> applicationList = applicationManager.listTenantApplication();
            response = createOutput(applicationList, tenantId);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Data added :" + response.toString());
        return response;
    }

    private static JsonArray createOutput(List<ApplicationDataEntity> userDataEntityList, String tenantId) {

        JsonArray allApps = new JsonArray();

        for (ApplicationDataEntity userDataEntity : userDataEntityList) {

            JsonObject app = new JsonObject();

            app.addProperty("tenantName", userDataEntity.getId());
            app.addProperty("clientID", userDataEntity.getClientId());
            app.addProperty("applicationName", userDataEntity.getAppName());
            app.addProperty("callbackURL", userDataEntity.getCallBackUrl());

            if (!tenantId.isEmpty() && tenantId.equals(userDataEntity.getId())) {
                allApps.add(app);
            }
        }

        return allApps;
    }

}
