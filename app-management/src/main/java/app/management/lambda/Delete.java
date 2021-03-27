package app.management.lambda;

import app.management.ApplicationManagement;
import app.management.model.entity.ApplicationDataEntity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;

public class Delete extends ApplicationManagement implements RequestHandler<ApplicationDataEntity, Object> {

    @Override
    public Object handleRequest(ApplicationDataEntity appData, Context context) {

        return removeApplication(appData.getAppName(),appData.getTenantId());
    }

    public static void main(String[] args) {

        removeApplication();
    }

    private static void removeApplication(){
        ApplicationDataEntity userData = new ApplicationDataEntity();
        userData.setAppName("AppNew3");
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
            return getErrorOutput(e.getMessage()).toString();
        }
        return response.toString();
    }
}
