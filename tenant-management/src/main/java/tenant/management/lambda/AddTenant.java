package tenant.management.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;
import tenant.management.TenantManagement;
import tenant.management.lambda.dao.TenantCreationData;
import tenant.management.model.entity.TenantDataEntity;
import user.management.UserApi;
import user.management.model.entity.UserDataEntity;

public class AddTenant extends TenantManagement implements RequestHandler<TenantCreationData, Object> {

    @Override
    public Object handleRequest(TenantCreationData tenantCreationData, Context context) {
        return addtenant(tenantCreationData);
    }

    public static void main(String[] args) {
        TenantCreationData tenantCreationData = new TenantCreationData();
        tenantCreationData.setAdminEmail("admin@lcip.org");
        tenantCreationData.setAdminPassword("admin123");
        tenantCreationData.setAdminUserName("admin");
        tenantCreationData.setTenantId("lcip-super");
        addtenant(tenantCreationData);
    }

    private static Object addtenant(TenantCreationData tenantCreationData) {
        try {
            TenantDataEntity tenantData = new TenantDataEntity();
            tenantData.setTenantId(tenantCreationData.getTenantId());
            tenantData.setAdminName(tenantCreationData.getAdminUserName());
            JSONObject response = tenantManager.addTenant(tenantData);

            UserDataEntity userDataEntity = new UserDataEntity();
            userDataEntity.setTenantId(tenantCreationData.getTenantId());
            userDataEntity.setUserEmail(tenantCreationData.getAdminUserName());
            userDataEntity.setPassword(tenantCreationData.getAdminPassword());
            userDataEntity.setUserName(tenantCreationData.getAdminUserName());

            UserApi userApi = new UserApi();
            userApi.addUser(userDataEntity);
            return (response);
        } catch (Exception e) {
            e.printStackTrace();
            return (getErrorOutput(e.getMessage()));
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
