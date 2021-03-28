package tenant.management.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import tenant.management.TenantManagement;
import tenant.management.model.entity.TenantDataEntity;

import java.util.List;

public class ListAllTenants extends TenantManagement implements RequestHandler<TenantDataEntity, Object> {

    @Override
    public Object handleRequest(TenantDataEntity tenantDataEntity,
                                com.amazonaws.services.lambda.runtime.Context context) {
        return getTenant();
    }

    public static void main(String[] args) {
        getTenant();
    }

    private static Object getTenant() {

        try {
            List<TenantDataEntity> tenantDataEntityList = tenantManager.getAllTenants();
            JSONArray response = createOutput(tenantDataEntityList);
            System.out.println(response.toString());
            return (response);
        } catch (Exception e) {
            e.printStackTrace();
            return (getErrorOutput(e.getMessage()));
        }
    }

    private static JSONArray createOutput(List<TenantDataEntity> tenantDataEntityList) {

        JSONArray allTenants = new JSONArray();
        for (TenantDataEntity tenantDataEntity : tenantDataEntityList) {

            JSONObject tenant = new JSONObject();
            tenant.put("Tenant Id", tenantDataEntity.getTenantId());
            tenant.put("Tenant Admin", tenantDataEntity.getAdminName());
            allTenants.add(tenant);
        }
        return allTenants;
    }

}

