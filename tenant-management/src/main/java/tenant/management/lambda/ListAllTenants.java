package tenant.management.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import tenant.management.TenantManagement;
import tenant.management.model.entity.TenantDataEntity;

import java.util.List;

public class ListAllTenants extends TenantManagement implements RequestHandler<TenantDataEntity, Object> {

    @Override
    public Object handleRequest(TenantDataEntity tenantDataEntity, com.amazonaws.services.lambda.runtime.Context context) {
        return getTenant();
    }

    public static void main(String[] args) {
        getTenant();
    }

    private static Object getTenant() {

        try {
            List<TenantDataEntity> tenantDataEntityList = tenantManager.getAllTenants();
            JsonArray response = createOutput(tenantDataEntityList);
            System.out.println(response.toString());
            return toJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            return toJson(getErrorOutput(e.getMessage()));
        }
    }

    private static JsonArray createOutput(List<TenantDataEntity> tenantDataEntityList) {

        JsonArray allTenants = new JsonArray();
        for (TenantDataEntity tenantDataEntity : tenantDataEntityList) {

            JsonObject tenant = new JsonObject();
            tenant.addProperty("Tenant Id", tenantDataEntity.getTenantId());
            tenant.addProperty("Tenant Name", tenantDataEntity.getTenantName());
            tenant.addProperty("Tenant Admin", tenantDataEntity.getAdminName());
            allTenants.add(tenant);
        }
        return allTenants;
    }

}

