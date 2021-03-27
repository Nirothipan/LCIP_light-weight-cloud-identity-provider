package user.management.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import user.management.UserManagement;
import user.management.model.entity.UserDataEntity;

import java.util.List;

public class ListAllUsers extends UserManagement implements RequestHandler<UserDataEntity, Object> {

    @Override
    public Object handleRequest(UserDataEntity userDataEntity, com.amazonaws.services.lambda.runtime.Context context) {
        return getUser(userDataEntity.getTenantId());
    }

    public static void main(String[] args) {
        //  getUser("niro", 1234);
    }

    private static Object getUser(String tenantId) {

        try {
            List<UserDataEntity> userDataEntityList = userManager.getTenantUser();
            JsonArray response = createOutput(userDataEntityList, tenantId);
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return getErrorOutput(e.getMessage()).toString();
        }
    }

    private static JsonArray createOutput(List<UserDataEntity> userDataEntityList, String tenantId) {

        JsonArray allUsers = new JsonArray();
        for (UserDataEntity userDataEntity : userDataEntityList) {

            JsonObject user = new JsonObject();
            user.addProperty("Tenant Name", userDataEntity.getTenantId());
            user.addProperty("User Name", userDataEntity.getUserName());
            user.addProperty("Email", userDataEntity.getUserEmail());
            if (!tenantId.isEmpty() && tenantId.equals(userDataEntity.getTenantId())) {
                allUsers.add(user);
            } else {
                allUsers.add(user);
            }
        }
        return allUsers;
    }

}
