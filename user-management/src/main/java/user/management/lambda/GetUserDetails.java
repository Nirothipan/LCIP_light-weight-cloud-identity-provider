package user.management.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import user.management.UserManagement;
import user.management.model.entity.UserDataEntity;

public class GetUserDetails extends UserManagement implements RequestHandler<UserDataEntity, UserDataEntity> {

    @Override
    public UserDataEntity handleRequest(UserDataEntity userDataEntity,
                                        com.amazonaws.services.lambda.runtime.Context context) {
        return getUser(userDataEntity.getUserName(), userDataEntity.getTenantId());
    }

    public static void main(String[] args) {
        System.out.println(getUser("ram", "del"));
    }

    private static UserDataEntity getUser(String name, String id) {

        try {
            return userManager.getUser(name, id);
        } catch (Exception e) {
            e.printStackTrace();
            return new UserDataEntity(); //getErrorOutput(e.getMessage());
        }
    }

}

