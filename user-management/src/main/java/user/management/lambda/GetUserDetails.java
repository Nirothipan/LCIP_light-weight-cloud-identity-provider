package user.management.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import user.management.UserManagement;
import user.management.model.entity.UserDataEntity;

public class GetUserDetails extends UserManagement implements RequestHandler<UserDataEntity, JsonObject> {

    @Override
    public JsonObject handleRequest(UserDataEntity userDataEntity,
                                    com.amazonaws.services.lambda.runtime.Context context) {
        return getUser(userDataEntity.getUserName(), userDataEntity.getTenantId());
    }

    public static void main(String[] args) {
        // getUser("ram2","test");
    }

    private static JsonObject getUser(String name, String id) {

        try {
            return userManager.getUser(name, id);
        } catch (Exception e) {
            e.printStackTrace();
            return getErrorOutput(e.getMessage());
        }
    }

}

