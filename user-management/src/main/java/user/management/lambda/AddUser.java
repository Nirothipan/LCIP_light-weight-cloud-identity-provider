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
        //addUser(userData, context);
        addUser();
    }

    private static Object addUser(UserDataEntity data) {
        UserDataEntity userData = new UserDataEntity();
        userData.setTenantId(data.getTenantId());
        userData.setUserName(data.getUserName());
        userData.setUserEmail(data.getUserEmail());
        userData.setPassword(data.getPassword());

        JsonObject response = new JsonObject();
        try {
            response = userManager.addUser(userData);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Data added :" + response.toString());
        return response.toString();
    }

    private static Object addUser() {
        UserDataEntity userData = new UserDataEntity();
        userData.setTenantId("122323");
        userData.setUserName("niro12212");
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
