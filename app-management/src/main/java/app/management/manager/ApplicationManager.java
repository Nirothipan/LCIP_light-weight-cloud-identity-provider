package app.management.manager;

import app.management.dao.UpdateDB;
import app.management.exception.DBException;
import app.management.model.entity.ApplicationDataEntity;
import app.management.model.entity.ApplicationIdDataEntity;
import org.hibernate.exception.JDBCConnectionException;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.UUID;
import javax.persistence.PersistenceException;

//import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public class ApplicationManager {

    //    private final Configuration config;
    private final UpdateDB updateDB;

    private static final int MAX_RETRIES = 3;

    //    public ApplicationManager(Configuration config) {
    //
    ////        this.config = config;
    //        this.updateDB = null;
    //
    //    }

    public ApplicationManager(UpdateDB updateDB) {

        //        this.config = config;
        this.updateDB = updateDB;

    }

    private JSONObject createOutput(boolean isSuccess, String message) {

        JSONObject output = new JSONObject();
        output.put("status", isSuccess ? "added" : "failed");
        output.put("message", message);
        return output;
    }

    private JSONObject deleteOutput(boolean isSuccess, String message, String appName) {

        JSONObject output = new JSONObject();
        output.put("status", isSuccess ? "deleted" : "failed");
        output.put("message", message);
        output.put("application Name", appName);
        return output;
    }

    private JSONObject createOutput(String appID, String appName, int errorCode, String message) {

        JSONObject output = new JSONObject();
        output.put("clientID", appID);
        output.put("appName", appName);
        output.put("errorCode", errorCode);
        output.put("message", message);
        return output;
    }

    public JSONObject addApplication(ApplicationDataEntity appData) throws DBException, Exception {
        // Avoid creating duplicate keys
        String appName = appData.getAppName();
        String clientID = null;

        if (appName != null && !appName.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            ;
            clientID = uuid.toString();

            ApplicationDataEntity applicationManagement = appData;
            applicationManagement.setClientId(clientID);
            persistToDB(applicationManagement);
            return createOutput(clientID, appName, 0, null);
        }
        return createOutput(false, "Application Name is empty");
    }

    public JSONObject getApplication(String name, String id) throws DBException, Exception {

        ApplicationDataEntity appDataEntity = findApplication(name, id);

        JSONObject output = new JSONObject();
        if (appDataEntity != null) {
            output.put("tenantName", appDataEntity.getTenantId());
            output.put("clientID", appDataEntity.getClientId());
            output.put("applicationName", appDataEntity.getAppName());
            output.put("callbackURL", appDataEntity.getCallBackUrl());
            return output;
        }
        output.put("Error", "Could not get Application with given name: " + name);
        return output;

    }

    public JSONObject getApplicationWithID(String appID) throws DBException, Exception {

        ApplicationIdDataEntity appDataEntity = findApplicationWithID(appID);

        JSONObject output = new JSONObject();
        if (appDataEntity != null) {
            output.put("tenantName", appDataEntity.getId());
            output.put("clientID", appDataEntity.getClientId());
            output.put("applicationName", appDataEntity.getAppName());
            output.put("callbackURL", appDataEntity.getCallBackUrl());
            return output;
        }
        output.put("Error", "Could not get Application with given ID: " + appID);
        return output;

    }

    public JSONObject deleteApplication(String appName, String tenantID) throws DBException, Exception {

        if (appName != null && !appName.isEmpty() && tenantID != null && !tenantID.isEmpty()) {
            removeFromDB(appName, tenantID);
            return deleteOutput(true, "Successfully deleted", appName);
        }
        return deleteOutput(false, "Delete operation failed", appName);
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
                //                if ((cause instanceof CommunicationsException || cause instanceof JDBCConnectionException)) {
                //                    continue;
                //                }
                throw new RuntimeException(
                        "Exception occurred when creating EntityManagerFactory for the named " + "persistence unit: ",
                        e);
            }
        } while (numAttempts <= MAX_RETRIES);
    }

    public List<ApplicationDataEntity> listTenantApplication() throws DBException, Exception {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                return updateDB.getAllApplication();
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if ((cause instanceof JDBCConnectionException)) {
                    continue;
                }
                throw new RuntimeException(
                        "Exception occurred when creating EntityManagerFactory for the named " + "persistence unit: ",
                        e);
            }
        } while (numAttempts <= MAX_RETRIES);
        return null;

    }

    private synchronized ApplicationDataEntity findApplication(String name, String id) throws DBException {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                return updateDB.findEntity(name, id);
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if (cause instanceof JDBCConnectionException) {
                    continue;
                }
                throw new RuntimeException(
                        "Exception occurred when creating EntityManagerFactory for the named " + "persistence unit: ",
                        e);
            }
        } while (numAttempts <= MAX_RETRIES);
        return null;
    }

    private synchronized ApplicationIdDataEntity findApplicationWithID(String appID) throws DBException {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                return updateDB.findEntityWithID(appID);
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if (cause instanceof JDBCConnectionException) {
                    continue;
                }
                throw new RuntimeException(
                        "Exception occurred when creating EntityManagerFactory for the named " + "persistence unit: ",
                        e);
            }
        } while (numAttempts <= MAX_RETRIES);
        return null;
    }

    private synchronized void persistToDB(ApplicationDataEntity userData) throws DBException {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                updateDB.insertEntity(userData);
                return;
            } catch (PersistenceException e) {
                throw new RuntimeException(
                        "Exception occurred when creating EntityManagerFactory for the named " + "persistence unit: ",
                        e);
            }
        } while (numAttempts <= MAX_RETRIES);
    }

}
