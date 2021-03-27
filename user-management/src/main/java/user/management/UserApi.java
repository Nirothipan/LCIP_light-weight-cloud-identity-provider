package user.management;

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

}
