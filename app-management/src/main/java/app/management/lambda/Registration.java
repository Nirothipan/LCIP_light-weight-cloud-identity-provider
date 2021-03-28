package app.management.lambda;

import app.management.ApplicationManagement;
import app.management.model.entity.ApplicationDataEntity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;

public class Registration extends ApplicationManagement implements RequestHandler<ApplicationDataEntity, Object> {

    @Override
    public Object handleRequest(ApplicationDataEntity userData, Context context) {
        return addApplication(userData, context);
    }

    public static void main(String[] args) {

        addApplication();
    }

    private static void addApplication() {

        ApplicationDataEntity appData = new ApplicationDataEntity();
        appData.setAppName("AppNew3");
        appData.setCallBackUrl("https://google.com");
        appData.setTenantId("12345");
        JSONObject response = new JSONObject();
        try {
            response = applicationManager.addApplication(appData);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Data added :" + response.toString());
    }

    private static void removeApplication(String name, String id) {
        JSONObject response = new JSONObject();
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

        JSONObject response = new JSONObject();
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
