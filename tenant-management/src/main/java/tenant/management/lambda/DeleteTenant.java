package tenant.management.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;
import tenant.management.TenantManagement;
import tenant.management.model.entity.TenantDataEntity;

public class DeleteTenant extends TenantManagement implements RequestHandler<TenantDataEntity, Object> {

    @Override
    public Object handleRequest(TenantDataEntity tenantData, Context context) {
        return removeTenant(tenantData.getTenantId());
    }

    public static void main(String[] args) {
        removeTenant("del");
    }

    private static Object removeTenant(String id) {

        try {
            JSONObject response = tenantManager.deleteTenant(id);
            return (response);
        } catch (Exception e) {
            e.printStackTrace();
            return (getErrorOutput(e.getMessage()));
        }
    }

}
