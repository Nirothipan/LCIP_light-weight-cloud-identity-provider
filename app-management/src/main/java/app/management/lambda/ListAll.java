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

    private static JsonArray createOutput(List<ApplicationDataEntity> appDataEntityList, String tenantId) {

        JsonArray allApps = new JsonArray();

        for (ApplicationDataEntity appDataEntity : appDataEntityList) {

            JsonObject app = new JsonObject();

            app.addProperty("tenantName", appDataEntity.getTenantId());
            app.addProperty("clientID", appDataEntity.getClientId());
            app.addProperty("applicationName", appDataEntity.getAppName());
            app.addProperty("callbackURL", appDataEntity.getCallBackUrl());

            if (tenantId != null && !tenantId.isEmpty()) {
                if (tenantId.equals(appDataEntity.getTenantId())) {
                    allApps.add(app);
                }
            } else {
                allApps.add(app);
            }
        }

        return allApps;
    }

}
