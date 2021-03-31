package user.management.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.management.UserManagement;
import user.management.model.entity.UserDataEntity;

import java.util.List;

public class ListAllUsers extends UserManagement implements RequestHandler<UserDataEntity, Object> {

    @Override
    public Object handleRequest(UserDataEntity userDataEntity, com.amazonaws.services.lambda.runtime.Context context) {
        return getUser(userDataEntity.getTenantId());
    }

    public static void main(String[] args) {
        getUser(null);
    }

    private static Object getUser(String tenantId) {



        try {
            List<UserDataEntity> userDataEntityList = userManager.getTenantUser();
            JSONArray response = createOutput(userDataEntityList, tenantId);
            return (response);
        } catch (Exception e) {
            e.printStackTrace();
            return (getErrorOutput(e.getMessage()));
        }
    }

    private static JSONArray createOutput(List<UserDataEntity> userDataEntityList, String tenantId) {

        JSONArray allUsers = new JSONArray();
        for (UserDataEntity userDataEntity : userDataEntityList) {

            JSONObject user = new JSONObject();
            user.put("Tenant Name", userDataEntity.getTenantId());
            user.put("User Name", userDataEntity.getUserName());
            user.put("Email", userDataEntity.getUserEmail());
            if (tenantId != null && !tenantId.isEmpty()) {
                if (tenantId.equals(userDataEntity.getTenantId())) {
                    allUsers.add(user);
                }
            } else {
                allUsers.add(user);
            }
        }
        return allUsers;
    }

}

