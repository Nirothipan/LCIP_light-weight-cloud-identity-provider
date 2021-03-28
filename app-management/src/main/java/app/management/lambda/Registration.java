package app.management.lambda;

import app.management.ApplicationManagement;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import app.management.dao.UpdateDB;
import app.management.manager.ApplicationManager;
import app.management.model.config.Configuration;
import app.management.model.entity.ApplicationDataEntity;
import app.management.utils.Constants;
import app.management.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Registration extends ApplicationManagement implements RequestHandler<ApplicationDataEntity, Object> {

    @Override
    public Object handleRequest(ApplicationDataEntity userData, Context context) {
        return addApplication(userData, context);
    }

    public static void main(String[] args) {

      addApplication();
    }

    private static void addApplication(){

        ApplicationDataEntity appData = new ApplicationDataEntity();
        appData.setAppName("AppNew3");
        appData.setCallBackUrl("https://google.com");
        appData.setTenantId("12345");
        JsonObject response = new JsonObject();
        try {
            response = applicationManager.addApplication(appData);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Data added :" + response.toString());
    }

    private static void removeApplication(String name, String id){
        JsonObject response = new JsonObject();
        try {
            response = applicationManager.deleteApplication(name, id);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Data added :" + response.toString());
    }

    private Object addApplication(ApplicationDataEntity userData, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("initializing handler ");

        JsonObject response = new JsonObject();
        try {
            response = applicationManager.addApplication(userData);
        } catch (Exception e) {
            logger.log("Exception :: " + e);
            e.printStackTrace();
            return getErrorOutput(e.getMessage()).toString();
        }
        return response.toString();
    }

}
