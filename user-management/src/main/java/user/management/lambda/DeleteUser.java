package user.management.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import user.management.UserManagement;
import user.management.model.entity.UserDataEntity;

public class DeleteUser extends UserManagement implements RequestHandler<UserDataEntity, Object> {

    @Override
    public Object handleRequest(UserDataEntity userData, Context context) {
        return removeUser(userData.getUserName(), userData.getTenantId());
    }

    public static void main(String[] args) {
        // removeUser("niro", "1234");
    }

    private static Object removeUser(String name, String id) {

        try {
            JsonObject response = userManager.deletUser(name, id);
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return getErrorOutput(e.getMessage()).toString();
        }
    }

}
