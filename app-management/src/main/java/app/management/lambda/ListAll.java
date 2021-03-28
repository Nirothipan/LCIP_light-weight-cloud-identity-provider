package app.management.lambda;

import app.management.ApplicationManagement;
import app.management.model.entity.ApplicationDataEntity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

        JSONArray response = new JSONArray();
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

    private static JSONArray createOutput(List<ApplicationDataEntity> appDataEntityList, String tenantId) {

        JSONArray allApps = new JSONArray();

        for (ApplicationDataEntity appDataEntity : appDataEntityList) {

            JSONObject app = new JSONObject();

            app.put("tenantName", appDataEntity.getTenantId());
            app.put("clientID", appDataEntity.getClientId());
            app.put("applicationName", appDataEntity.getAppName());
            app.put("callbackURL", appDataEntity.getCallBackUrl());

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
