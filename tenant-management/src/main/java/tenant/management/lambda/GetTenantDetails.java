package tenant.management.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;
import tenant.management.TenantManagement;
import tenant.management.model.entity.TenantDataEntity;

public class GetTenantDetails extends TenantManagement implements RequestHandler<TenantDataEntity, Object> {

    @Override
    public Object handleRequest(TenantDataEntity tenantDataEntity,
                                com.amazonaws.services.lambda.runtime.Context context) {
        return gettenant(tenantDataEntity.getTenantId());
    }

    public static void main(String[] args) {
        // gettenant("niro","12212");
    }

    private static Object gettenant(String id) {

        try {
            JSONObject response = tenantManager.getTenant(id);
            return (response);
        } catch (Exception e) {
            e.printStackTrace();
            return (getErrorOutput(e.getMessage()));
        }
    }

}

