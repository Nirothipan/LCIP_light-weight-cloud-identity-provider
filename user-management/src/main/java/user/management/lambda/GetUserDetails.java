package user.management.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import user.management.UserManagement;
import user.management.model.entity.UserDataEntity;

import javax.naming.Context;

public class GetUserDetails extends UserManagement implements RequestHandler<UserDataEntity, Object> {

    @Override
    public Object handleRequest(UserDataEntity userDataEntity, com.amazonaws.services.lambda.runtime.Context context) {
        return getUser(userDataEntity.getUserName(),userDataEntity.getTenantId());
    }

    public static void main(String[] args) {
        getUser("niro","12212");
    }

    private static Object getUser(String name, String id) {

        JsonObject response = new JsonObject();
        try {
            response = userManager.getUser(name, id);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Data added :" + response.toString());
        return response.toString();
    }

}

