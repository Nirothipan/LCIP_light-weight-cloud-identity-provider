package app.management.lambda;

import app.management.ApplicationManagement;
import app.management.model.entity.ApplicationDataEntity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;

public class Delete extends ApplicationManagement implements RequestHandler<ApplicationDataEntity, Object> {

    @Override
    public Object handleRequest(ApplicationDataEntity appData, Context context) {

        return removeApplication(appData.getAppName(), appData.getTenantId());
    }

    public static void main(String[] args) {

        removeApplication();
    }

    private static void removeApplication() {
        ApplicationDataEntity userData = new ApplicationDataEntity();
        userData.setAppName("AppNew3");
        JSONObject response = new JSONObject();
        try {
            response = applicationManager.deleteApplication(userData.getAppName(), "12345");
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Removed added :" + response.toString());
    }

    private Object removeApplication(String name, String id) {

        try {
            return applicationManager.deleteApplication(name, id);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
            return getErrorOutput(e.getMessage()).toString();
        }
    }
}
