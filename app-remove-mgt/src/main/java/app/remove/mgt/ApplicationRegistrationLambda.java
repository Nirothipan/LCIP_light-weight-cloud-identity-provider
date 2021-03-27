package app.remove.mgt;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import app.remove.mgt.dao.UpdateDB;
import app.remove.mgt.manager.ApplicationManager;
import app.remove.mgt.model.config.Configuration;
import app.remove.mgt.model.entity.ApplicationDataEntity;
import app.remove.mgt.utils.Constants;
import app.remove.mgt.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ApplicationRegistrationLambda implements RequestHandler<ApplicationDataEntity, Object> {

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
    public Object handleRequest(ApplicationDataEntity appData, Context context) {
        return removeApplication(appData.getAppName(),appData.getId());
    }

    public static void main(String[] args) {

//        addApplication();
//        removeApplication();
    }

    private static void addApplication(){
        ApplicationDataEntity userData = new ApplicationDataEntity();
        userData.setAppName("App123");
        userData.setCallBackUrl("https://google.com");
        JsonObject response = new JsonObject();
        try {
            response = applicationManager.addApplication(userData);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Data added :" + response.toString());
    }

    private static void removeApplication(){
        ApplicationDataEntity userData = new ApplicationDataEntity();
        userData.setAppName("App123");
        JsonObject response = new JsonObject();
        try {
            response = applicationManager.deleteApplication(userData.getAppName(), "1234");
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Removed added :" + response.toString());
    }

    private Object removeApplication(String name, String id){
        JsonObject response = new JsonObject();
        try {
            response = applicationManager.deleteApplication(name, id);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        return response.toString();
    }

    private Object addApplication(ApplicationDataEntity userData, Context context) {
        LambdaLogger logger = context.getLogger();
        String okResult = "200 OK";
        // log execution details
        logger.log("initializing handler ");

         config = Utils.loadConfig(Constants.Configurations.CONFIGURATION_YAML, Configuration.class);

        JsonObject response = new JsonObject();
        try {
            response = applicationManager.addApplication(userData);
        } catch (Exception e) {
            logger.log("Exception :: " + e);
            e.printStackTrace();
        }
        // logger.log("Response : " + response.toString());
        return response.toString();
    }

}
