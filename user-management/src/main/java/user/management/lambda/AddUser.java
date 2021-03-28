package user.management.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import user.management.UserManagement;
import user.management.model.entity.UserDataEntity;

public class AddUser extends UserManagement implements RequestHandler<UserDataEntity, Object> {

    @Override
    public Object handleRequest(UserDataEntity userData, Context context) {
        return addUser(userData);
    }

    public static void main(String[] args) {
        addUser();
    }

    private static Object addUser(UserDataEntity userData) {
        try {
            JsonObject response = userManager.addUser(userData);
            System.out.println(toJson(response));
            return toJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            return toJson(getErrorOutput(e.getMessage()));
        }
    }

        private static Object addUser() {
            UserDataEntity userData = new UserDataEntity();
            userData.setTenantId("del");
            userData.setUserName("ram");
            userData.setUserEmail("niro@wso2.com");
            userData.setPassword("122121");
            JsonObject response = new JsonObject();
            try {
                response = userManager.addUser(userData);
            } catch (Exception e) {
                System.out.println("Exception :: " + e);
                e.printStackTrace();
            }
            System.out.println("Data added :" + response.toString());
            return response;
        }

}
