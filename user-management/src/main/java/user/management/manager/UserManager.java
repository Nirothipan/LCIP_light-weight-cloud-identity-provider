package user.management.manager;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.hibernate.exception.JDBCConnectionException;
import org.json.simple.JSONObject;
import user.management.dao.UpdateDB;
import user.management.exception.DBException;
import user.management.model.entity.UserDataEntity;

import java.util.List;
import javax.persistence.PersistenceException;

public class UserManager {

    // private final Configuration config;
    private final UpdateDB updateDB;

    private static final int MAX_RETRIES = 3;

    public UserManager(UpdateDB updateDB) {

        // this.config = config;
        this.updateDB = updateDB;

    }

    private JSONObject createOutput(boolean isSuccess, String message) {

        JSONObject output = new JSONObject();
        output.put("Status", isSuccess ? "success" : "failure");
        return output;
    }

    public JSONObject addUser(UserDataEntity userData) throws DBException, Exception {
        // Avoid creating duplicate keys
        String username = userData.getUserName();

        if (username == null || username.isEmpty()) {
            throw new Exception("User name cannot be empty");
        }
        persistToDB(userData);
        return createOutput(true, null);
    }

    public JSONObject deletUser(String name, String id) throws DBException, Exception {

        removeFromDB(name, id);
        return createOutput(true, null);
    }

    public UserDataEntity getUser(String name, String id) throws DBException, Exception {

        UserDataEntity userDataEntity = getFromDB(name, id);

        if (userDataEntity == null) {
            return null;
        }

        return userDataEntity;

        //        JSONObject user = new JSONObject();
        //        user.addProperty("Tenant Name", userDataEntity.getTenantId());
        //        user.addProperty("User Name", userDataEntity.getUserName());
        //        user.addProperty("Email", userDataEntity.getUserEmail());
        //
        //        return user;

    }

    // P.S do not judge me by this code
    public boolean validateUser(UserDataEntity userDataEntity) throws DBException {
        UserDataEntity entity = getFromDB(userDataEntity.getUserName(), userDataEntity.getTenantId());
        if (entity == null) {
            return false;
        }
        return entity.getPassword().equals(userDataEntity.getPassword());
    }

    public List<UserDataEntity> getTenantUser() throws DBException, Exception {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                return updateDB.getAllUsers();
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if ((cause instanceof CommunicationsException || cause instanceof JDBCConnectionException)) {
                    continue;
                }
                throw new RuntimeException(
                        "Exception occurred when creating EntityManagerFactory for the named " + "persistence unit: ",
                        e);
            }
        } while (numAttempts <= MAX_RETRIES);
        return null;

    }

    private synchronized UserDataEntity getFromDB(String name, String id) throws DBException {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                return updateDB.findEntity(name, id);
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if ((cause instanceof CommunicationsException || cause instanceof JDBCConnectionException)) {
                    continue;
                }
                throw new RuntimeException(
                        "Exception occurred when creating EntityManagerFactory for the named " + "persistence unit: ",
                        e);
            }
        } while (numAttempts <= MAX_RETRIES);
        return null;
    }

    private synchronized void removeFromDB(String name, String id) throws DBException {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                updateDB.removeEntity(name, id);
                return;
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if ((cause instanceof CommunicationsException || cause instanceof JDBCConnectionException)) {
                    continue;
                }
                throw new RuntimeException(
                        "Exception occurred when creating EntityManagerFactory for the named " + "persistence unit: ",
                        e);
            }
        } while (numAttempts <= MAX_RETRIES);
    }

    private synchronized void persistToDB(UserDataEntity userData) throws DBException {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                updateDB.insertEntity(userData);
                return;
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if ((cause instanceof CommunicationsException || cause instanceof JDBCConnectionException)) {
                    continue;
                }
                throw new RuntimeException(
                        "Exception occurred when creating EntityManagerFactory for the named " + "persistence unit: ",
                        e);
            }
        } while (numAttempts <= MAX_RETRIES);
    }

}
