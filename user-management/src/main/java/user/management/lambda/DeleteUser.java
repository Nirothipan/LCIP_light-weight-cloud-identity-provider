package user.management.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;
import user.management.UserManagement;
import user.management.model.entity.UserDataEntity;

public class DeleteUser extends UserManagement implements RequestHandler<UserDataEntity, Object> {

    @Override
    public Object handleRequest(UserDataEntity userData, Context context) {
        return removeUser(userData.getUserName(), userData.getTenantId());
    }

    public static void main(String[] args) {
        removeUser("ram", "del");
    }

    private static Object removeUser(String name, String id) {

        try {
            JSONObject response = userManager.deletUser(name, id);
            return (response);
        } catch (Exception e) {
            e.printStackTrace();
            return (getErrorOutput(e.getMessage()));
        }
    }

}
