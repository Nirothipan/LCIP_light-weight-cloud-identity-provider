package app.management.model.config;

/**
 * This class is used to load the database details from file licensekeygenerator.yaml.
 *
 * @since 1.0.0
 */
public class DatabaseConnection {

    private UserCredentials credentials;
    private String endpoint;
    private String poolSize;
    private int maxRetries;
    private int retryInterval;

    public String getPoolSize() {

        return poolSize;
    }

    public void setPoolSize(String poolSize) {

        this.poolSize = poolSize;
    }

    public int getMaxRetries() {

        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {

        this.maxRetries = maxRetries;
    }

    public int getRetryInterval() {

        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {

        this.retryInterval = retryInterval;
    }

    public UserCredentials getCredentials() {

        return credentials;
    }

    public void setCredentials(UserCredentials credentials) {

        this.credentials = credentials;
    }

    public String getEndpoint() {

        return endpoint;
    }

    public void setEndpoint(String endpoint) {

        this.endpoint = endpoint;
    }

}
