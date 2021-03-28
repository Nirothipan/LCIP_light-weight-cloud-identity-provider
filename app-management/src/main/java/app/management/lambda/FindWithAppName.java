package app.management.lambda;

import app.management.ApplicationManagement;
import app.management.model.entity.ApplicationDataEntity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;

public class FindWithAppName extends ApplicationManagement implements RequestHandler<ApplicationDataEntity, Object> {

    @Override
    public Object handleRequest(ApplicationDataEntity appData, Context context) {
        return getApplication(appData.getAppName(), appData.getTenantId());
    }

    public static void main(String[] args) {
        getApplication("AppNew3", "12345");
        //        System.out.println("checking........." + checkApplication("AppNew2", "1234456"));
    }

    private static Object getApplication(String name, String id) {

        JSONObject response = new JSONObject();
        try {
            response = applicationManager.getApplication(name, id);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
            return getErrorOutput(e.getMessage()).toString();
        }
        System.out.println("Data retrieved :" + response.toString());
        return response.toString();
    }

}
