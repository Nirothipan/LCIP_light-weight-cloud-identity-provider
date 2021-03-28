package app.management.lambda;

import app.management.ApplicationManagement;
import app.management.model.entity.ApplicationDataEntity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class FindWithAppID extends ApplicationManagement implements RequestHandler<ApplicationDataEntity, Object> {

    @Override
    public Object handleRequest(ApplicationDataEntity userData, Context context) {
        return getApplication(userData.getClientId());
    }

    public static void main(String[] args) {
        getApplication("9baff252-dadc-4ee3-8ed7-b2e3bd404739");
    }

    private static Object getApplication(String appID) {

        try {
            return applicationManager.getApplicationWithID(appID);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
            return getErrorOutput(e.getMessage()).toString();
        }
    }

}
