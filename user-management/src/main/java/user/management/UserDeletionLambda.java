package user.management;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import user.management.model.entity.UserDataEntity;

public class UserDeletionLambda extends UserManagement implements RequestHandler<UserDataEntity, Object> {

    @Override
    public Object handleRequest(UserDataEntity userData, Context context) {
        return removeUser("niro", 1234);
    }

    public static void main(String[] args) {
        //addUser(userData, context);
        //addUser();
        removeUser("niro", 1234);
    }

    private static Object removeUser(String name, int id) {

        JsonObject response = new JsonObject();
        try {
            response = userManager.deletUser(name, id);
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Data added :" + response.toString());
        return response;
    }

}
