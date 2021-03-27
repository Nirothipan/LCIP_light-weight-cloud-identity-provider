package tenant.management.manager;

import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.hibernate.exception.JDBCConnectionException;
import tenant.management.dao.UpdateDB;
import tenant.management.exception.DBException;
import tenant.management.model.entity.TenantDataEntity;

import java.util.List;
import javax.persistence.PersistenceException;

public class TenantManager {

    // private final Configuration config;
    private final UpdateDB updateDB;

    private static final int MAX_RETRIES = 3;

    public TenantManager(UpdateDB updateDB) {

        // this.config = config;
        this.updateDB = updateDB;

    }

    private JsonObject createOutput(boolean isSuccess, String message) {

        JsonObject output = new JsonObject();
        output.addProperty("Status", isSuccess ? "success" : "failure");
        return output;
    }

    public JsonObject addTenant(TenantDataEntity tenantData) throws DBException, Exception {
        // Avoid creating duplicate keys
        String tenantId = tenantData.getTenantId();

        if (tenantId == null || tenantId.isEmpty()) {
            throw new Exception(" Tenant ID cannot be empty");
        }
        persistToDB(tenantData);
        return createOutput(true, null);
    }

    public JsonObject deleteTenant(String id) throws DBException, Exception {

        removeFromDB(id);
        return createOutput(true, null);
    }

    public JsonObject getTenant(String id) throws DBException, Exception {

        TenantDataEntity tenantDataEntity = getFromDB(id);

        JsonObject tenant = new JsonObject();
        tenant.addProperty("Tenant Id", tenantDataEntity.getTenantId());
        tenant.addProperty("Tenant Name", tenantDataEntity.getTenantName());

        return tenant;

    }

    public List<TenantDataEntity> getAllTenants() throws DBException, Exception {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                return updateDB.getAllTenants();
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

    private synchronized TenantDataEntity getFromDB(String id) throws DBException {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                return updateDB.findEntity(id);
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

    private synchronized void removeFromDB(String id) throws DBException {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                updateDB.removeEntity(id);
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

    private synchronized void persistToDB(TenantDataEntity tenantData) throws DBException {

        int numAttempts = 0;
        do {
            ++numAttempts;
            try {
                updateDB.insertEntity(tenantData);
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
