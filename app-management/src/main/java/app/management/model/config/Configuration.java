package app.management.model.config;

/**
 * Configuration to load configs from licensekeygenerator.yaml file.
 *
 * @since 1.0.0
 */

public class Configuration {

    private DatabaseConnection database = new DatabaseConnection();
    private MicroServiceConnection microserviceConnection = new MicroServiceConnection();
    private SalesForceConnection salesForceConnection = new SalesForceConnection();
    private KeyFileInfo keyFileInfo = new KeyFileInfo();

    public KeyFileInfo getKeyFileInfo() {

        return keyFileInfo;
    }

    public void setKeyFileInfo(KeyFileInfo keyFileInfo) {

        this.keyFileInfo = keyFileInfo;
    }

    public SalesForceConnection getSalesForceConnection() {

        return salesForceConnection;
    }

    public void setSalesForceConnection(SalesForceConnection salesForceConnection) {

        this.salesForceConnection = salesForceConnection;
    }

    public MicroServiceConnection getMicroServiceConnection() {

        return microserviceConnection;
    }

    public void setMicroServiceConnection(MicroServiceConnection microserviceConnection) {

        this.microserviceConnection = microserviceConnection;
    }

    public DatabaseConnection getDatabaseConfig() {

        return database;
    }

    public void setDatabaseConfig(DatabaseConnection database) {

        this.database = database;
    }
}
