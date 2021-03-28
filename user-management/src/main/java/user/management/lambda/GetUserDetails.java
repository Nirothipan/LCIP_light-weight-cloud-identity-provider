package user.management.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;
import user.management.UserManagement;
import user.management.model.entity.UserDataEntity;

public class GetUserDetails extends UserManagement implements RequestHandler<UserDataEntity, Object> {

    @Override
    public Object handleRequest(UserDataEntity userDataEntity, com.amazonaws.services.lambda.runtime.Context context) {
        return getUser(userDataEntity.getUserName(), userDataEntity.getTenantId());
    }

    public static void main(String[] args) {
        System.out.println(getUser("ram", "del").toString());
    }

    private static Object getUser(String name, String id) {

        try {
            UserDataEntity userDataEntity = userManager.getUser(name, id);
            if (userDataEntity == null) {
                return null;
            }
            JSONObject user = new JSONObject();
            user.put("Tenant Name", userDataEntity.getTenantId());
            user.put("User Name", userDataEntity.getUserName());
            user.put("Email", userDataEntity.getUserEmail());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return getErrorOutput(e.getMessage());
        }
    }

}

