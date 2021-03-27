package app.management.model.config;

/**
 * MicroService connection class.
 *
 * @since 1.0.0
 */
public class MicroServiceConnection {

    private UserCredentials credentials;

    public UserCredentials getCredentials() {

        return credentials;
    }

    public void setCredentials(UserCredentials credentials) {

        this.credentials = credentials;
    }
}
