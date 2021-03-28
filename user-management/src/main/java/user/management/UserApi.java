package user.management;

import org.json.simple.JSONObject;
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
            JSONObject response = userManager.addUser(userData);
            return (response);
        } catch (Exception e) {
            e.printStackTrace();
            return (getErrorOutput(e.getMessage()));
        }
    }
}
