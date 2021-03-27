package user.management;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import user.management.model.entity.UserDataEntity;

import java.util.List;

public class ListAllUsersLambda extends UserManagement implements RequestHandler<UserDataEntity, Object> {

    @Override
    public Object handleRequest(UserDataEntity userDataEntity, com.amazonaws.services.lambda.runtime.Context context) {
        return null;
    }

    public static void main(String[] args) {
        getUser("niro", 1234);
    }

    private static Object getUser(String name, int id) {

        JsonArray response = null;//= new JsonObject();
        try {
            List<UserDataEntity> userDataEntityList = userManager.getTenantUser();
            response = createOutput(userDataEntityList, "1234");
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
            e.printStackTrace();
        }
        System.out.println("Data added :" + response.toString());
        return response;
    }

    private static JsonArray createOutput(List<UserDataEntity> userDataEntityList, String tenantId) {

        JsonArray allUsers = new JsonArray();

        for (UserDataEntity userDataEntity : userDataEntityList) {

            JsonObject user = new JsonObject();

            user.addProperty("tenantName", userDataEntity.getId());
            user.addProperty("name", userDataEntity.getUsername());
            user.addProperty("email", userDataEntity.getUserEmail());

            if (!tenantId.isEmpty() && tenantId.equals(userDataEntity.getId())) {
                allUsers.add(user);
            } else {
                allUsers.add(user);
            }
        }

        return allUsers;
    }

}

