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

public class FindWithAppID extends ApplicationManagement implements RequestHandler<ApplicationDataEntity, Object> {


    @Override
    public Object handleRequest(ApplicationDataEntity userData, Context context) {
        return getApplication(userData.getClientId());
    }

    public static void main(String[] args) {
        getApplication("9baff252-dadc-4ee3-8ed7-b2e3bd404739");
    }

    private static Object getApplication(String appID) {

        JsonObject response = new JsonObject();
        try {
            response = applicationManager.getApplicationWithID(appID);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
            return getErrorOutput(e.getMessage()).toString();
        }
        System.out.println("Data retrieved :" + response.toString());
        return response;
    }

}
