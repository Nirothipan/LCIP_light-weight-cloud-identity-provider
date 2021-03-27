package app.management.lambda;

import app.management.ApplicationManagement;
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

public class ListAll extends ApplicationManagement implements RequestHandler<ApplicationDataEntity, Object> {

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
            return getErrorOutput(e.getMessage()).toString();
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
