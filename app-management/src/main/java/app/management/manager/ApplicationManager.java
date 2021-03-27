package app.management.manager;

import app.management.model.entity.ApplicationIdDataEntity;
import com.google.gson.JsonObject;

import app.management.dao.UpdateDB;
import app.management.exception.DBException;
import app.management.model.entity.ApplicationDataEntity;
//import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.hibernate.exception.JDBCConnectionException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.UUID;

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

    private JsonObject createOutput(boolean isSuccess, String message) {

        JsonObject output = new JsonObject();
        output.addProperty("status", isSuccess ? "added" : "failed");
        output.addProperty("message", message);
        return output;
    }

    private JsonObject deleteOutput(boolean isSuccess, String message, String appName) {

        JsonObject output = new JsonObject();
        output.addProperty("status", isSuccess ? "deleted" : "failed");
        output.addProperty("message", message);
        output.addProperty("application Name", appName);
        return output;
    }

    private JsonObject createOutput(String appID, String appName, int errorCode, String message) {

        JsonObject output = new JsonObject();
        output.addProperty("clientID", appID);
        output.addProperty("appName", appName);
        output.addProperty("errorCode", errorCode);
        output.addProperty("message", message);
        return output;
    }

    public JsonObject addApplication(ApplicationDataEntity appData) throws DBException, Exception {
        // Avoid creating duplicate keys
        String appName = appData.getAppName();
        String clientID = null;

        if (appName != null && !appName.isEmpty()) {
            UUID uuid = UUID.randomUUID();;
            clientID = uuid.toString();

            ApplicationDataEntity applicationManagement = appData;
            applicationManagement.setClientId(clientID);
            persistToDB(applicationManagement);
            return createOutput(clientID, appName,0, null);
        }
        return createOutput(false, "Application Name is empty");
    }

    public JsonObject getApplication(String name, String id) throws DBException, Exception {

        ApplicationDataEntity userDataEntity = findApplication(name, id);

        JsonObject output = new JsonObject();
        output.addProperty("tenantName", userDataEntity.getTenantId());
        output.addProperty("clientID", userDataEntity.getClientId());
        output.addProperty("applicationName", userDataEntity.getAppName());
        output.addProperty("callbackURL", userDataEntity.getCallBackUrl());
        return output;

    }

    public JsonObject getApplicationWithID(String appID) throws DBException, Exception {

        ApplicationIdDataEntity userDataEntity = findApplicationWithID(appID);

        JsonObject output = new JsonObject();
        if (userDataEntity != null) {
            output.addProperty("tenantName", userDataEntity.getId());
            output.addProperty("clientID", userDataEntity.getClientId());
            output.addProperty("applicationName", userDataEntity.getAppName());
            output.addProperty("callbackURL", userDataEntity.getCallBackUrl());
            return output;
        }
        output.addProperty("Error", "Could not get Application with given ID: " + appID);
        return output;


    }

    public JsonObject deleteApplication(String  appName, String tenantID) throws DBException, Exception {

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

}
