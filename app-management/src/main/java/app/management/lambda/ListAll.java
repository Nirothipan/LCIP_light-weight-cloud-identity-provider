package app.management.lambda;

import app.management.ApplicationManagement;
import app.management.model.entity.ApplicationDataEntity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class ListAll extends ApplicationManagement implements RequestHandler<ApplicationDataEntity, Object> {

    @Override
    public Object handleRequest(ApplicationDataEntity userData, Context context) {
        return getApplicationList(userData.getTenantId());
    }

    public static void main(String[] args) {

        getApplicationList("12345");
    }

    private static Object getApplicationList(String tenantId) {

        JsonArray response = new JsonArray();
        try {
            List<ApplicationDataEntity> applicationList = applicationManager.listTenantApplication();
            response = createOutput(applicationList, tenantId);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
            return getErrorOutput(e.getMessage()).toString();
        }
        System.out.println("Data added :" + response.toString());
        return response.toString();
    }

    private static JsonArray createOutput(List<ApplicationDataEntity> userDataEntityList, String tenantId) {

        JsonArray allApps = new JsonArray();

        for (ApplicationDataEntity userDataEntity : userDataEntityList) {

            JsonObject app = new JsonObject();

            app.addProperty("tenantName", userDataEntity.getTenantId());
            app.addProperty("clientID", userDataEntity.getClientId());
            app.addProperty("applicationName", userDataEntity.getAppName());
            app.addProperty("callbackURL", userDataEntity.getCallBackUrl());

            if (!tenantId.isEmpty() && tenantId.equals(userDataEntity.getTenantId())) {
                allApps.add(app);
            }
        }

        return allApps;
    }

}
