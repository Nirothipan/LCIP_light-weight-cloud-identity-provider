package app.management.model.config;

/**
 * SalesForce Connection class.
 *
 * @since 1.0.0
 */
public class SalesForceConnection {

    private UserCredentials credentials;
    private String endpoint;

    public String getEndpoint() {

        return endpoint;
    }

    public void setEndpoint(String endpoint) {

        this.endpoint = endpoint;
    }

    public UserCredentials getCredentials() {

        return credentials;
    }

    public void setCredentials(UserCredentials credentials) {

        this.credentials = credentials;
    }
}
