package app.management.manager;

import com.google.gson.JsonObject;

import app.management.dao.UpdateDB;
import app.management.exception.DBException;
import app.management.model.config.Configuration;
import app.management.model.entity.ApplicationDataEntity;

import javax.persistence.PersistenceException;
import java.util.UUID;

public class ApplicationManager {

    private final Configuration config;
    private final UpdateDB updateDB;

    public ApplicationManager(Configuration config) {

        this.config = config;
        this.updateDB = null;

    }

    public ApplicationManager(UpdateDB updateDB, Configuration config) {

        this.config = config;
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
            applicationManagement.setId("1234");
            persistToDB(applicationManagement);
            return createOutput(clientID, appName,0, null);
        }
        return createOutput(false, "Application Name is empty");
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
        } while (numAttempts <= config.getDatabaseConfig().getMaxRetries());
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
        } while (numAttempts <= config.getDatabaseConfig().getMaxRetries());
    }


}
