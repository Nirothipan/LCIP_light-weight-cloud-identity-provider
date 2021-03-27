package user.management;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import user.management.dao.UpdateDB;
import user.management.manager.UserManager;
import user.management.model.UserData;
import user.management.model.config.Configuration;
import user.management.model.entity.UserDataEntity;
import user.management.utils.Constants;
import user.management.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UserManagement {

    //private static Configuration config = Utils.loadConfig(Constants.Configurations.CONFIGURATION_YAML,
                                                           //Configuration.class);

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

        String password = System.getenv("password");
        if (password == null || password.isEmpty()) {
            password = "root";
        }
        jdbcConfig.put(Constants.Database.JDBC_PASSWORD, password);

        jdbcConfig.put(Constants.Database.C3P0_MAX_CONNECTION_POOL_SIZE, 30);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(Constants.Database.PERSISTENCE_UNIT_NAME,
                                                                          jdbcConfig);
        updateDB = new UpdateDB(emf, 3, 5000);
    }

    protected static UserManager userManager = new UserManager(updateDB);

    public static void main(String[] args) {

        //addUser();
        // removeUser("niro" , 1234);
    }

    //
    //    private Object addUser(UserDataEntity userData, Context context) {
    //        LambdaLogger logger = context.getLogger();
    //        String okResult = "200 OK";
    //        // log execution details
    //        logger.log("initializing hanlder ");
    //
    //        //         config = Utils.loadConfig(Constants.Configurations.CONFIGURATION_YAML, Configuration.class);
    //
    //        JsonObject response = new JsonObject();
    //        try {
    //            response = userManager.addUser(userData);
    //        } catch (Exception e) {
    //            logger.log("Exception :: " + e);
    //            e.printStackTrace();
    //        }
    //        // logger.log("Response : " + response.toString());
    //        return response.toString();
    //    }

}
