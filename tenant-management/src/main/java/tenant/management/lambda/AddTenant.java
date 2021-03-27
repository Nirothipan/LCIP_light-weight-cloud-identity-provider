package tenant.management.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import tenant.management.TenantManagement;
import tenant.management.model.entity.TenantDataEntity;

public class AddTenant extends TenantManagement implements RequestHandler<TenantDataEntity, Object> {

    @Override
    public Object handleRequest(TenantDataEntity tenantData, Context context) {
        return addtenant(tenantData);
    }

    public static void main(String[] args) {
        //addtenant();
    }

    private static Object addtenant(TenantDataEntity tenantData) {
        try {
            JsonObject response = tenantManager.addTenant(tenantData);
            return toJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            return toJson(getErrorOutput(e.getMessage()));
        }
    }

    //    private static Object addtenant() {
    //        tenantDataEntity tenantData = new tenantDataEntity();
    //        tenantData.setTenantId("del");
    //        tenantData.settenantName("ram");
    //        tenantData.settenantEmail("niro@wso2.com");
    //        tenantData.setPassword("122121");
    //        JsonObject response = new JsonObject();
    //        try {
    //            response = tenantManager.addtenant(tenantData);
    //        } catch (Exception e) {
    //            System.out.println("Exception :: " + e);
    //            e.printStackTrace();
    //        }
    //        System.out.println("Data added :" + response.toString());
    //        return response;
    //    }

}
