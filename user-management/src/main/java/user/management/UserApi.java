package user.management;

import com.google.gson.JsonObject;
import user.management.exception.DBException;
import user.management.model.entity.UserDataEntity;

public class UserApi extends UserManagement {

    public boolean validate(UserDataEntity userDataEntity) {
        try {
            return userManager.validateUser(userDataEntity);
        } catch (DBException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object addUser(UserDataEntity userData) {
        try {
            JsonObject response = userManager.addUser(userData);
            return toJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            return toJson(getErrorOutput(e.getMessage()));
        }
    }
}
