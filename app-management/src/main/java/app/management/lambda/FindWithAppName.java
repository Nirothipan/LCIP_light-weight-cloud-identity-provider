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
import com.google.gson.JsonObject;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class FindWithAppName extends ApplicationManagement implements RequestHandler<ApplicationDataEntity, Object> {

    @Override
    public Object handleRequest(ApplicationDataEntity appData, Context context) {
        return getApplication(appData.getAppName(), appData.getId());
    }

    public static void main(String[] args) {
        getApplication("AppNew2","1234");
    }

    private static Object getApplication(String name, String id) {

        JsonObject response = new JsonObject();
        try {
            response = applicationManager.getApplication(name, id);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
            return getErrorOutput(e.getMessage()).toString();
        }
        System.out.println("Data retrieved :" + response.toString());
        return response;
    }

}
